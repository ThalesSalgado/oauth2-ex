package com.thales.oauth.config;

import com.thales.oauth.config.token.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private static final String RESOURCE_ID = "oauth2-ex";

    @Value("${SECURITY_OAUTH2_CLIENT_CLIENT_ID}")
    private String clientId;

    @Value("${SECURITY_OAUTH2_CLIENT_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${SECURITY_OAUTH2_MOBILE_CLIENT_ID}")
    private String mobileId;

    @Value("${SECURITY_OAUTH2_MOBILE_CLIENT_SECRET}")
    private String mobileSecret;

    @Value("${SECURITY_OAUTH2_CLIENT_SCOPE}")
    private String grantType;

    @Value("${CONFIG_SECURITY_OAUTH2_REFRESH_TOKEN}")
    private String refreshToken;

    @Value("${CONFIG_SECURITY_OAUTH2_SCOPE_READ}")
    private String scopeRead;

    @Value("${CONFIG_SECURITY_OAUTH2_SCOPE_WRITE}")
    private String scopeWrite;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientId)
                .secret(clientSecret)
                .scopes(scopeRead, scopeWrite)
                .authorizedGrantTypes(grantType, refreshToken)
                .accessTokenValiditySeconds(900)
                .refreshTokenValiditySeconds(3600 * 24)
                .and()
                .withClient(mobileId)
                .secret(mobileSecret)
                .scopes(scopeRead, scopeWrite)
                .authorizedGrantTypes(grantType, refreshToken)
                .accessTokenValiditySeconds(900)
                .refreshTokenValiditySeconds(3600 * 24);

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnchancer(), accessTokenConverter()));

        endpoints.tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .reuseRefreshTokens(false)
                .authenticationManager(authenticationManager);
    }

    @Bean
    public TokenEnhancer tokenEnchancer() {
        return new CustomTokenEnhancer();
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(RESOURCE_ID);
        return jwtAccessTokenConverter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
}
