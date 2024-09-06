package com.app.demo.toolrental.services.validation;

import com.app.demo.toolrental.services.tool.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class ValidatorService {
    private static final String EXIT_COMMAND = "exit";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yy");

    private final ToolService toolService;

    @Autowired
    public ValidatorService(ToolService toolService) {
        this.toolService=toolService;
    }

    public ValidationResult isToolCodeValid(String toolCode) {
        ValidationResult.ValidationResultBuilder validationBuilder = ValidationResult.builder();

        if (toolCode == null || toolCode.isBlank()) {
            validationBuilder = buildValidationFailedResult(validationBuilder, ValidationMessages.TOOL_CODE_NONE);
        }
        else if (toolService.isToolCodeExist(toolCode)) {
            validationBuilder = buildValidationSuccessResult(validationBuilder);
        }
        else {
            validationBuilder = buildValidationFailedResult(validationBuilder,ValidationMessages.TOOL_CODE_INVALID);
        }

        return validationBuilder.build();
    }

    public ValidationResult isConsoleToolCodeValid(String toolCode) {
        ValidationResult.ValidationResultBuilder validationBuilder = checkAndBuildExitValidationResult(toolCode);

        if (!validationBuilder.hasExitCode()) {
            validationBuilder = isToolCodeValid(toolCode).toBuilder();
        }

        return validationBuilder.build();
    }

    public ValidationResult isRentalDayCountValid(Integer dayCount) {
        ValidationResult.ValidationResultBuilder validationBuilder = ValidationResult.builder();

        return dayCount != null ? buildValidationSuccessResult(validationBuilder).build()
                : buildValidationFailedResult(validationBuilder,ValidationMessages.RENTAL_DAY_COUNT_NONE).build();
    }

    public ValidationResult isConsoleRentalDayCountValid(String dayCount) {
        ValidationResult.ValidationResultBuilder validationBuilder = checkAndBuildExitValidationResult(dayCount);

        if (!validationBuilder.hasExitCode()) {
            if (dayCount == null || dayCount.isBlank()) {
                validationBuilder = buildValidationFailedResult(validationBuilder,ValidationMessages.RENTAL_DAY_COUNT_NONE);
            }
            else {
                try {
                    int count = Integer.parseInt(dayCount);
                    if (count < 1) {
                        validationBuilder = buildValidationFailedResult(validationBuilder, ValidationMessages.RENTAL_DAY_COUNT_INVALID);
                    }
                    else {
                        validationBuilder = buildValidationSuccessResult(validationBuilder);
                    }
                }
                catch (NumberFormatException e) {
                    validationBuilder = buildValidationFailedResult(validationBuilder, ValidationMessages.RENTAL_DAY_COUNT_NOT_A_NUMBER);
                }
            }
        }

        return validationBuilder.build();
    }

    public ValidationResult isDiscountPercentValid(Integer discount) {
        ValidationResult.ValidationResultBuilder validationBuilder = ValidationResult.builder();

        if (discount == null || discount < 0 || discount > 100) {
            validationBuilder = buildValidationFailedResult(validationBuilder,ValidationMessages.DISCOUNT_PERCENT_INVALID_RANGE);
        }
        else {
            validationBuilder = buildValidationSuccessResult(validationBuilder);
        }

        return validationBuilder.build();
    }

    public ValidationResult isConsoleDiscountPercentValid(String discount) {
        ValidationResult.ValidationResultBuilder validationBuilder = checkAndBuildExitValidationResult(discount);

        if (!validationBuilder.hasExitCode()) {
            if (discount == null || discount.isBlank()) {
                validationBuilder = buildValidationFailedResult(validationBuilder,ValidationMessages.DISCOUNT_PERCENT_NONE);
            }
            else if (discount.contains(".")) {
                validationBuilder = buildValidationFailedResult(validationBuilder,ValidationMessages.DISCOUNT_PERCENT_INVALID);
            }
            else {
                try {
                    int discountNumber = Integer.parseInt(discount);
                    validationBuilder = isDiscountPercentValid(discountNumber).toBuilder();
                }
                catch (NumberFormatException e) {
                    validationBuilder = buildValidationFailedResult(validationBuilder,ValidationMessages.DISCOUNT_PERCENT_NOT_A_NUMBER);
                }
            }
        }

        return validationBuilder.build();
    }

    public ValidationResult isCheckoutDateValid(LocalDate date) {
        ValidationResult.ValidationResultBuilder validationBuilder = ValidationResult.builder();

        return date != null ? buildValidationSuccessResult(validationBuilder).build()
                : buildValidationFailedResult(validationBuilder,ValidationMessages.CHECKOUT_DATE_NONE).build();
    }

    public ValidationResult isConsoleCheckoutDateValid(String checkoutDate) {
        ValidationResult.ValidationResultBuilder validationBuilder = checkAndBuildExitValidationResult(checkoutDate);

        if (!validationBuilder.hasExitCode()) {
            if (checkoutDate == null || checkoutDate.isBlank()) {
                validationBuilder = buildValidationFailedResult(validationBuilder, ValidationMessages.CHECKOUT_DATE_NONE);
            }
            else {
                try {
                    LocalDate.parse(checkoutDate, dateFormatter);
                    validationBuilder = buildValidationSuccessResult(validationBuilder);
                }
                catch (DateTimeParseException e) {
                    validationBuilder = buildValidationFailedResult(validationBuilder,ValidationMessages.CHECKOUT_DATE_INVALID);
                }
            }
        }

        return validationBuilder.build();
    }

    private ValidationResult.ValidationResultBuilder checkAndBuildExitValidationResult(String validate) {
        ValidationResult.ValidationResultBuilder validationBuilder = ValidationResult.builder();

        if (isExit(validate)) {
            validationBuilder = buildExitValidationResult(validationBuilder);
        }

        return validationBuilder;
    }

    private boolean isExit(String input) {
        return input != null && EXIT_COMMAND.equalsIgnoreCase(input.trim());
    }

    private ValidationResult.ValidationResultBuilder buildExitValidationResult(ValidationResult.ValidationResultBuilder builder) {
        return buildValidationFailedResult(builder, ValidationMessages.EXIT).isExitCode(true);
    }

    private ValidationResult.ValidationResultBuilder buildValidationFailedResult(ValidationResult.ValidationResultBuilder builder, ValidationMessages message) {
        return builder.isValid(false).validationMessage(message);
    }

    private ValidationResult.ValidationResultBuilder buildValidationSuccessResult(ValidationResult.ValidationResultBuilder builder) {
        return builder.isExitCode(false).isValid(true).validationMessage(null);
    }
}
