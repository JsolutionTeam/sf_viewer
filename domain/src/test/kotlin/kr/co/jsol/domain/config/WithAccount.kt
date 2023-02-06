package kr.co.jsol.domain.config

import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithAccountSecurityContextFactory::class)
annotation class WithAccount(val value: String = "tester001")
