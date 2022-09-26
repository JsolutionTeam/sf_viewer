package kr.co.jsol.api.exception.entities.jwt;

import com.jsol.api.config.exception.BasicException;

public class TokenIsNotValidException extends BasicException {
    public TokenIsNotValidException() {
        super(400, "token_not_valid", "토큰정보가 비정상입니다.");
    }
}