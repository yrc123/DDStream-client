package com.yrc.ddstreamclient.controller.common

import com.yrc.common.exception.common.CommonException
import com.yrc.common.exception.common.impl.SimpleException
import com.yrc.common.pojo.common.ResponseDto
import com.yrc.common.utils.ResponseUtils
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class ControllerAdvice{
    @ResponseBody
    @ExceptionHandler(CommonException::class)
    fun commonExceptionExceptionHandler(e: CommonException,
                                   res: HttpServletResponse): ResponseDto<ResponseUtils.ExceptionData> {
        res.status = e.getCode()
        return ResponseUtils.exceptionResponse(e)
    }
    @ResponseBody
    @ExceptionHandler(MalformedJwtException::class)
    fun malformedJwtExceptionHandler(e: MalformedJwtException,
                                   res: HttpServletResponse): ResponseDto<ResponseUtils.ExceptionData> {
        val simpleException = SimpleException(400, e.message ?: "")
        res.status = simpleException.getCode()
        return ResponseUtils.exceptionResponse(simpleException)
    }

    @ResponseBody
    @ExceptionHandler(SignatureException::class)
    fun malformedJwtExceptionHandler(e: SignatureException,
                                     res: HttpServletResponse): ResponseDto<ResponseUtils.ExceptionData> {
        val simpleException = SimpleException(400, e.message ?: "")
        res.status = simpleException.getCode()
        return ResponseUtils.exceptionResponse(simpleException)
    }
}