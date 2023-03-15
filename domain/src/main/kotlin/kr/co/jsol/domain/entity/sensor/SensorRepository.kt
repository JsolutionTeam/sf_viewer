package kr.co.jsol.domain.entity.sensor

import org.springframework.data.jpa.repository.JpaRepository

interface SensorRepository : JpaRepository<Sensor, Long>
