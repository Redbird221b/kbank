package org.spectre.kbank.security.resolver

import org.aspectj.internal.lang.annotation.ajcPrivileged
import org.spectre.kbank.annotation.CurrentUser
import org.spectre.kbank.domain.User
import org.spectre.kbank.repository.UserRepository
import org.spectre.kbank.security.CustomUserDetailService
import org.spectre.kbank.service.jwt.JwtUtils
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class CurrentUserArgumentResolver(
    private val jwtUtils: JwtUtils,
    private val userRepository: UserRepository
): HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.getParameterAnnotation(CurrentUser::class.java) != null &&
                parameter.parameterType == User::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val authentication = SecurityContextHolder.getContext().authentication
        val jwtAuth = authentication as? JwtAuthenticationToken
            ?: throw IllegalStateException("Authentication is not a JWT")

        val username = jwtUtils.extractUsernameFromToken(jwtAuth.token.tokenValue)
        return userRepository.findByUsername(username)
            ?: throw IllegalStateException("User not found. $username")
    }
}