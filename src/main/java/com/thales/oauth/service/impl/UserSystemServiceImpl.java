package com.thales.oauth.service.impl;

import com.thales.oauth.domain.Permissao;
import com.thales.oauth.domain.UserSystem;
import com.thales.oauth.repository.UserSystemRepository;
import com.thales.oauth.security.UsuarioSistema;
import com.thales.oauth.service.UserSystemService;
import com.thales.oauth.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.thales.oauth.utils.Helper.createPasswordByBCrypt;
import static org.springframework.security.oauth2.common.exceptions.OAuth2Exception.INVALID_REQUEST;

@Service(value = "userService")
public class UserSystemServiceImpl implements UserSystemService, UserDetailsService {

    public static final String INVALID_PERFIL = "Perfil inválido.";
    public static final String ERRO_CRIAR_USER = "Erro ao criar usuário.";
    public static final String ERRO_USER_NOT_FOUND = "Usuário não encontrado.";
    public static final String AUTH_ERROR = "Não foi possível autenticar o usuário.";
    public static final String CAMPO_EMAIL_INVALIDO = "O e-mail é inválido.";
    public static final String CAMPO_NOVO_EMAIL_INVALIDO = "O novo e-mail é inválido";
    public static final String ERRO_ATUALIZAR_LOGIN = "Erro ao atualizar o login.";
    public static final String CAMPO_NOVO_PASSWORD_INVALIDO = "A nova senha é inválida";
    public static final String ERRO_ATUALIZAR_PASSWORD = "Erro ao atualizar a senha.";
    public static final String ERRO_RECUPERAR_PASSWORD = "Erro ao recuperar a senha.";

    private static final Logger log = LoggerFactory.getLogger(UserSystemServiceImpl.class);

    @Autowired
    private UserSystemRepository userSystemRepository;

    @Override
    @Transactional
    public UserSystem create(UserSystem userSystemRequest) {

        Helper.checkIfObjectIsNull(userSystemRequest, INVALID_REQUEST);
        this.loginIsUnique(userSystemRequest.getLogin());

        UserSystem user = new UserSystem();

        try {
            user.setCustomerId(userSystemRequest.getCustomerId());
            user.setLogin(userSystemRequest.getPassword());
            user.setPassword(createPasswordByBCrypt(userSystemRequest.getPassword()));
            user.setPermissoes(Arrays.asList(createPermissoesForUser()));
            this.userSystemRepository.saveAndFlush(user);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException(INVALID_PERFIL);
        } catch (Exception e) {
            log.error(ERRO_CRIAR_USER);
            throw new RuntimeException(ERRO_CRIAR_USER);
        }

        return user;
    }

    private boolean loginIsUnique(String login) {
        Optional<UserSystem> userOptional = Optional.ofNullable(
                this.userSystemRepository.findByLoginIgnoreCase(login));

        if (userOptional.isPresent())
            throw new RuntimeException("LOGIN_UNIQUE");

        return true;
    }

    private Permissao createPermissoesForUser (){
        Permissao p = new Permissao();
        p.setCodigo(2L);
        p.setDescricao("ROLE_USER");
        return p;
    }

    @Override //
    public UserSystem createWithCustomer(UserSystem customer) {
        return null;
    }

    @Override
    public UserSystem findByLogin(String login) {
        UserSystem user = this.userSystemRepository.findByLoginIgnoreCase(login);
        Helper.checkIfObjectIsNull(user, ERRO_USER_NOT_FOUND);

        /*UserSystemResponse userSystemResponse = new UserSystemResponse();
        userSystemResponse.setId(credential.getCodigo());
        userSystemResponse.setCustomerId(credential.getCustomerId());
        userSystemResponse.setLogin(credential.getLogin());
        userSystemResponse.setPassword("* * *");
        userSystemResponse.setCreated(credential.getCreated());
        userSystemResponse.setUpdated(credential.getUpdated());*/

        return user;
    }

    @Override //
    public void updateLoginWithCustomer(String oldLogin, UserSystem customerCustomerResponse) {

    }

    @Override //
    public void changeLogin(String newLogin, String oldLogin) {
        Helper.checkIfStringIsBlank(oldLogin, CAMPO_EMAIL_INVALIDO);
        Helper.checkIfStringIsBlank(newLogin, CAMPO_NOVO_EMAIL_INVALIDO);

        this.loginIsUnique(newLogin);

        UserSystem userSystem = this.userSystemRepository.findByLogin(oldLogin.toLowerCase());
        Helper.checkIfObjectIsNull(userSystem, ERRO_USER_NOT_FOUND);

        /*Credential credential = this.credentialRepository.findByLoginIgnoreCase(oldLogin);
        Helper.checkIfObjectIsNull(credential, ERRO_USER_NOT_FOUND);*/

        try {
            userSystem.setLogin(newLogin);
            this.userSystemRepository.saveAndFlush(userSystem);

            /*credential.setLogin(newLogin);
            this.credentialRepository.saveAndFlush(credential);*/
        } catch (Exception e) {
            log.error(ERRO_ATUALIZAR_LOGIN);
            throw new RuntimeException(ERRO_ATUALIZAR_LOGIN);
        }
    }

    @Override //
    public void changePassword(String newPassord, String login) {
        Helper.checkIfStringIsBlank(login, CAMPO_EMAIL_INVALIDO);
        Helper.checkIfStringIsBlank(newPassord, CAMPO_NOVO_PASSWORD_INVALIDO);

        /*Customer customer = this.customerRepository.findByLogin(login.toLowerCase());
        Helper.checkIfObjectIsNull(customer, CUSTOMER_NOT_FOUND);*/

        UserSystem userSystem = this.userSystemRepository.findByLoginIgnoreCase(login);
        Helper.checkIfObjectIsNull(userSystem, ERRO_USER_NOT_FOUND);

        try {
            String passwordEncoder = createPasswordByBCrypt(newPassord);

            userSystem.setPassword(passwordEncoder);
            this.userSystemRepository.saveAndFlush(userSystem);

            /*customer.setPassword(passwordEncoder);
            this.customerRepository.saveAndFlush(customer);*/
        } catch (Exception e) {
            log.error(ERRO_ATUALIZAR_PASSWORD);
            throw new RuntimeException(ERRO_ATUALIZAR_PASSWORD);
        }
    }

    @Override //
    public void changeLogingAndPassword(String newLogin, String newPassord, String login) {
        this.changeLogin(newLogin, login);
        this.changePassword(newPassord, newLogin);
    }

    @Override //
    public void recoveryPassword(String login) {
        Helper.checkIfStringIsBlank(login, CAMPO_EMAIL_INVALIDO);
        login = login.toLowerCase().trim();

        UserSystem userSystem = this.userSystemRepository.findByLogin(login);
        Helper.checkIfObjectIsNull(userSystem, ERRO_USER_NOT_FOUND);

        String senha = userSystem.getPassword();

        String template = "email/recuperar-senha";

        Map<String, Object> map = new HashMap<>();
        map.put("senha", senha);

        try {
            /*this.sendEmail.enviarEmail("testeemail@gmail.com", Arrays.asList(userSystem.getLogin()),
                    "Recuperação de senha", template, map);*/
        } catch (Exception e) {
            log.error(ERRO_RECUPERAR_PASSWORD);
            throw new RuntimeException(ERRO_RECUPERAR_PASSWORD);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserSystem user = this.userSystemRepository.findByLoginIgnoreCase(login);

        if (null == user) {
            throw new UsernameNotFoundException(AUTH_ERROR);
        }

        return new UsuarioSistema(user, this.getPermissoes(user));
    }

    private Collection<? extends GrantedAuthority> getPermissoes(UserSystem userSystem) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        userSystem.getPermissoes().forEach(
                p -> authorities.add(new SimpleGrantedAuthority(p.getDescricao().toUpperCase())));
        return authorities;
    }
}
