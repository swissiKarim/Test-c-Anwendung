package com.backend.devp.config;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;

import javax.naming.NameNotFoundException;

public class LdapAuthProvider extends LdapAuthenticationProvider {
    private LdapUserDetailsService ldapUserDetailsService;

    public LdapAuthProvider(LdapAuthenticator authenticator, LdapUserDetailsService ldapUserDetailsService) {
        super(authenticator);
        this.ldapUserDetailsService = ldapUserDetailsService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = ldapUserDetailsService.loadUserByUsername(authentication.getName());
        try {
            return new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                throw new BadCredentialsException("Bad credentials");
            } else if (e instanceof NameNotFoundException) {
                throw new RuntimeException(e.getMessage());
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        }
    }


}
