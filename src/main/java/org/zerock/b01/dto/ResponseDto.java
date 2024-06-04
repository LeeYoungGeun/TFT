package org.zerock.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.zerock.b01.utils.ResponseStatus;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private final ResponseStatus status;
    private final String message;
    private final T data;
}
