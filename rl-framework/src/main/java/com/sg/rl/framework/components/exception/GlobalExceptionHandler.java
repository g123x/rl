package com.sg.rl.framework.components.exception;

import com.sg.rl.common.constants.ExceptionDefineEnum;
import com.sg.rl.common.entity.base.HttpResult;
import com.sg.rl.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@ControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());



    @ResponseStatus(value=HttpStatus.OK)
    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public Object serviceExceptionHandler(ServiceException e,HttpServletRequest request) {
        log.warn(" [ServiceException] errorMsg:{},uri:{},errCode:{}",e.getErrorMessage(),request.getRequestURI(),e.getCode(),e);

        HttpResult hr = new HttpResult();
        hr.setErrorCode(e.getCode());
        hr.setErrorMsg(e.getErrorMessage());
        hr.setData(new HashMap<>());
        return hr;
    }


    @ResponseStatus(value=HttpStatus.OK)
    @ExceptionHandler(value = DataAccessException.class)
    @ResponseBody
    public Object dbExceptionHandler(DataAccessException e,HttpServletRequest request) {
        log.warn(" [dbExceptionHandler] errorMsg:{},uri:{}",e.getMessage(),request.getRequestURI(),e);
        HttpResult hr = new HttpResult();
        if(e instanceof DuplicateKeyException){
            hr.setErrorCode(ExceptionDefineEnum.EXCEPTION_CODE_DB_DUP_FAILED.getCode());
            hr.setErrorMsg(ExceptionDefineEnum.EXCEPTION_CODE_DB_DUP_FAILED.getMessage());
        }
        else if(e instanceof DataIntegrityViolationException){
            hr.setErrorCode(ExceptionDefineEnum.EXCEPTION_CODE_DB_UPDATE_FAILED.getCode());
            hr.setErrorMsg(ExceptionDefineEnum.EXCEPTION_CODE_DB_UPDATE_FAILED.getMessage());
        }
        else if(e instanceof DataAccessResourceFailureException){
            hr.setErrorCode(ExceptionDefineEnum.EXCEPTION_CODE_DB_ACCESS_FAILED.getCode());
            hr.setErrorMsg(ExceptionDefineEnum.EXCEPTION_CODE_DB_ACCESS_FAILED.getMessage());
        }
        else {
            hr.setErrorCode(ExceptionDefineEnum.EXCEPTION_CODE_DB_UNKNOW_FAILED.getCode());
            hr.setErrorMsg(ExceptionDefineEnum.EXCEPTION_CODE_DB_UNKNOW_FAILED.getMessage());
        }
        hr.setData(new HashMap<>());
        return hr;
    }



    @ResponseStatus(value=HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Object httpRequestMethodNotSupportedHandler(Exception e, HttpServletRequest request) {
        log.warn(" [HttpRequestMethodNotSupportedException] errorCode:{},uri:{}", ExceptionDefineEnum.METHOD_NOT_ALLOWED.getCode()
                ,request.getRequestURI());
        HttpResult hr = new HttpResult();
        hr.setErrorCode(ExceptionDefineEnum.METHOD_NOT_ALLOWED.getCode());
        hr.setErrorMsg(ExceptionDefineEnum.METHOD_NOT_ALLOWED.getMessage());
        hr.setData(new HashMap<>());
        return hr;
    }


    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object defaultExceptionHandler(Exception e,HttpServletRequest request) {
        log.warn(" [Exception] errorMsg:{},uri:{}",e.getMessage(),request.getRequestURI(),e);
        HttpResult hr = new HttpResult();
        hr.setErrorCode(ExceptionDefineEnum.SYS_ERR.getCode());
        hr.setErrorMsg(ExceptionDefineEnum.SYS_ERR.getMessage());
        hr.setData(new HashMap<>());
        return hr;
    }



    @ResponseStatus(value=HttpStatus.OK)
    @ExceptionHandler(value=MethodArgumentNotValidException.class)
    @ResponseBody
    public Object MethodArgumentNotValidHandler(HttpServletRequest request,
                                                MethodArgumentNotValidException exception) throws Exception
    {
        log.warn(" [MethodArgumentNotValidException] errorCode:{}",400);
        HttpResult hr = new HttpResult();
        hr.setErrorCode(ExceptionDefineEnum.EXCEPTION_CODE_PARM_VALIDATION_ERR.getCode());
        StringBuffer errDetail = new StringBuffer();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errDetail.append("default message:");
            errDetail.append(error.getDefaultMessage());
            errDetail.append(",");
            errDetail.append("field:");
            errDetail.append(error.getField());
            errDetail.append(",");
            errDetail.append("rejected value:");
            errDetail.append(error.getRejectedValue());
            errDetail.append(";");
        }
        hr.setErrorMsg(errDetail.toString());
        hr.setData(new HashMap<>());
        return hr;
    }



    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public Object missingServletRequestParameterException(Exception e,HttpServletRequest request) {
        log.warn(" [HttpMessageNotReadableException] miss parameters errorCode:400");
        HttpResult hr = new HttpResult();
        hr.setErrorCode(HttpStatus.BAD_REQUEST.value());
        hr.setErrorMsg("miss parameters");
        hr.setData(new HashMap<>());
        return hr;
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MissingServletRequestPartException.class)
    @ResponseBody
    public Object missingServletRequestPartException(Exception e,HttpServletRequest request) {
        log.warn(" [HttpMessageNotReadableException] parameters is not present errorCode:400");
        HttpResult hr = new HttpResult();
        hr.setErrorCode(HttpStatus.BAD_REQUEST.value());
        hr.setErrorMsg("parameters is not present");
        hr.setData(new HashMap<>());
        return hr;
    }



    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public Object MethodArgumentTypeMismatchExceptionHandler(Exception e,HttpServletRequest request) {
        log.warn(" [MethodArgumentTypeMismatchException] errorCode:400 parameter type is mismatch");
        HttpResult hr = new HttpResult();
        hr.setErrorCode(HttpStatus.BAD_REQUEST.value());
        hr.setErrorMsg("parameter type is mismatch");
        hr.setData(new HashMap<>());
        return hr;
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public Object jsonParseExceptionHandler(Exception e,HttpServletRequest request) {
        log.warn(" [HttpMessageNotReadableException] errorCode:400 error json format ");
        HttpResult hr = new HttpResult();
        hr.setErrorCode(HttpStatus.BAD_REQUEST.value());
        hr.setErrorMsg("error json format ");
        hr.setData(new HashMap<>());
        return hr;
    }
}