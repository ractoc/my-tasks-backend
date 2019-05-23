package com.ractoc.mytasksbackend.common.controller;

import com.ractoc.mytasksbackend.common.response.BaseResponse;
import com.ractoc.mytasksbackend.common.service.DuplicateEntryException;
import com.ractoc.mytasksbackend.common.service.NoSuchEntryException;
import com.ractoc.mytasksbackend.common.service.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final Logger LOG = Logger.getLogger(ExceptionControllerAdvice.class.getName());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> exceptionHandler(Exception ex) {
        LOG.log(Level.SEVERE, ex.getMessage(), ex);

        BaseResponse error = new BaseResponse();
        error.setResponseCode(INTERNAL_SERVER_ERROR.value());
        error.setMessage("Internal server error. Please contact your administrator");
        return new ResponseEntity<>(error, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse> runTimeExceptionHandler(Exception ex) {
        LOG.log(Level.SEVERE, ex.getMessage(), ex);

        BaseResponse error = new BaseResponse();
        error.setResponseCode(INTERNAL_SERVER_ERROR.value());
        error.setMessage("Internal server error. Please contact your administrator");
        return new ResponseEntity<>(error, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<BaseResponse> exceptionHandler(ServiceException ex) {
        LOG.log(Level.SEVERE, ex.getMessage(), ex);

        BaseResponse error = new BaseResponse();
        error.setResponseCode(INTERNAL_SERVER_ERROR.value());
        error.setMessage("Internal server error. Please contact your administrator");
        return new ResponseEntity<>(error, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchEntryException.class)
    public ResponseEntity<BaseResponse> exceptionHandler(NoSuchEntryException ex) {
        LOG.log(Level.INFO, ex.getMessage(), ex);

        BaseResponse error = new BaseResponse();
        error.setResponseCode(NOT_FOUND.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, OK);
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<BaseResponse> exceptionHandler(DuplicateEntryException ex) {
        LOG.log(Level.INFO, ex.getMessage(), ex);

        BaseResponse error = new BaseResponse();
        error.setResponseCode(MULTIPLE_CHOICES.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, OK);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse> exceptionHandler(MissingServletRequestParameterException ex) {
        LOG.log(Level.INFO, ex.getMessage(), ex);

        BaseResponse error = new BaseResponse();
        error.setResponseCode(UNPROCESSABLE_ENTITY.value());

        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse> exceptionHandler(ConstraintViolationException ex) {
        LOG.log(Level.INFO, ex.getMessage(), ex);

        BaseResponse error = new BaseResponse();
        error.setResponseCode(UNPROCESSABLE_ENTITY.value());

        Set<ConstraintViolation<?>> objectErrors = ex.getConstraintViolations();
        for (ConstraintViolation<?> objectError : objectErrors) {
            Map<String, Object> result = new HashMap<>();
            result.put("value", objectError.getInvalidValue());
            String path = objectError.getPropertyPath().toString();
            result.put("field", path.substring(path.lastIndexOf('.') + 1));
            error.addError(result);
        }
        return new ResponseEntity<>(error, UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse> exceptionHandler(HttpMessageNotReadableException ex) {
        LOG.log(Level.SEVERE, ex.getMessage(), ex);

        BaseResponse error = new BaseResponse();
        error.setResponseCode(UNPROCESSABLE_ENTITY.value());
        error.setMessage("Unable to parse the JSON document.");
        return new ResponseEntity<>(error, UNPROCESSABLE_ENTITY);
    }
}
