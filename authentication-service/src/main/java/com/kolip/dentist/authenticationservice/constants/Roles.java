package com.kolip.dentist.authenticationservice.constants;

public enum Roles {
    USER("ROLE_USER"),

    PRE_USER("ROLE_PRE_USER"),

    ADMIN("ROLE_ADMIN");

    private String roleName;

    Roles(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
