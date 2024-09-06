package com.app.demo.toolrental;

import com.app.demo.toolrental.services.checkout.CheckoutService;
import com.app.demo.toolrental.services.checkout.RentalAgreement;
import com.app.demo.toolrental.services.dailycharge.DailyChargeService;
import com.app.demo.toolrental.services.tool.ToolService;
import com.app.demo.toolrental.services.validation.ValidationException;
import com.app.demo.toolrental.services.validation.ValidationMessages;
import com.app.demo.toolrental.services.validation.ValidationResult;
import com.app.demo.toolrental.services.validation.ValidatorService;
import com.app.demo.toolrental.transport.CheckoutDTO;
import com.app.demo.toolrental.transport.DailyChargeDTO;
import com.app.demo.toolrental.transport.ToolDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CheckoutServiceTest {
    @Mock
    private DailyChargeService dailyChargeService;
    @Mock
    private ToolService toolService;
    @Mock
    private ValidatorService validatorService;
    @InjectMocks
    private CheckoutService checkoutService;

    private static final DailyChargeDTO DAILY_CHARGE_LADDER_DTO = new DailyChargeDTO(1L, new BigDecimal("1.99"),true,true,false);
    private static final DailyChargeDTO DAILY_CHARGE_CHAINSAW_DTO = new DailyChargeDTO(2L, new BigDecimal("1.49"),true,false,true);
    private static final DailyChargeDTO DAILY_CHARGE_JACKHAMMER_DTO = new DailyChargeDTO(3L, new BigDecimal("2.99"),true,false,false);

    private static final ToolDTO TOOL_CHNS_CHAINSAW = new ToolDTO(1L,"CHNS","Chainsaw","Stihl",2L);
    private static final ToolDTO TOOL_LADW_LADDER = new ToolDTO(2L,"LADW","Ladder","Werner",1L);
    private static final ToolDTO TOOL_JAKD_JACKHAMMER = new ToolDTO(3L,"JAKD","Jackhammer","DeWalt",3L);
    private static final ToolDTO TOOL_JAKR_JACKHAMMER = new ToolDTO(4L,"JAKR","Jackhammer","Ridgid",3L);

    private static final ValidationResult VALIDATION_RESULT_VALID = new ValidationResult(true,null,false);
    private static final ValidationResult VALIDATION_RESULT_DISCOUNT_INVALID_RANGE = new ValidationResult(false, ValidationMessages.DISCOUNT_PERCENT_INVALID_RANGE,false);
    private static final ValidationResult VALIDATION_RESULT_TOOL_CODE_INVALID = new ValidationResult(false, ValidationMessages.TOOL_CODE_INVALID,false);
    private static final ValidationResult VALIDATION_RESULT_TOOL_CODE_NONE = new ValidationResult(false, ValidationMessages.TOOL_CODE_NONE,false);
    private static final ValidationResult VALIDATION_RESULT_RENTAL_DAYS_NONE = new ValidationResult(false, ValidationMessages.RENTAL_DAY_COUNT_NONE,false);
    private static final ValidationResult VALIDATION_RESULT_CHECKOUT_DATE_NONE = new ValidationResult(false, ValidationMessages.CHECKOUT_DATE_NONE, false);



    @BeforeEach
    public void setUp() {
        Map<Long, DailyChargeDTO> dailyChargeIdToDTOMap = Map.of(
                DAILY_CHARGE_LADDER_DTO.getId(), DAILY_CHARGE_LADDER_DTO,
                DAILY_CHARGE_CHAINSAW_DTO.getId(), DAILY_CHARGE_CHAINSAW_DTO,
                DAILY_CHARGE_JACKHAMMER_DTO.getId(), DAILY_CHARGE_JACKHAMMER_DTO);

        Map<String, ToolDTO> toolCodeToToolDTOMap = Map.of(
                TOOL_CHNS_CHAINSAW.getCode(), TOOL_CHNS_CHAINSAW,
                TOOL_LADW_LADDER.getCode(), TOOL_LADW_LADDER,
                TOOL_JAKD_JACKHAMMER.getCode(), TOOL_JAKD_JACKHAMMER,
                TOOL_JAKR_JACKHAMMER.getCode(), TOOL_JAKR_JACKHAMMER
        );

        Mockito.when(dailyChargeService.getDailyChargeById(Mockito.anyLong())).thenAnswer(v->{
            Long inputArg = v.getArgument(0, Long.class);
            if (inputArg > 0 && inputArg <= dailyChargeIdToDTOMap.size()) {
                return Optional.of(dailyChargeIdToDTOMap.get(inputArg));
            }
            return Optional.empty();
        });

        Mockito.when(toolService.getToolByCode(Mockito.anyString())).thenAnswer(v-> {
            String inputArg = v.getArgument(0, String.class);
            if (toolCodeToToolDTOMap.containsKey(inputArg.toUpperCase())) {
                return Optional.of(toolCodeToToolDTOMap.get(inputArg.toUpperCase()));
            }
            return Optional.empty();
        });

        Mockito.when(toolService.isToolCodeExist(Mockito.anyString())).thenAnswer(v-> {
            String inputArg = v.getArgument(0, String.class);
            return toolCodeToToolDTOMap.containsKey(inputArg.toUpperCase());
        });

        Mockito.when(validatorService.isToolCodeValid(Mockito.nullable(String.class))).thenAnswer(v -> {
            String inputArg = v.getArgument(0, String.class);
            if (inputArg != null && toolCodeToToolDTOMap.containsKey(inputArg.toUpperCase())) {
                return VALIDATION_RESULT_VALID;
            }
            if (inputArg == null) {
                return VALIDATION_RESULT_TOOL_CODE_NONE;
            }
            return VALIDATION_RESULT_TOOL_CODE_INVALID;
        });

        Mockito.when(validatorService.isRentalDayCountValid(Mockito.nullable(Integer.class))).thenAnswer(v -> {
            Integer inputArg = v.getArgument(0, Integer.class);
            return inputArg != null ? VALIDATION_RESULT_VALID : VALIDATION_RESULT_RENTAL_DAYS_NONE;
        });

        Mockito.when(validatorService.isDiscountPercentValid(Mockito.nullable(Integer.class))).thenAnswer(v -> {
            Integer inputArg = v.getArgument(0, Integer.class);
            if (inputArg == null || inputArg < 0 || inputArg >100) {
                return VALIDATION_RESULT_DISCOUNT_INVALID_RANGE;
            }
            return VALIDATION_RESULT_VALID;
        });

        Mockito.when(validatorService.isCheckoutDateValid(Mockito.nullable(LocalDate.class))).thenAnswer(v -> {
            LocalDate inputArg = v.getArgument(0, LocalDate.class);
            return inputArg != null ? VALIDATION_RESULT_VALID : VALIDATION_RESULT_CHECKOUT_DATE_NONE;
        });
    }

    @Test
    //This is Test 1 from requirements doc
    public void testIfInvalidDiscount_ThenThrowExceptionAndVerifyMessage() {
        String expectedErrMsg = "Validation Errors:\n"+VALIDATION_RESULT_DISCOUNT_INVALID_RANGE.getValidationMessage();
        CheckoutDTO checkoutInvalidDiscount = new CheckoutDTO("JAKR",5,101, LocalDate.of(2015,9,3));
        Assertions.assertThatThrownBy(() -> {
            checkoutService.generateRentalAgreement(checkoutInvalidDiscount);
        }).isInstanceOf(ValidationException.class).hasMessage(expectedErrMsg);
    }

    @Test
    //This is Test 2 from requirements doc
    public void testWhenIncludingHolidayThatIsNoCharge_thenHolidayIsExcludedFromChargeDays() {
        LocalDate checkoutDate = LocalDate.of(2020,7,2);
        CheckoutDTO checkoutIncludesFourthHoliday = new CheckoutDTO("LADW",3,10, checkoutDate);

        Integer expectedRentalDays = 3;
        LocalDate expectedDueDate = checkoutDate.plusDays(expectedRentalDays);
        Integer expectedChargeDays = 2;
        BigDecimal expectedPreDiscountCharge = DAILY_CHARGE_LADDER_DTO.getPrice().multiply(BigDecimal.valueOf(expectedChargeDays));
        Integer expectedDiscountPercent = 10;
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(expectedDiscountPercent).movePointLeft(2));
        BigDecimal expectedFinalAmount = expectedPreDiscountCharge.subtract(expectedDiscountAmount);

        testRentalAgreementAssertions(checkoutIncludesFourthHoliday, expectedRentalDays, checkoutDate, expectedDueDate,
                expectedChargeDays, expectedPreDiscountCharge, expectedDiscountPercent, expectedDiscountAmount,
                expectedFinalAmount, TOOL_LADW_LADDER, DAILY_CHARGE_LADDER_DTO);
    }

    @Test
    //This is Test 3 from requirements doc
    public void testWhenIncludingHolidayThatIsChargeAndOnNonChargeWeekend_thenHolidayIsExcludedFromChargeDays() {
        LocalDate checkoutDate = LocalDate.of(2015,7,2);
        CheckoutDTO checkoutIncludesFourthHoliday = new CheckoutDTO("CHNS",5,25, checkoutDate);

        Integer expectedRentalDays = 5;
        LocalDate expectedDueDate = checkoutDate.plusDays(expectedRentalDays);
        Integer expectedChargeDays = 3;
        BigDecimal expectedPreDiscountCharge = DAILY_CHARGE_CHAINSAW_DTO.getPrice().multiply(BigDecimal.valueOf(expectedChargeDays));
        Integer expectedDiscountPercent = 25;
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(expectedDiscountPercent).movePointLeft(2));
        BigDecimal expectedFinalAmount = expectedPreDiscountCharge.subtract(expectedDiscountAmount);

        testRentalAgreementAssertions(checkoutIncludesFourthHoliday, expectedRentalDays, checkoutDate, expectedDueDate,
                expectedChargeDays, expectedPreDiscountCharge, expectedDiscountPercent, expectedDiscountAmount,
                expectedFinalAmount, TOOL_CHNS_CHAINSAW, DAILY_CHARGE_CHAINSAW_DTO);
    }

    @Test
    //This is Test 4 from requirements doc
    public void testWhenIncludingHolidayThatIsNoChargeOnChargeableDay_thenHolidayIsExcludedFromChargeDays() {
        LocalDate checkoutDate = LocalDate.of(2015,9,3);
        CheckoutDTO checkoutIncludesLaborDayHoliday = new CheckoutDTO("JAKD",6,0, checkoutDate);

        Integer expectedRentalDays = 6;
        LocalDate expectedDueDate = checkoutDate.plusDays(expectedRentalDays);
        Integer expectedChargeDays = 3;
        BigDecimal expectedPreDiscountCharge = DAILY_CHARGE_JACKHAMMER_DTO.getPrice().multiply(BigDecimal.valueOf(expectedChargeDays));
        Integer expectedDiscountPercent = 0;
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(expectedDiscountPercent).movePointLeft(2));
        BigDecimal expectedFinalAmount = expectedPreDiscountCharge.subtract(expectedDiscountAmount);

        testRentalAgreementAssertions(checkoutIncludesLaborDayHoliday, expectedRentalDays, checkoutDate, expectedDueDate,
                expectedChargeDays, expectedPreDiscountCharge, expectedDiscountPercent, expectedDiscountAmount,
                expectedFinalAmount, TOOL_JAKD_JACKHAMMER, DAILY_CHARGE_JACKHAMMER_DTO);
    }

    @Test
    //This is Test 5 from requirements doc
    public void testWhenIncludingHolidayThatIsNoChargeAndOnNonChargeWeekend_thenShouldNotDiscountMoreThanOnce() {
        LocalDate checkoutDate = LocalDate.of(2015,7,2);
        CheckoutDTO checkoutIncludesFourthHoliday = new CheckoutDTO("JAKR",9,0, checkoutDate);

        Integer expectedRentalDays = 9;
        LocalDate expectedDueDate = checkoutDate.plusDays(expectedRentalDays);
        Integer expectedChargeDays = 6;
        BigDecimal expectedPreDiscountCharge = DAILY_CHARGE_JACKHAMMER_DTO.getPrice().multiply(BigDecimal.valueOf(expectedChargeDays));
        Integer expectedDiscountPercent = 0;
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(expectedDiscountPercent).movePointLeft(2));
        BigDecimal expectedFinalAmount = expectedPreDiscountCharge.subtract(expectedDiscountAmount);

        testRentalAgreementAssertions(checkoutIncludesFourthHoliday, expectedRentalDays, checkoutDate, expectedDueDate,
                expectedChargeDays, expectedPreDiscountCharge, expectedDiscountPercent, expectedDiscountAmount,
                expectedFinalAmount, TOOL_JAKR_JACKHAMMER, DAILY_CHARGE_JACKHAMMER_DTO);
    }

    @Test
    //This is Test 6  from requirements doc
    public void testWhenDifferentYear_thenSubtractHolidaysAppropriately() {
        LocalDate checkoutDate = LocalDate.of(2020,7,2);
        CheckoutDTO checkoutIncludesFourthHoliday = new CheckoutDTO("JAKR",4,50, checkoutDate);

        Integer expectedRentalDays = 4;
        LocalDate expectedDueDate = checkoutDate.plusDays(expectedRentalDays);
        Integer expectedChargeDays = 2;
        BigDecimal expectedPreDiscountCharge = DAILY_CHARGE_JACKHAMMER_DTO.getPrice().multiply(BigDecimal.valueOf(expectedChargeDays));
        Integer expectedDiscountPercent = 50;
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(expectedDiscountPercent).movePointLeft(2));
        BigDecimal expectedFinalAmount = expectedPreDiscountCharge.subtract(expectedDiscountAmount);

        testRentalAgreementAssertions(checkoutIncludesFourthHoliday, expectedRentalDays, checkoutDate, expectedDueDate,
                expectedChargeDays, expectedPreDiscountCharge, expectedDiscountPercent, expectedDiscountAmount,
                expectedFinalAmount, TOOL_JAKR_JACKHAMMER, DAILY_CHARGE_JACKHAMMER_DTO);
    }

    @Test
    //Both holidays on a weekday, test that we subtract both
    public void testWhenContainBothHolidays_thenBothHolidaysAreSubtracted() {
        LocalDate checkoutDate = LocalDate.of(2024,7,2);
        CheckoutDTO checkoutIncludesFourthHoliday = new CheckoutDTO("LADW",65,0, checkoutDate);

        Integer expectedRentalDays = 65;
        LocalDate expectedDueDate = checkoutDate.plusDays(expectedRentalDays);
        Integer expectedChargeDays = 63;
        BigDecimal expectedPreDiscountCharge = DAILY_CHARGE_LADDER_DTO.getPrice().multiply(BigDecimal.valueOf(expectedChargeDays));
        Integer expectedDiscountPercent = 0;
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(expectedDiscountPercent).movePointLeft(2));
        BigDecimal expectedFinalAmount = expectedPreDiscountCharge.subtract(expectedDiscountAmount);

        testRentalAgreementAssertions(checkoutIncludesFourthHoliday, expectedRentalDays, checkoutDate, expectedDueDate,
                expectedChargeDays, expectedPreDiscountCharge, expectedDiscountPercent, expectedDiscountAmount,
                expectedFinalAmount, TOOL_LADW_LADDER, DAILY_CHARGE_LADDER_DTO);
    }

    @Test
    //Both holidays on a weekday. Rental period crosses year. Test that we subtract both holidays.
    public void testWhenContainTwoHolidaysAcrossTwoYears_thenBothHolidaysAreSubtracted() {
        LocalDate checkoutDate = LocalDate.of(2023,9,2);
        CheckoutDTO checkoutIncludesFourthHoliday = new CheckoutDTO("LADW",350,0, checkoutDate);

        Integer expectedRentalDays = 350;
        LocalDate expectedDueDate = checkoutDate.plusDays(expectedRentalDays);
        Integer expectedChargeDays = 348;
        BigDecimal expectedPreDiscountCharge = DAILY_CHARGE_LADDER_DTO.getPrice().multiply(BigDecimal.valueOf(expectedChargeDays));
        Integer expectedDiscountPercent = 0;
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(expectedDiscountPercent).movePointLeft(2));
        BigDecimal expectedFinalAmount = expectedPreDiscountCharge.subtract(expectedDiscountAmount);

        testRentalAgreementAssertions(checkoutIncludesFourthHoliday, expectedRentalDays, checkoutDate, expectedDueDate,
                expectedChargeDays, expectedPreDiscountCharge, expectedDiscountPercent, expectedDiscountAmount,
                expectedFinalAmount, TOOL_LADW_LADDER, DAILY_CHARGE_LADDER_DTO);
    }

    @Test
    //Rental period crosses multiple years and 7 instances of a holiday. Test that we subtract all holidays.
    public void testWhenContainSevenHolidaysAcrossFourYears_thenAllHolidaysAreSubtracted() {
        LocalDate checkoutDate = LocalDate.of(2023,7,2);
        CheckoutDTO checkoutIncludesFourthHoliday = new CheckoutDTO("LADW",1100,0, checkoutDate);

        Integer expectedRentalDays = 1100;
        LocalDate expectedDueDate = checkoutDate.plusDays(expectedRentalDays);
        Integer expectedChargeDays = 1093;
        BigDecimal expectedPreDiscountCharge = DAILY_CHARGE_LADDER_DTO.getPrice().multiply(BigDecimal.valueOf(expectedChargeDays));
        Integer expectedDiscountPercent = 0;
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(expectedDiscountPercent).movePointLeft(2));
        BigDecimal expectedFinalAmount = expectedPreDiscountCharge.subtract(expectedDiscountAmount);

        testRentalAgreementAssertions(checkoutIncludesFourthHoliday, expectedRentalDays, checkoutDate, expectedDueDate,
                expectedChargeDays, expectedPreDiscountCharge, expectedDiscountPercent, expectedDiscountAmount,
                expectedFinalAmount, TOOL_LADW_LADDER, DAILY_CHARGE_LADDER_DTO);
    }

    @Test
    //Rental period crosses year because checkout date is 12/30/23.
    public void testWhenCrossesYearWithShortRentalPeriod_thenChargeDaysAreCalculatedCorrectly() {
        LocalDate checkoutDate = LocalDate.of(2023,12,30);
        CheckoutDTO checkoutIncludesFourthHoliday = new CheckoutDTO("LADW",5,0, checkoutDate);

        Integer expectedRentalDays = 5;
        LocalDate expectedDueDate = checkoutDate.plusDays(expectedRentalDays);
        Integer expectedChargeDays = 5;
        BigDecimal expectedPreDiscountCharge = DAILY_CHARGE_LADDER_DTO.getPrice().multiply(BigDecimal.valueOf(expectedChargeDays));
        Integer expectedDiscountPercent = 0;
        BigDecimal expectedDiscountAmount = expectedPreDiscountCharge.multiply(BigDecimal.valueOf(expectedDiscountPercent).movePointLeft(2));
        BigDecimal expectedFinalAmount = expectedPreDiscountCharge.subtract(expectedDiscountAmount);

        testRentalAgreementAssertions(checkoutIncludesFourthHoliday, expectedRentalDays, checkoutDate, expectedDueDate,
                expectedChargeDays, expectedPreDiscountCharge, expectedDiscountPercent, expectedDiscountAmount,
                expectedFinalAmount, TOOL_LADW_LADDER, DAILY_CHARGE_LADDER_DTO);
    }

    private void testRentalAgreementAssertions(CheckoutDTO checkoutDTO, Integer expectedRentalDays, LocalDate checkoutDate,
                                               LocalDate expectedDueDate, Integer expectedChargeDays, BigDecimal expectedPreDiscountCharge,
                                               Integer expectedDiscountPercent, BigDecimal expectedDiscountAmount,
                                               BigDecimal expectedFinalAmount, ToolDTO toolDto, DailyChargeDTO dailyChargeDTO) {

        try {
            RentalAgreement agreement = checkoutService.generateRentalAgreement(checkoutDTO);
            Assertions.assertThat(agreement.getToolCode()).isEqualTo(toolDto.getCode());
            Assertions.assertThat(agreement.getToolType()).isEqualTo(toolDto.getType());
            Assertions.assertThat(agreement.getToolBrand()).isEqualTo(toolDto.getBrand());
            Assertions.assertThat(agreement.getTotalRentalDays()).isEqualTo(expectedRentalDays);
            Assertions.assertThat(agreement.getCheckoutDate()).isEqualTo(checkoutDate);
            Assertions.assertThat(agreement.getDueDate()).isEqualTo(expectedDueDate);
            Assertions.assertThat(agreement.getDailyRentalCharge()).isEqualTo(dailyChargeDTO.getPrice());
            Assertions.assertThat(agreement.getChargeDays()).isEqualTo(expectedChargeDays);
            Assertions.assertThat(agreement.getPreDiscountCharge()).isEqualTo(expectedPreDiscountCharge);
            Assertions.assertThat(agreement.getDiscountPercent()).isEqualTo(expectedDiscountPercent);
            Assertions.assertThat(agreement.getDiscountAmount()).isEqualTo(expectedDiscountAmount);
            Assertions.assertThat(agreement.getFinalCharge()).isEqualTo(expectedFinalAmount);
        }
        catch (Exception e) {
            Assertions.fail("No exceptions expected, but got one.", e);
        }
    }
}
