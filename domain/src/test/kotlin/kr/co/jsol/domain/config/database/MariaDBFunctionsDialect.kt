package kr.co.jsol.domain.config.database

import org.hibernate.dialect.MariaDB106Dialect
import org.hibernate.dialect.function.SQLFunctionTemplate
import org.hibernate.type.StandardBasicTypes

class MariaDBFunctionsDialect : MariaDB106Dialect() {
    init {
        // native function 추가 - hibernate 버그인지 등록을 안 해주면 실행을 못 함
        /**
         * Function to evaluate regexp in MySQL
         * 참조 : https://stackoverflow.com/questions/17702544/hibernate-regexp-mysql/17702545#17702545
         */
//        this.registerFunction("regexp", StandardSQLFunction( "?1 REGEXP ?2", StandardBasicTypes.INTEGER));
//        registerFunction("regexp", SQLFunctionTemplate(StandardBasicTypes.INTEGER, "?1 REGEXP ?2"))
//        // 함수 키워드 등록
//        this.registerKeyword("regexp");

        registerFunction(
            "REGEXP",
            SQLFunctionTemplate(
                StandardBasicTypes.BOOLEAN, "?1 REGEXP ?2"
            )
        )
    }
}
