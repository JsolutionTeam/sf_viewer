package kr.co.jsol.domain.entity.sensorDevice

import org.springframework.data.jpa.repository.JpaRepository

interface SensorDeviceRepository : JpaRepository<SensorDevice, Long>, SensorDeviceCustomRepository
