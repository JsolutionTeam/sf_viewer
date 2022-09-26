package kr.co.jsol.api.exception.entities.relatedShop;

import com.jsol.api.config.exception.BasicException;

public class ExistRelatedShopException extends BasicException {
    public ExistRelatedShopException(Integer status, String code, String message) {
        super(status, code, message);
    }

    public ExistRelatedShopException() {
        this(409, "exist", "이미 연동된 업체 요청입니다.");
    }
}
