package com.backend.devp.config;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public class CustomLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {
    private LdapTemplate ldapTemplate;

    public CustomLdapAuthoritiesPopulator(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations dirContextOperations, String username) {
        List<GroupInfo> groupInfos = ldapTemplate.search(
                "", "(objectclass=groupOfUniqueNames)", new GroupInfoAttirbuteMapper());
        Collection<GrantedAuthority> gas = new HashSet<>();
        groupInfos.forEach(groupInfo -> {
            boolean isUserExistInGroup = groupInfo.getMembers().stream().filter(member -> {
                String uid = member.split(",")[0];
                String memberName = uid.split("=")[1];
                return memberName.equalsIgnoreCase(username);
            }).count() > 0;
            if (isUserExistInGroup) {
                gas.add(new SimpleGrantedAuthority(groupInfo.getGroupName()));
            }
        });
        return gas;
    }
}
