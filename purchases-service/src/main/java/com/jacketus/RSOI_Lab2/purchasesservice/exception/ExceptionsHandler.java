package com.jacketus.RSOI_Lab2.purchasesservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionsHandler {
    Logger logger;

    public ExceptionsHandler(){
        logger = LoggerFactory.getLogger(ExceptionsHandler.class);
    }

    @ResponseBody
    @ExceptionHandler(PurchaseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String purchaseNotFoundHandler(PurchaseNotFoundException e){
        logger.error("Error while looking for a purchase: ", e);
        return e.getMessage();
    }
}
