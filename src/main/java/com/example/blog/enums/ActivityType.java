package com.example.blog.enums;

import lombok.Getter;

@Getter
public enum ActivityType {
    CREATE("create"),
    VIEW("view"),
    LIKE("like"),
    COMMENT("comment"),
    SHARE("share"),
    EDIT("edit"),
    DELETE("delete"),
    LOGIN("login"),
    LOGOUT("logout");

    private final String value;

    ActivityType(String value) {
        this.value = value;
    }
}
