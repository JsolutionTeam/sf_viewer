package kr.co.jsol.api.exception.entities.jwt;

import com.jsol.api.config.exception.BasicException;

public class TokenExpiredException extends BasicException {
    public TokenExpiredException() {
        super(401, "token_expired", "로그인 세션이 만료되었습니다.");
    }
}