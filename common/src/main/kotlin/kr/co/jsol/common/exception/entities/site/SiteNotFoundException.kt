package kr.co.jsol.common.exception.entities.site

import kr.co.jsol.common.exception.BasicException

class SiteNotFoundException : BasicException(404, "농가정보를 조회할 수 없습니다.")
