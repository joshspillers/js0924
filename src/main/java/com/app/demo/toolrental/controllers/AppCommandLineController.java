package com.app.demo.toolrental.controllers;

import com.app.demo.toolrental.services.checkout.CheckoutService;
import com.app.demo.toolrental.services.validation.ValidationException;
import com.app.demo.toolrental.services.validation.ValidationResult;
import com.app.demo.toolrental.services.validation.ValidatorService;
import com.app.demo.toolrental.transport.CheckoutDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.function.Function;

@Controller
@Order()
public class AppCommandLineController implements CommandLineRunner {
    private final ValidatorService validatorService;
    private final CheckoutService checkoutService;

    @Autowired
    public AppCommandLineController(ValidatorService validatorService, CheckoutService checkoutService) {
        this.validatorService = validatorService;
        this.checkoutService =  checkoutService;
    }

    @Override
    public void run(String... args) throws Exception {
        while(true) {
            System.out.println(PromptMessages.EXIT_ANY_TIME.prompt());
            Scanner sc = new Scanner(System.in);
            CheckoutDTO.CheckoutDTOBuilder checkoutBuilder = CheckoutDTO.builder();

            //Get and validate tool code
            String toolCode = promptAndGetAnswer(PromptMessages.ENTER_TOOL_CODE.prompt(), sc, validatorService::isConsoleToolCodeValid);
            checkoutBuilder = checkoutBuilder.toolCode(toolCode);

            //Get and validate rental day count
            String rentalDayCount = promptAndGetAnswer(PromptMessages.ENTER_RENTAL_DAYS.prompt(), sc, validatorService::isConsoleRentalDayCountValid);
            checkoutBuilder = checkoutBuilder.rentalDayCount(Integer.valueOf(rentalDayCount));

            //Get and validate discount percent
            String discount = promptAndGetAnswer(PromptMessages.ENTER_DISCOUNT.prompt(), sc, validatorService::isConsoleDiscountPercentValid);
            checkoutBuilder = checkoutBuilder.discountPercent(Integer.valueOf(discount));

            //Get and validate checkoutDate
            String checkoutDate = promptAndGetAnswer(PromptMessages.ENTER_CHECKOUT_DATE.prompt(), sc, validatorService::isConsoleCheckoutDateValid);
            LocalDate checkoutLocalDate = LocalDate.parse(checkoutDate, DateTimeFormatter.ofPattern("M/d/yy"));
            checkoutBuilder = checkoutBuilder.checkoutDate(checkoutLocalDate);

            try {
                System.out.println("\n\n******Rental Agreement******\n\n"+checkoutService.generateRentalAgreement(checkoutBuilder.build()).toConsoleString());
            }
            catch (ValidationException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("\n****************************\n");
        }
    }

    private String promptAndGetAnswer(String prompt, Scanner sc, Function<String,ValidationResult> validate) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            ValidationResult validationResult = validate.apply(input);
            ifExitDisplayMessageAndExitApplication(validationResult);
            if (validationResult.isValid()) {
                return input;
            }
            else {
                System.out.println(validationResult.getValidationMessage());
            }
        }
    }

    private void ifExitDisplayMessageAndExitApplication(ValidationResult r) {
        if (r.isExitCode()) {
            System.out.println(r.getValidationMessage());
            System.exit(0);
        }
    }

    private enum PromptMessages {
        ENTER_TOOL_CODE("Enter Tool Code: "),
        ENTER_RENTAL_DAYS("Enter Number of Rental Days: "),
        ENTER_DISCOUNT("Enter Discount Percentage: "),
        ENTER_CHECKOUT_DATE("Enter Checkout Date: "),
        EXIT_ANY_TIME("Type exit at any time to end program.");

        private final String prompt;

        PromptMessages(String prompt) {
            this.prompt=prompt;
        }

        public String prompt(){
            return this.prompt;
        }
    }
}
