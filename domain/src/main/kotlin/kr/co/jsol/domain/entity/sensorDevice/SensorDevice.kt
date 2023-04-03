package kr.co.jsol.domain.entity.sensorDevice

import kr.co.jsol.domain.entity.BaseEntity
import kr.co.jsol.domain.entity.site.Site
import org.hibernate.annotations.Comment
import org.hibernate.annotations.Where
import javax.persistence.*

@Where(clause = "deleted_at is null")
@Entity
@Table(
    name = "tb_sensor_device",
)
/**
 * 센서 기기
 */
class SensorDevice(

    // 센서 수집 데이터 타입 (지온, 지습 등..)
    @Column(name = "type", length = 20, nullable = false,)
    @Comment("센서 수집 데이터 타입 (지온, 지습 등..)")
    var type: String,

    // 센서 수집 데이터 타입의 단위 ex (℃, %)
    @Column(name = "unit", length = 10, nullable = false,)
    @Comment("센서 수집 데이터 타입의 단위")
    var unit: String,

    // 센서 기기 모델 명
    @Column(name = "model_name", length = 30, nullable = false,)
    @Comment("모델명")
    var modelName: String,

    // 기기 자체 일련번호
    @Column(name = "serial_number", length = 255, nullable = false,)
    @Comment("기기 자체 일련번호, 빈 값 입력 시 UUID로 생성")
    var serialNumber: String,

    // 기기 현 IP 정보
    @Column(name = "ip", length = 50,)
    @Comment("기기 현 IP 정보")
    var ip: String = "",

    // 메모
    @Column(name = "memo", length = 1000)
    @Comment("메모")
    var memo: String = "",

    // 농장 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "site_seq",
        foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT),
    )
    @Comment("농가 정보")
    var site: Site,

    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_device_id", updatable = false)
    @Comment("기본키")
    val id: Long? = null,
) : BaseEntity() {

    fun updateDeviceInfo(
        ip: String? = null,
        memo: String? = null,
    ) {
        this.ip = ip ?: this.ip
        this.memo = memo ?: this.memo
    }

    fun updateSensorInfo(
        type: String? = null,
        unit: String? = null,
        modelName: String? = null,
        serialNumber: String? = null,
    ) {
        this.type = type ?: this.type
        this.unit = unit ?: this.unit
        this.modelName = modelName ?: this.modelName
        this.serialNumber = serialNumber ?: this.serialNumber
    }

    fun updateSite(site: Site) {
        this.site = site
    }

    override fun toString(): String {
        return "SensorDevice(modelName='$modelName', serialNumber='$serialNumber', ip='$ip', memo='$memo', site=$site, id=$id)"
    }
}
