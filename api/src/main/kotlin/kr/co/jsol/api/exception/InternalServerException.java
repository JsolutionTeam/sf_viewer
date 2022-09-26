package kr.co.jsol.api.exception;

public class InternalServerException extends BasicException {
    public InternalServerException() {
        this("서버 에러 발생");
    }

    public InternalServerException(String message){
        super(500, "internal server error", message);
    }

    public InternalServerException(String entityName, String message){
        super(500, entityName+"_internal server error", message);
    }
}