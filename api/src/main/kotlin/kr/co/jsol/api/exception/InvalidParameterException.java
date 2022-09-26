package kr.co.jsol.api.exception;

import org.springframework.validation.Errors;

public class InvalidParameterException extends BasicException {

    private static final long serialVersionUID = -2116671122895194101L;

    private final Errors errors;

    public InvalidParameterException(Errors errors) {
        super(400, "bad_request_parameters", "불가능한 요청 데이터입니다.");
        this.errors = errors;
    }

    public Errors getErrors() {
        return this.errors;
    }

}