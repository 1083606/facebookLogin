package com.example.facebooklogin;

import java.io.Serializable;

public class Remindertime implements Serializable {

    //public String cricketerName;
    //public String teamName;
    public String ReminderTime;
    public Remindertime() {

    }
    public Remindertime(String ReminderTime) {
        this.ReminderTime = ReminderTime;
    }
    public String getReminderTime() {
        return ReminderTime;
    }
    public void setReminderTime(String ReminderTime) {
        this.ReminderTime = ReminderTime;
    }

}

