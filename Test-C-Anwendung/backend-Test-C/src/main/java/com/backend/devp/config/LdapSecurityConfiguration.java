package com.backend.devp.config;


import com.backend.devp.filters.BeforeLoginProcessingFilter;
import com.backend.devp.filters.TokenAuthenticationFilter;
import com.backend.devp.handlers.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.authentication.PasswordComparisonAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.naming.Context;
import java.util.HashMap;
import java.util.Map;


@EnableWebSecurity
@Configuration
public class LdapSecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
       
    	http.
        csrf().
        disable().
        authorizeRequests().
        antMatchers(HttpMethod.POST, "/auth¨").permitAll().
        antMatchers(HttpMethod.GET, "/**").permitAll().
        antMatchers("/api/**").authenticated().
        and().
        addFilterBefore(new TokenAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class).
        addFilterBefore(new TokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    public AbstractAuthenticationProcessingFilter authenticationProcessingFilter() throws Exception {
        BeforeLoginProcessingFilter beforeLoginProcessingFilter = new BeforeLoginProcessingFilter("/auth", authenticationManager());
        beforeLoginProcessingFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        return beforeLoginProcessingFilter;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ldapAuthenticationProvider());
    }

    @Bean
    public LdapAuthenticator ldapAuthenticator() {
        PasswordComparisonAuthenticator passwordComparisonAuthenticator = new PasswordComparisonAuthenticator(ldapContextSource());
        passwordComparisonAuthenticator.setPasswordAttributeName("userpassword");
        passwordComparisonAuthenticator.setPasswordEncoder(new LdapShaPasswordEncoder());
        passwordComparisonAuthenticator.setUserDnPatterns(new String[]{"uid={0},ou=people"});
        return passwordComparisonAuthenticator;
    }

    @Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider() {
        LdapAuthProvider ldapAuthProvider = new LdapAuthProvider(ldapAuthenticator(), ldapUserDetailsService());
        ldapAuthProvider.setUseAuthenticationRequestCredentials(true);
        return ldapAuthProvider;
    }

    @Bean
    public LdapAuthoritiesPopulator ldapAuthoritiesPopulator() {
        return new CustomLdapAuthoritiesPopulator(ldapTemplate());
    }

    @Bean
    public LdapUserSearch ldapUserSearch() {
        return new FilterBasedLdapUserSearch("", "(uid={0})", ldapContextSource());
    }

    @Bean
    public LdapUserDetailsService ldapUserDetailsService() {
        LdapUserDetailsService ldapUserDetailsService = new LdapUserDetailsService(ldapUserSearch(), ldapAuthoritiesPopulator());
        ldapUserDetailsService.setUserDetailsMapper(new UserDetailsContextMapperImpl());
        return ldapUserDetailsService;
    }


    @Bean
    public LdapContextSource ldapContextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://localhost:389/dc=springframework,dc=org");
        Map<String, Object> env = new HashMap<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:389/dc=springframework,dc=org");

        // To get rid of the PartialResultException when using Active Directory
        env.put(Context.REFERRAL, "follow");

        // Needed for the Bind (User Authorized to Query the LDAP server)
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=springframework,dc=org");
        env.put(Context.SECURITY_CREDENTIALS, "12345678");
        contextSource.setBaseEnvironmentProperties(env);
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(ldapContextSource());
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.
                ignoring()
                .antMatchers("/static/**"); // #3
    }

}
