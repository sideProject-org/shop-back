package toy.shop.domain;

import lombok.Getter;

@Getter
public enum Role {

    ROLE_USER("ROLE_USER"),
    ROLE_COMPANY("ROLE_COMPANY"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }
}
