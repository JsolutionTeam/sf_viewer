package site

import micro.dto.request.SearchCondition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.dto.response.SearchResponse

@Service
class SiteService @Autowired constructor(
    private val siteQuerydslRepository: SiteQuerydslRepository,
) {

    @Transactional(readOnly = true)
    fun getRealTime(condition: SearchCondition): List<SearchResponse> {
        return siteQuerydslRepository.getRealTime;
    }

    @Transactional(readOnly = true)
    fun getByRegTime(condition: SearchCondition): List<SearchResponse> {
        return siteQuerydslRepository.getMicroList(condition)
    }
}
