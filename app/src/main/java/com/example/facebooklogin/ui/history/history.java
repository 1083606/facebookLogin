package com.example.facebooklogin.ui.history;

public class history {
    private String HabitName;
    private String ContiDay;
    private String AccumDay;
    private String MaxContiDay;

    public history() {
    }

    public history(String habitName, String contiDay, String accumDay, String maxContiDay) {
        HabitName = habitName;
        ContiDay = contiDay;
        AccumDay = accumDay;
        MaxContiDay = maxContiDay;
    }

    //getter
    public String getHabitName() {
        return HabitName;
    }

    public String getContiDay() {
        return ContiDay;
    }

    public String getAccumDay() {
        return AccumDay;
    }

    public String getMaxContiDay() { return MaxContiDay; }

    //setter


    public void setHabitName(String habitName) {
        HabitName = habitName;
    }

    public void setContiDay(String contiDay) {
        ContiDay = contiDay;
    }

    public void setAccumDay(String accumDay) {
        AccumDay = accumDay;
    }

    public void setMaxContiDay(String maxContiDay) {
        MaxContiDay = maxContiDay;
    }
}
