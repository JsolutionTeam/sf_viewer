package kr.co.jsol.api.exception;

public class UnauthorizedException extends BasicException {

    public UnauthorizedException() {
        super(401, "unauthorized", "계정 정보 인증에 실패했습니다.");
    }

    public UnauthorizedException(String message) {
        super(401, "unauthorized", message);
    }
}