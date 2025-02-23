package com.example.top.pages.service.enumerate;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum StatusEntity {
    WAIT_APPROVE(1), APPROVED(2), CANCELED(3);

    private final Integer code;
    StatusEntity(Integer code) { this.code = code; }

}