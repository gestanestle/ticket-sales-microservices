package com.krimo.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ResponseObject {

    private String message;
    private Integer status;
    private Object data;

    public static ResponseObject of(String message, Integer status, Object data) {
        return new ResponseObject(message, status, data);
    }
}
