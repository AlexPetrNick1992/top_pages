package com.example.top.pages.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CheckEmailResponse {
    boolean emailExist;
}
