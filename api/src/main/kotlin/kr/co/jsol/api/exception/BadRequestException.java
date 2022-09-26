package kr.co.jsol.api.exception;

public class BadRequestException extends BasicException {
    public BadRequestException() {
        super(400, "bad_request", "잘못된 요청입니다. 데이터를 확인해주세요.");
    }

    public BadRequestException(String message){
        super(400, "bad_request", message);
    }

    public BadRequestException(String entityName, String message){
        super(400, entityName+"_bad_request", message);
    }
}