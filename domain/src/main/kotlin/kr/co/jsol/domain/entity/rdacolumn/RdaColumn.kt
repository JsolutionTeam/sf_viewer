package kr.co.jsol.domain.entity.rdacolumn

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tb_rda_column")
class RdaColumn(
    // 수집 항목 영문명
    @Id
    @Column(name = "eng")
    val eng: String,

    // 수집 항목 한글명
    @Column(name = "kor")
    var kor: String,
) {
    override fun toString(): String {
        return "RdaColumn(eng=$eng, kor=$kor)"
    }
}
