package com.college.factory;

import com.college.domain.Shift;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShiftFactory {
    public static Shift createShift(int shiftId, LocalDate shiftDay, LocalTime shiftStartTime, LocalTime shiftEndTime, boolean shiftOvertime) {
        return new Shift.Builder()
                .setShiftId(shiftId)
                .setShiftDay(shiftDay)
                .setShiftStartTime(shiftStartTime)
                .setShiftEndTime(shiftEndTime)
                .setShiftOvertime(shiftOvertime)
                .build();
    }


}