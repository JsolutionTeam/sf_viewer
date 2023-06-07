package kr.co.jsol.domain.entity.rdacolumn

import org.hibernate.annotations.Comment
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(
    name = "tb_rda_column",
    indexes = [
        Index(name = "idx_rda_column_no", columnList = "no", unique = true)
    ]
)
class RdaColumn(
    @Id
    @Column(name = "property")
    @Comment("수집 항목 데이터베이스에서 사용하는 컬럼명, 사용하지 않는 값 일 시 null_번호로 표기")
    val property: String,

    @Column(name = "target")
    @Comment("수집 항목 API 송신 시 사용해야하는 키값")
    val target: String,

    @Column(name = "description")
    @Comment("수집 항목 한글명 혹은 설명")
    var description: String,

    // 조회 순서 -> 데이터 입력 순서를 맞추기 위해 사용
    @Column(name = "no")
    var no: Int,
) {
    override fun toString(): String {
        return "RdaColumn(property=$property, description=$description, key=$target, no=$no)"
    }
}
