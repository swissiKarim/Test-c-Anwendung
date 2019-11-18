package com.backend.devp.config;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupInfo {
    private String groupName;
    private List<String> members;

    public GroupInfo() {

    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void addMembers(String member) {
        if (Objects.isNull(members)) {
            members = new ArrayList<>();
        }
        members.add(member);
    }


    public List<String> getMembers() {
        return members;
    }
}
