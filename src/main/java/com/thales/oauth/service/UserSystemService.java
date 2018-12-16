package com.thales.oauth.service;

import com.thales.oauth.domain.UserSystem;

public interface UserSystemService {

    UserSystem create(UserSystem credentialRequest);

    UserSystem createWithCustomer(UserSystem customer);

    UserSystem findByLogin(String login);

    void updateLoginWithCustomer(String oldLogin, UserSystem customerCustomerResponse);

    void changeLogin(String newLogin, String oldLogin);

    void changePassword(String newPassord, String login);

    void changeLogingAndPassword(String newLogin, String newPassord, String login);

    void recoveryPassword(String login);
}
