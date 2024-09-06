package com.app.demo.toolrental.services.utilities;

import com.app.demo.toolrental.transport.CheckoutDTO;
import com.app.demo.toolrental.transport.DailyChargeDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

public class DateUtils {

    //The approach I took here was to implement a way to find 'week parts' (ie: weekdays and weekend days) and holidays
    //without 'brute forcing' every day in the rental period into a LocalDate object. Since we know the start day of the
    //rental period, we know the total days of rental, and we know those dates are consecutive, we should be able to
    //calculate the needed data by doing some somewhat-simple math to find the needed info. This should be more efficient
    //than the brute force approach since we won't be creating n-LocalDate objects. I also accounted for crossing an
    //arbitrary number of years in our rental period.
    public static int getChargeDays(CheckoutDTO checkoutDTO, DailyChargeDTO chargeDTO) {
        List<YearDayRange> yearDateRangeList = divideRentalPeriodByCalendarYear(checkoutDTO);
        int totalChargeDays = 0;
        for (YearDayRange ydr : yearDateRangeList) {
            HolidayChecker holidayChecker = checkHolidays(ydr, chargeDTO.getChargeOnHoliday());

            int periodTotal = ydr.getDayOfYearEnd()-ydr.getDayOfYearStart()+1;
            WeekPartCounts weekPartCounts = getNumberOfWeekdaysAndWeekendDays(LocalDate.ofYearDay(ydr.getYear(), ydr.getDayOfYearStart()).getDayOfWeek(), periodTotal);

            if (chargeDTO.getChargeOnWeekend() && weekPartCounts.getWeekendDayCount() > 0) {
                totalChargeDays += weekPartCounts.getWeekendDayCount();
                if (!chargeDTO.getChargeOnHoliday() && holidayChecker.isHasFourthOfJulyHoliday() && !holidayChecker.isFourthOfJulyWeekday()) {
                    totalChargeDays--;
                }
            }

            if (chargeDTO.getChargeOnWeekdays() && weekPartCounts.getWeekdayCount() > 0) {
                totalChargeDays += weekPartCounts.getWeekdayCount();

                if (!chargeDTO.getChargeOnHoliday()) {
                    if (holidayChecker.isHasLaborDayHoliday()) {
                        totalChargeDays--;
                    }
                    if (holidayChecker.isHasFourthOfJulyHoliday() && holidayChecker.isFourthOfJulyWeekday()) {
                        totalChargeDays--;
                    }
                }
            }
        }

        return totalChargeDays;
    }

    public static HolidayChecker checkHolidays(YearDayRange ydr, boolean chargeOnHoliday) {
        HolidayChecker.HolidayCheckerBuilder holidayCheckerBuilder = HolidayChecker.builder();
        if (!chargeOnHoliday) {
            holidayCheckerBuilder.hasFourthOfJulyHoliday(HolidayChecker.hasFourthOfJulyInDayRange(ydr.getDayOfYearStart(),
                    ydr.getDayOfYearEnd(), ydr.getYear()));
            holidayCheckerBuilder.isFourthOfJulyWeekday(HolidayChecker.isFourthOfJulyOnWeekday(ydr.getYear()));
            holidayCheckerBuilder.hasLaborDayHoliday(HolidayChecker.hasLaborDayInDayRange(ydr.getDayOfYearStart(), ydr.getDayOfYearEnd(), ydr.getYear()));
        }
        return holidayCheckerBuilder.build();
    }

    //If the rental period crosses years, divide the needed data to calculate charge days into discrete years so that we
    //can calculate the correct holidays and leap year info per year.
    private static List<YearDayRange> divideRentalPeriodByCalendarYear(CheckoutDTO checkoutDTO) {
        LocalDate checkoutDtPlusOne = checkoutDTO.getCheckoutDate().plusDays(1);
        LocalDate checkoutDtPlusRentalDays = checkoutDTO.getCheckoutDate().plusDays(checkoutDTO.getRentalDayCount());

        List<YearDayRange> yearDateRangeList = new ArrayList<>();
        if (checkoutDtPlusOne.getYear() != checkoutDtPlusRentalDays.getYear()) {
            yearDateRangeList.add(new YearDayRange(checkoutDtPlusOne.getDayOfYear(),
                    checkoutDtPlusOne.with(lastDayOfYear()).getDayOfYear(), checkoutDtPlusOne.getYear()));
            int remainingDays = checkoutDTO.getRentalDayCount() -
                    (checkoutDtPlusOne.with(lastDayOfYear()).getDayOfYear() - checkoutDtPlusOne.getDayOfYear()+1);
            int year = checkoutDtPlusOne.getYear();
            while (remainingDays > 0) {
                year++;
                boolean isLeapYear = Year.isLeap(year);
                if (isLeapYear && remainingDays >= 366) {
                    yearDateRangeList.add(new YearDayRange(1, 366, year));
                    remainingDays -= 366;
                }
                else if (remainingDays >= 365) {
                    yearDateRangeList.add(new YearDayRange(1,365, year));
                    remainingDays -= 365;
                }
                else {
                    yearDateRangeList.add(new YearDayRange(1, remainingDays, year));
                    break;
                }
            }
        }
        else {
            yearDateRangeList.add(new YearDayRange(checkoutDtPlusOne.getDayOfYear(),
                    checkoutDtPlusRentalDays.getDayOfYear(), checkoutDtPlusOne.getYear()));
        }

        return yearDateRangeList;
    }

    private static WeekPartCounts getNumberOfWeekdaysAndWeekendDays(DayOfWeek startDayOfWeek, int totalDays) {
        int weekdayCount = 0;
        int weekendDayCount = 0;
        int daysLeftToCalc = totalDays;
        int offset = startDayOfWeek.getValue()-DayOfWeek.MONDAY.getValue();

        if (5-offset > 0) {
            weekdayCount += Math.min(5 - offset, totalDays);
            daysLeftToCalc -= weekdayCount;
        }

        if (daysLeftToCalc > 0) {
            weekendDayCount += Math.min(2, daysLeftToCalc);
            daysLeftToCalc -= weekendDayCount;
        }

        int weeks = daysLeftToCalc / 7;
        int remainder = daysLeftToCalc % 7;
        weekdayCount += weeks * 5;
        weekendDayCount += weeks * 2;
        if (remainder != 0) {
            if (remainder < 6) {
                weekdayCount += remainder;
            }
            else {
                weekdayCount += 5;
                weekendDayCount++;
            }
        }

        return new WeekPartCounts(weekdayCount, weekendDayCount);
    }
}
