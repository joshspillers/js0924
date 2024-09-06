package com.app.demo.toolrental.services.utilities;

public class YearDayRange {
    private final int dayOfYearStart;
    private final int dayOfYearEnd;
    private final int year;

    public YearDayRange(int dayOfYearStart, int dayOfYearEnd, int year) {
        this.dayOfYearStart = dayOfYearStart;
        this.dayOfYearEnd = dayOfYearEnd;
        this.year = year;
    }

    public int getDayOfYearStart() {
        return dayOfYearStart;
    }

    public int getDayOfYearEnd() {
        return dayOfYearEnd;
    }

    public int getYear() {
        return year;
    }
}
