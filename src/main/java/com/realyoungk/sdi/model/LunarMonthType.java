package com.realyoungk.sdi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LunarMonthType {
    NORMAL("일반달"),
    LEAP("윤달");

    private final String description;
}