package kr.co.jsol.api.exception;

public class ExternalApiRequestException extends BasicException {
    public ExternalApiRequestException(String message, String code){
        super(500, code, message);
    }
}