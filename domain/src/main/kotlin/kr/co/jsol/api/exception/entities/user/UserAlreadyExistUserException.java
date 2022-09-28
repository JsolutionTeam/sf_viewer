package kr.co.jsol.api.exception.entities.user;

import kr.co.jsol.api.exception.BasicException;

public class UserAlreadyExistUserException extends BasicException {
    public UserAlreadyExistUserException() {
        super(409, "conflict", "이미 회원가입된 아이디입니다.");
    }

}
