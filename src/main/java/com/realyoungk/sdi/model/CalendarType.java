package com.realyoungk.sdi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CalendarType {
    SOLAR("양력"),
    LUNAR("음력");

    private final String description;
}
