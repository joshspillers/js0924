package com.app.demo.toolrental.services.validation;

public class ValidationException extends Exception{
    public ValidationException(String validationMsgs) {
        super("Validation Errors:\n"+validationMsgs);
    }
}
