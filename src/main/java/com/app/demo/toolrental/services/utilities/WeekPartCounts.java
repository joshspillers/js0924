package com.app.demo.toolrental.services.utilities;

public class WeekPartCounts {
    private final int weekdayCount;
    private final int weekendDayCount;

    public WeekPartCounts(int weekdayCount, int weekendDayCount) {
        this.weekdayCount = weekdayCount;
        this.weekendDayCount = weekendDayCount;
    }

    public int getWeekdayCount() {
        return weekdayCount;
    }

    public int getWeekendDayCount() {
        return weekendDayCount;
    }
}
