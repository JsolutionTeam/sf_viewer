package kr.co.jsol.api.exception;

public class MyEntityNotFoundException extends BasicException {
    public MyEntityNotFoundException() {
        super(400, "not_found", "데이터를 찾을 수 없습니다.");
    }

    public MyEntityNotFoundException(String entityName, String message){
        super(400, entityName+"_not_found", message);
    }
}