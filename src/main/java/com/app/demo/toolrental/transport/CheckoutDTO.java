package com.app.demo.toolrental.transport;

import java.time.LocalDate;

public class CheckoutDTO {
    private final String toolCode;
    private final Integer rentalDayCount;
    private final Integer discountPercent;
    private final LocalDate checkoutDate;

    public CheckoutDTO(String toolCode, Integer rentalDayCount, Integer discountPercent, LocalDate checkoutDate) {
        this.toolCode = toolCode;
        this.rentalDayCount = rentalDayCount;
        this.discountPercent = discountPercent;
        this.checkoutDate = checkoutDate;
    }

    public static CheckoutDTOBuilder builder() {
        return new CheckoutDTOBuilder();
    }

    public String getToolCode() {
        return toolCode;
    }

    public Integer getRentalDayCount() {
        return rentalDayCount;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    //From DTO to DTO builder
    public CheckoutDTOBuilder toBuilder() {
        return new CheckoutDTOBuilder()
                .toolCode(this.toolCode)
                .rentalDayCount(this.rentalDayCount)
                .discountPercent(this.discountPercent)
                .checkoutDate(this.checkoutDate);
    }

    //builder pattern
    public static final class CheckoutDTOBuilder {
        private String toolCode;
        private Integer rentalDayCount;
        private Integer discountPercent;
        private LocalDate checkoutDate;

        CheckoutDTOBuilder() {
        }

        public CheckoutDTOBuilder toolCode(String toolCode) {
            this.toolCode = toolCode;
            return this;
        }

        public CheckoutDTOBuilder rentalDayCount(Integer rentalDayCount) {
            this.rentalDayCount = rentalDayCount;
            return this;
        }

        public CheckoutDTOBuilder discountPercent(Integer discountPercent) {
            this.discountPercent = discountPercent;
            return this;
        }

        public CheckoutDTOBuilder checkoutDate(LocalDate checkoutDate) {
            this.checkoutDate = checkoutDate;
            return this;
        }

        public CheckoutDTO build() {
            return new CheckoutDTO(toolCode, rentalDayCount, discountPercent, checkoutDate);
        }
    }
}
