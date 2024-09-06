package com.app.demo.toolrental.services.utilities;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

public class HolidayChecker {
    private final boolean hasFourthOfJulyHoliday;
    private final boolean isFourthOfJulyWeekday;
    private final boolean hasLaborDayHoliday;

    public HolidayChecker(boolean hasFourthOfJulyHoliday, boolean isFourthOfJulyWeekday, boolean hasLaborDayHoliday) {
        this.hasFourthOfJulyHoliday = hasFourthOfJulyHoliday;
        this.isFourthOfJulyWeekday = isFourthOfJulyWeekday;
        this.hasLaborDayHoliday = hasLaborDayHoliday;
    }

    public static HolidayCheckerBuilder builder() {
        return new HolidayCheckerBuilder();
    }

    public boolean isHasFourthOfJulyHoliday() {
        return hasFourthOfJulyHoliday;
    }

    public boolean isFourthOfJulyWeekday() {
        return isFourthOfJulyWeekday;
    }

    public boolean isHasLaborDayHoliday() {
        return hasLaborDayHoliday;
    }

    public static boolean hasFourthOfJulyInDayRange(int rangeStart, int rangeEnd, int year) {
        int fourthOfJulyDoyForYear = LocalDate.of(year, Month.JULY, 4).getDayOfYear();
        return rangeStart <= fourthOfJulyDoyForYear && rangeEnd >= fourthOfJulyDoyForYear;
    }

    public static boolean isFourthOfJulyOnWeekday(int year) {
        DayOfWeek dayOfWeek = LocalDate.of(year, Month.JULY, 4).getDayOfWeek();
        return dayOfWeek.getValue() < 6;
    }

    public static boolean hasLaborDayInDayRange(int rangeStart, int rangeEnd, int year) {
        int laborDayDoyForYear = LocalDate.of(year, Month.SEPTEMBER, 1).with(TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.MONDAY)).getDayOfYear();
        return rangeStart <= laborDayDoyForYear && rangeEnd >= laborDayDoyForYear;
    }


    public static final class HolidayCheckerBuilder {
        private boolean hasFourthOfJulyHoliday;
        private boolean isFourthOfJulyWeekday;
        private boolean hasLaborDayHoliday;

        private HolidayCheckerBuilder() {
        }

        public HolidayCheckerBuilder hasFourthOfJulyHoliday(boolean hasFourthOfJulyHoliday) {
            this.hasFourthOfJulyHoliday = hasFourthOfJulyHoliday;
            return this;
        }

        public HolidayCheckerBuilder isFourthOfJulyWeekday(boolean isFourthOfJulyWeekday) {
            this.isFourthOfJulyWeekday = isFourthOfJulyWeekday;
            return this;
        }

        public HolidayCheckerBuilder hasLaborDayHoliday(boolean hasLaborDayHoliday) {
            this.hasLaborDayHoliday = hasLaborDayHoliday;
            return this;
        }

        public HolidayChecker build() {
            return new HolidayChecker(hasFourthOfJulyHoliday, isFourthOfJulyWeekday, hasLaborDayHoliday);
        }
    }
}
