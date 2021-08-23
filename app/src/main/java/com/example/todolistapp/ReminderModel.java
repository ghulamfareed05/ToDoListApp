package com.example.todolistapp;

import java.io.Serializable;

public class ReminderModel implements Serializable {
    private String notes;
    private String dueDate;
    private String doeTime;
    private String snoozeText;
    private String addList;


    private int id;

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setDoeTime(String doeTime) {
        this.doeTime = doeTime;
    }

    public void setSnoozeText(String snoozeText) {
        this.snoozeText = snoozeText;
    }

    public void setAddList(String addList) {
        this.addList = addList;
    }

    public String getNotes() {
        return notes;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getDoeTime() {
        return doeTime;
    }

    public String getSnoozeText() {
        return snoozeText;
    }

    public String getAddList() {
        return addList;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public ReminderModel(String notes, String dueDate, String doeTime, String snoozeText, String addList,int id) {
        this.notes = notes;
        this.dueDate = dueDate;
        this.doeTime = doeTime;
        this.snoozeText = snoozeText;
        this.addList = addList;
        this.id = id;
    }





}
