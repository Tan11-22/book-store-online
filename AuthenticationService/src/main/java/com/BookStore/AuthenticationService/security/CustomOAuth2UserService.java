package com.BookStore.AuthenticationService.security;

import com.BookStore.AuthenticationService.model.AccountEntity;
import com.BookStore.AuthenticationService.model.RoleEntity;
import com.BookStore.AuthenticationService.repository.AccountRepository;
import com.BookStore.AuthenticationService.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");

        Optional<AccountEntity> accountOptional = accountRepository.findByEmail(email);
        AccountEntity account;
        
        if (accountOptional.isPresent()) {
            account = accountOptional.get();
            if (account.getGoogleId() == null) {
                account.setGoogleId(googleId);
                accountRepository.save(account);
            }
        } else {
            // Register new user
            RoleEntity userRole = roleRepository.findByRoleName("USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            
            account = new AccountEntity();
            account.setEmail(email);
            account.setGoogleId(googleId);
            account.setStatus(true);
            account.setRole(userRole);
            account.setVerified(true);
            accountRepository.save(account);
        }

        return oAuth2User;
    }
}
