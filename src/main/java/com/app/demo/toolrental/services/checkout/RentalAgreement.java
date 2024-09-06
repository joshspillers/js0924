package com.app.demo.toolrental.services.checkout;

import com.app.demo.toolrental.transport.ToolDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RentalAgreement {
    private final ToolDTO toolDTO;
    private final Integer totalRentalDays;
    private final LocalDate checkoutDate;
    private final BigDecimal dailyRentalCharge;
    private final Integer chargeDays;
    private final BigDecimal preDiscountCharge;
    private final Integer discountPercent;
    private final BigDecimal discountAmount;
    private final BigDecimal finalCharge;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yy");
    private static final String PERCENT_FORMATTER = "%s%%";
    //DecimalFormat is not thread-safe. Wrap it in ThreadLocal to create one per thread since it doesn't need to
    //be shared between threads.
    private final ThreadLocal<DecimalFormat> decimalFormatter = ThreadLocal.withInitial(()-> {
        DecimalFormat df = new DecimalFormat("'$'#,##0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df;
    });


    public RentalAgreement(ToolDTO toolDTO, Integer totalRentalDays, LocalDate checkoutDate, BigDecimal dailyRentalCharge,
                           Integer chargeDays, BigDecimal preDiscountCharge, Integer discountPercent, BigDecimal discountAmount,
                           BigDecimal finalCharge) {
        this.toolDTO = toolDTO;
        this.totalRentalDays = totalRentalDays;
        this.checkoutDate = checkoutDate;
        this.dailyRentalCharge = dailyRentalCharge;
        this.chargeDays = chargeDays;
        this.preDiscountCharge = preDiscountCharge;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.finalCharge = finalCharge;
    }

    public static RentalAgreementBuilder builder() {
        return new RentalAgreementBuilder();
    }

    public ToolDTO getToolDTO() {
        return toolDTO;
    }

    public String getToolCode() {
        return toolDTO !=null ? toolDTO.getCode() : null;
    }

    public String getToolType() {
        return toolDTO !=null ? toolDTO.getType() : null;
    }

    public String getToolBrand() {
        return toolDTO !=null ? toolDTO.getBrand() : null;
    }

    public Integer getTotalRentalDays() {
        return totalRentalDays;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        if (checkoutDate == null || totalRentalDays == null) {
            return null;
        }

        return checkoutDate.plusDays(totalRentalDays);
    }

    public BigDecimal getDailyRentalCharge() {
        return dailyRentalCharge;
    }

    public Integer getChargeDays() {
        return chargeDays;
    }

    public BigDecimal getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getFinalCharge() {
        return finalCharge;
    }

    public String toConsoleString() {
        return "Tool code: " + (toolDTO.getCode() != null ? toolDTO.getCode() : null) +
                "\nTool type: " + (toolDTO.getType() != null ? toolDTO.getType() : null) +
                "\nTool brand: " + (toolDTO.getBrand() != null ? toolDTO.getBrand() : null) +
                "\nRental days: " + totalRentalDays +
                "\nCheck out date: " + (checkoutDate != null ? DATE_FORMATTER.format(checkoutDate) : null) +
                "\nDue date: " + (checkoutDate != null ? DATE_FORMATTER.format(getDueDate()) : null) +
                "\nDaily rental charge: " + (dailyRentalCharge != null ? decimalFormatter.get().format(dailyRentalCharge) : null) +
                "\nCharge days: " + chargeDays +
                "\nPre-discount charge: " + (preDiscountCharge != null ? decimalFormatter.get().format(preDiscountCharge) : null) +
                "\nDiscount percent: " + String.format(PERCENT_FORMATTER, discountPercent) +
                "\nDiscount amount: " + (discountAmount != null ? decimalFormatter.get().format(discountAmount) : null) +
                "\nFinal charge: " + (finalCharge != null ? decimalFormatter.get().format(finalCharge) : null);
    }

    public RentalAgreementBuilder toBuilder() {
        return new RentalAgreementBuilder().toolDTO(toolDTO).totalRentalDays(totalRentalDays).checkoutDate(checkoutDate)
                .dailyRentalCharge(dailyRentalCharge).chargeDays(chargeDays).preDiscountCharge(preDiscountCharge)
                .discountPercent(discountPercent).discountAmount(discountAmount).finalCharge(finalCharge);
    }

    public static final class RentalAgreementBuilder {
        private ToolDTO toolDTO;
        private Integer totalRentalDays;
        private LocalDate checkoutDate;
        private BigDecimal dailyRentalCharge;
        private Integer chargeDays;
        private BigDecimal preDiscountCharge;
        private Integer discountPercent;
        private BigDecimal discountAmount;
        private BigDecimal finalCharge;

        private RentalAgreementBuilder() {
        }

        public RentalAgreementBuilder toolDTO(ToolDTO toolDTO) {
            this.toolDTO = toolDTO;
            return this;
        }

        public RentalAgreementBuilder totalRentalDays(Integer totalRentalDays) {
            this.totalRentalDays = totalRentalDays;
            return this;
        }

        public RentalAgreementBuilder checkoutDate(LocalDate checkoutDate) {
            this.checkoutDate = checkoutDate;
            return this;
        }

        public RentalAgreementBuilder dailyRentalCharge(BigDecimal dailyRentalCharge) {
            this.dailyRentalCharge = dailyRentalCharge;
            return this;
        }

        public RentalAgreementBuilder chargeDays(Integer chargeDays) {
            this.chargeDays = chargeDays;
            return this;
        }

        public RentalAgreementBuilder preDiscountCharge(BigDecimal preDiscountCharge) {
            this.preDiscountCharge = preDiscountCharge;
            return this;
        }

        public RentalAgreementBuilder discountPercent(Integer discountPercent) {
            this.discountPercent = discountPercent;
            return this;
        }

        public RentalAgreementBuilder discountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public RentalAgreementBuilder finalCharge(BigDecimal finalCharge) {
            this.finalCharge = finalCharge;
            return this;
        }

        public RentalAgreement build() {
            return new RentalAgreement(toolDTO, totalRentalDays, checkoutDate, dailyRentalCharge, chargeDays, preDiscountCharge,
                    discountPercent, discountAmount, finalCharge);
        }
    }
}
