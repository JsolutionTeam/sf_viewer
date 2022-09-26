package kr.co.jsol.api.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "에러")
public class BasicException extends RuntimeException{
    @Schema(description = "상태")
    private Integer status;
    @Schema(description = "코드")
    private String code;
    @Schema(description = "메세지")
    private String message;
}
