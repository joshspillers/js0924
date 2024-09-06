package com.app.demo.toolrental.services.validation;

public class ValidationResult {
    private final boolean isValid;
    private final boolean isExitCode;
    private final ValidationMessages validationMessage;

    public ValidationResult(boolean isValid, ValidationMessages validationMessage, boolean isExitCode) {
        this.isValid = isValid;
        this.validationMessage = validationMessage;
        this.isExitCode = isExitCode;
    }

    public static ValidationResultBuilder builder() {
        return new ValidationResultBuilder();
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isExitCode() {
        return isExitCode;
    }

    public String getValidationMessage() {
        return validationMessage.message();
    }

    public ValidationResultBuilder toBuilder() {
        return new ValidationResultBuilder().isValid(isValid).isExitCode(isExitCode).validationMessage(validationMessage);
    }

    public static final class ValidationResultBuilder {
        private boolean isValid;
        private boolean isExitCode;
        private ValidationMessages validationMessage;

        private ValidationResultBuilder() {
        }

        public boolean hasExitCode() {
            return isExitCode;
        }

        public ValidationResultBuilder isValid(boolean isValid) {
            this.isValid = isValid;
            return this;
        }

        public ValidationResultBuilder isExitCode(boolean isExitCode) {
            this.isExitCode = isExitCode;
            return this;
        }

        public ValidationResultBuilder validationMessage(ValidationMessages validationMessage) {
            this.validationMessage = validationMessage;
            return this;
        }

        public ValidationResult build() {
            return new ValidationResult(isValid, validationMessage, isExitCode);
        }
    }
}
