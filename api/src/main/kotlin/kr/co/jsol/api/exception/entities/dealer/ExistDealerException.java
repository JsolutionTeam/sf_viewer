package kr.co.jsol.api.exception.entities.dealer;

import com.jsol.api.config.exception.BasicException;

public class ExistDealerException extends BasicException {
    public ExistDealerException(Integer status, String code, String message) {
        super(status, code, message);
    }

    public ExistDealerException() {
        this(409, "exist", "이미 존재하는 정산점(딜러)입니다.");
    }
}
