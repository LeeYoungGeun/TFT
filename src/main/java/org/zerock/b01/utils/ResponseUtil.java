package org.zerock.b01.utils;

import org.zerock.b01.dto.ResponseDto;

public class ResponseUtil {
    public static <T> ResponseDto<T> SUCCESS (String message, T data) {
        return new ResponseDto(ResponseStatus.SUCCESS, message, data);
    }

    public static <T>ResponseDto<T> FAIL (String message, T data) {
        return new ResponseDto(ResponseStatus.FAIL, message, data);
    }

    public static <T>ResponseDto<T> ERROR (String message, T data) {
        return new ResponseDto(ResponseStatus.ERROR, message, data);
    }
}
