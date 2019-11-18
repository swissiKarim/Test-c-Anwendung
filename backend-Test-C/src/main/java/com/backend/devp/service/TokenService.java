package com.backend.devp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.devp.model.AccountCredentials;
import com.backend.devp.model.Token;
import com.backend.devp.util.ObjectUtil;
import com.unboundid.util.Base64;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Objects;


public final class TokenService {

    private TokenService() {

    }

    public static void createToken(HttpServletResponse response, Authentication authentication) {
        User ldapUserDetails = (User) authentication.getPrincipal();
        AccountCredentials accountCredentials = convertToAccountCredentials(ldapUserDetails);
        byte[] tokenBytes = ObjectUtil.deepCopy(accountCredentials);
        if (Objects.nonNull(tokenBytes)) {
            String encodedToken = Base64.encode(tokenBytes);
            ObjectMapper objectMapper = new ObjectMapper();
            Token token = new Token();
            token.setToken(encodedToken);
            try {
                response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(token));
                response.getWriter().flush();
            } catch (IOException e) {
                // ignore exception
            }
        }
    }

    private static AccountCredentials convertToAccountCredentials(User ldapUserDetails) {
        AccountCredentials accountCredentials = new AccountCredentials();
        accountCredentials.setAccountNonExpired(ldapUserDetails.isAccountNonExpired());
        accountCredentials.setAccountNonLocked(ldapUserDetails.isAccountNonLocked());
        accountCredentials.setAuthorities(ldapUserDetails.getAuthorities());
        accountCredentials.setUsername(ldapUserDetails.getUsername());
        return accountCredentials;
    }


    public static Authentication resolveToken(String token) {
        AccountCredentials accountCredentials = readToken(token);
        return Objects.nonNull(accountCredentials) ?
                new UsernamePasswordAuthenticationToken(accountCredentials.getUsername(),
                        accountCredentials.getPassword(),
                        accountCredentials.getAuthorities()) :
                null;
    }

    private static AccountCredentials readToken(String token) {
        ObjectInput in = null;
        AccountCredentials accountCredentials = null;
        try {
            byte[] tokenBytes = Base64.decode(token);
            ByteArrayInputStream bis = new ByteArrayInputStream(tokenBytes);
            in = new ObjectInputStream(bis);
            accountCredentials = (AccountCredentials) in.readObject();
        } catch (Exception e) {
            // ignore close exception
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return accountCredentials;
    }
}
