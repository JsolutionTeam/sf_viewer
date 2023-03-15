//package kr.co.jsol.domain.entity.site.dto.response
//
//import kr.co.jsol.domain.entity.sensor.dto.SensorTcpDto
//import kr.co.jsol.domain.entity.site.Site
//
//data class TcpSiteResponse(
//    val siteSeq: Long,
//    val delay: Long,
//    val ip: String,
//){
//    companion object {
//        fun of(site: Site): TcpSiteResponse {
//            return TcpSiteResponse(
//                siteSeq = site.id,
//                delay = site.delay,
//                ip = site.sensors[0]?.createdBy ?: "",
//            )
//        }
//    }
//}
