package kr.co.jsol.domain.config

import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.InitBinder

/**
 * 이 클래스는 모든 컨트롤러에 존재하는
 * GetMapping에서 사용하는 DTO에
 * Setter가 필요하지 않게 하기 위해 존재한다.
 */
@ControllerAdvice // 모든 컨트롤러에 적용을 위해 ControllerAdvice 적용
class WebControllerAdvice {
    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.initDirectFieldAccess()
    }
}
