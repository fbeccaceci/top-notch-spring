package com.iplastudio.boilerplate.configs

import com.iplastudio.boilerplate.features.security.AuthenticationDetailsHolder
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@EnableJpaAuditing
@Configuration
class SpringSecurityAuditorAwareImpl : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        return Optional.of(AuthenticationDetailsHolder.details?.username ?: "system")
    }

}
