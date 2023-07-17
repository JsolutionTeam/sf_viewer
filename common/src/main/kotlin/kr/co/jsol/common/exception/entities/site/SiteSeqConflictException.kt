package kr.co.jsol.common.exception.entities.site

import kr.co.jsol.common.exception.BasicException

class SiteSeqConflictException : BasicException(409, "농가번호가 중복됩니다.")
