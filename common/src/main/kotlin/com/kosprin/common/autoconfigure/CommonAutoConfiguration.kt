package com.kosprin.common.autoconfigure

import com.kosprin.common.web.CorrelationIdFilter
import com.kosprin.common.web.GlobalExceptionHandler
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class CommonAutoConfiguration {
    @Bean
    fun globalExceptionHandler(): GlobalExceptionHandler = GlobalExceptionHandler()

    @Bean
    fun correlationIdFilter(): FilterRegistrationBean<CorrelationIdFilter> =
        FilterRegistrationBean(CorrelationIdFilter()).apply {
            order = Ordered.HIGHEST_PRECEDENCE
        }
}
