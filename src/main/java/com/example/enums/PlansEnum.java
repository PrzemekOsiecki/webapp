package com.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlansEnum {
    BASIC(1, "Basic"),
    PRO(2, "Pro");

    private final int id;
    private final String planName;

}
