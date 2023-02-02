package kr.co.jsol.domain.entity.site

import kr.co.jsol.domain.entity.site.SiteQuerydslRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DisplayName("001.사이트 조회 테스트")
internal class SiteQuerydslRepositoryTest @Autowired constructor(
    private val siteQuerydslRepository: SiteQuerydslRepository,
) {

//    @Test
//    fun `마이크로_데이터_리스트_조회_테스트`() {
//        // given
//        val startTime = LocalDateTime.of(2022, 8, 1, 0, 0, 0)
//        val endTime = LocalDateTime.of(2022, 8, 31, 0, 0, 0)
//        val condition: SearchCondition = SearchCondition(
//            siteSeq = 2L,
//            startTime = startTime,
//            endTime = LocalDateTime.of(2022, 8, 30, 0, 0, 0),
//        )
//
//        // when
//        val result = siteQuerydslRepository.getMicroList(condition)
//        // then
//    }
}
