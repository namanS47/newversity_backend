package com.example.NewVersity.services.authservices.interceptor

import com.google.firebase.auth.FirebaseAuth
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.security.sasl.AuthenticationException

@Component
class ApiSecurityInterceptor : HandlerInterceptor {
    private val log = LogManager.getLogger(javaClass)

    @Throws(java.lang.Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var jwtAuthToken: String? = null
        try {
            if(byPassAuth(request)) {
                return true
            }

            // Get JWT token from header value
            jwtAuthToken = request.getHeader(AUTH_HEADER_PARAMETER_AUTHERIZATION).replace(AUTH_HEADER_PARAMETER_BEARER, "")
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(jwtAuthToken)

            return true
        } catch (ae: AuthenticationException) {
            log.error("Authentication failed :  : " + ae.message)
        } catch (e: Exception) {
            log.error("Error occured while authenticating request : " + e.message)
        }
        response.status = HttpStatus.UNAUTHORIZED.value()
        return false
    }


    @Throws(java.lang.Exception::class)
    override fun postHandle(
            request: HttpServletRequest, response: HttpServletResponse, handler: Any,
            modelAndView: ModelAndView?) {
    }

    fun byPassAuth(request: HttpServletRequest) :Boolean{
        return request.method == "GET" && request.requestURI == "/spring"
    }

    companion object {
        private const val AUTH_HEADER_PARAMETER_AUTHERIZATION = "authorization"
        private const val AUTH_HEADER_PARAMETER_BEARER = "Bearer "
    }
}

@Component
class ServiceInterceptorAppConfig : WebMvcConfigurer {
    @Autowired
    var productServiceInterceptor: ApiSecurityInterceptor? = null
    override fun addInterceptors(registry: InterceptorRegistry) {
        productServiceInterceptor?.let { registry.addInterceptor(it) }
    }
}