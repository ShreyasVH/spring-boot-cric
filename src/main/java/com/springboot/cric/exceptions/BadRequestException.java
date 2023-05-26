package com.springboot.cric.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BadRequestException extends MyException
{
    private Integer httpStatusCode = 400;

    public BadRequestException(String message)
    {
        super(message);
    }
}
