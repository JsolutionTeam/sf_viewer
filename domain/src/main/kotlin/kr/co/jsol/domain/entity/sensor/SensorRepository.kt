package kr.co.jsol.domain.entity.sensor

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SensorRepository : JpaRepository<Sensor, Long>{
    fun findAllByIsSendIsFalse(): List<Sensor>

    @Query("""
        select new map(
            s.id as id,
            s.createdAt as createdAt,
            s.rainfall as rainfall,
            s.windSpeed as windSpeed,
            s.windDirection as windDirection,
            s.temperature as temperature,
            s.humidity as humidity,
            s.solarRadiation as solarRadiation,
            s.cropTemperature as cropTemperature,
            s.cropHumidity as cropHumidity,
            s.earthTemperature as earthTemperature,
            s.earthHumidity as earthHumidity
        )
        from Sensor s
        where
            s.site.id = :siteSeq
            and (s.isSend = false or s.isSend is null)
    """)
    fun findAllBySite_IdAndIsSendIsFalsy(siteSeq: Long): List<HashMap<String, Any>>
}
