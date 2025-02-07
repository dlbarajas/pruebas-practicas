package com.parque.diversiones.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto <T>  {

    private Boolean status;
    private T data;
    private String message;
    public ApiResponseDto() {
    }
    public ApiResponseDto(String message, T data, Boolean status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }
}
