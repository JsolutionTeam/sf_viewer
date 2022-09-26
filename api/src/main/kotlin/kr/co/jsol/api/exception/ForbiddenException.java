package kr.co.jsol.api.exception;

import com.jsol.api.service.AccountServiceImpl;

public class ForbiddenException extends BasicException {

    public ForbiddenException(String message){
        super(403, "forbidden", message);
    }

    public ForbiddenException() {
        this("권한이 없습니다." + AccountServiceImpl.getAccountFromSecurityContext().getRole());
    }
}