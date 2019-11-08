package com.backend.devp.filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.devp.model.AccountCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class BeforeLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeforeLoginProcessingFilter.class);

    public BeforeLoginProcessingFilter(String url, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        LOGGER.info("Attempting authentication");
        AccountCredentials accountCredentials = new ObjectMapper().readValue(httpServletRequest.getInputStream(), AccountCredentials.class);
       
        return getAuthenticationManager().
                authenticate(new UsernamePasswordAuthenticationToken(accountCredentials.getUsername(), accountCredentials.getPassword(), Collections.emptyList()));
    }

}
