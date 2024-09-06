package com.app.demo.toolrental.services.checkout;

import com.app.demo.toolrental.services.dailycharge.DailyChargeService;
import com.app.demo.toolrental.services.tool.ToolService;
import com.app.demo.toolrental.services.utilities.DateUtils;
import com.app.demo.toolrental.services.validation.ValidationException;
import com.app.demo.toolrental.services.validation.ValidationResult;
import com.app.demo.toolrental.services.validation.ValidatorService;
import com.app.demo.toolrental.transport.CheckoutDTO;
import com.app.demo.toolrental.transport.DailyChargeDTO;
import com.app.demo.toolrental.transport.ToolDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckoutService {
    private final DailyChargeService dailyChargeService;
    private final ToolService toolService;
    private final ValidatorService validatorService;

    @Autowired
    public CheckoutService(DailyChargeService dailyChargeService, ToolService toolService, ValidatorService validatorService) {
        this.dailyChargeService = dailyChargeService;
        this.toolService = toolService;
        this.validatorService = validatorService;
    }

    public RentalAgreement generateRentalAgreement(CheckoutDTO checkoutDTO)
            throws ValidationException {
        return generateRentalAgreement(checkoutDTO, true);
    }

    public RentalAgreement generateRentalAgreement(CheckoutDTO checkoutDTO, boolean validateCheckoutInfo)
            throws ValidationException {

        //For console input, validation should have already taken place, but we can optionally re-validate in case we
        //come from a different pathway that doesn't provide for instant feedback.
        if (validateCheckoutInfo) {
            validateCheckoutInfoOrThrowException(checkoutDTO);
        }

        RentalAgreement.RentalAgreementBuilder agreementBuilder = RentalAgreement.builder();

        ToolDTO toolDTO = toolService.getToolByCode(checkoutDTO.getToolCode()).orElseThrow();
        DailyChargeDTO dailyChargeDTO = dailyChargeService.getDailyChargeById(toolDTO.getDailyChargeId()).orElseThrow();

        agreementBuilder.toolDTO(toolDTO).totalRentalDays(checkoutDTO.getRentalDayCount()).checkoutDate(checkoutDTO.getCheckoutDate())
                .dailyRentalCharge(dailyChargeDTO.getPrice()).discountPercent(checkoutDTO.getDiscountPercent());

        int chargeableDays = DateUtils.getChargeDays(checkoutDTO,dailyChargeDTO);
        BigDecimal preDiscountCharge = dailyChargeDTO.getPrice().multiply(BigDecimal.valueOf(chargeableDays));
        BigDecimal discountAmount = preDiscountCharge.multiply(BigDecimal.valueOf(checkoutDTO.getDiscountPercent()).movePointLeft(2));
        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);
        agreementBuilder.chargeDays(chargeableDays)
                .preDiscountCharge(preDiscountCharge)
                .discountAmount(discountAmount)
                .finalCharge(finalCharge);

        return agreementBuilder.build();
    }

    private void validateCheckoutInfoOrThrowException(CheckoutDTO checkoutDTO) throws ValidationException {
        List<ValidationResult> validationResults = validateCheckoutValues(checkoutDTO);
        List<ValidationResult> failedValidation = validationResults.stream().filter(v -> !v.isValid()).toList();
        if (!failedValidation.isEmpty()) {
            String validationErrorMsgs =failedValidation.stream()
                    .map(ValidationResult::getValidationMessage)
                    .collect(Collectors.joining("\n"));

            throw new ValidationException(validationErrorMsgs);
        }
    }

    private List<ValidationResult> validateCheckoutValues(CheckoutDTO checkoutDTO) {
        List<ValidationResult> results = new ArrayList<>();
        results.add(validatorService.isToolCodeValid(checkoutDTO.getToolCode()));
        results.add(validatorService.isCheckoutDateValid(checkoutDTO.getCheckoutDate()));
        results.add(validatorService.isDiscountPercentValid(checkoutDTO.getDiscountPercent()));
        results.add(validatorService.isRentalDayCountValid(checkoutDTO.getRentalDayCount()));

        return results;
    }
}
