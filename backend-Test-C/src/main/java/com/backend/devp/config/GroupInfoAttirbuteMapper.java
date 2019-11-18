package com.backend.devp.config;

import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

public class GroupInfoAttirbuteMapper implements AttributesMapper<GroupInfo> {
    @Override
    public GroupInfo mapFromAttributes(Attributes attributes) throws NamingException {
        GroupInfo groupInfo = new GroupInfo();
        String cn = (String) attributes.get("cn").get();
        groupInfo.setGroupName(cn);
        NamingEnumeration<?> uniquemembers = attributes.get("uniquemember").getAll();
        while (uniquemembers.hasMoreElements()) {
            String member = (String) uniquemembers.next();
            groupInfo.addMembers(member);
        }
        return groupInfo;

    }
}
