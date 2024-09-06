package com.app.demo.toolrental.services.validation;

public enum ValidationMessages {
    EXIT("Exit Command Received. Exiting."),
    TOOL_CODE_NONE("Tool Code not provided. Please enter a tool code."),
    TOOL_CODE_INVALID("Provided Tool Code does not exists. Please verify and re-enter Tool Code."),
    RENTAL_DAY_COUNT_NONE("Rental Day Count not provided. Please enter number of days for tool rental."),
    RENTAL_DAY_COUNT_INVALID("Rental Day Count must be greater than zero."),
    RENTAL_DAY_COUNT_NOT_A_NUMBER("The entered value is not an integer. Please enter Rental Day Count as a valid integer."),
    CHECKOUT_DATE_NONE("Checkout Date not provided. Please enter a Checkout date in the form of mm/dd/yy."),
    CHECKOUT_DATE_INVALID("The entered value is not a date of form mm/dd/yy. Please enter a valid Checkout Date."),
    DISCOUNT_PERCENT_NONE("Discount Percent not provided. Please enter a whole number between 0 and 100."),
    DISCOUNT_PERCENT_INVALID("Discount Percent is not a whole number. Please enter a whole number between 0 and 100."),
    DISCOUNT_PERCENT_NOT_A_NUMBER("The entered value is not a number. Please enter Discount Percent as a valid number."),
    DISCOUNT_PERCENT_INVALID_RANGE("The entered value is not between 0 and 100. Please enter Discount Percent as a valid number.");

    private final String message;

    ValidationMessages(String message) {
        this.message=message;
    }

    public String message(){
        return this.message;
    }
}
