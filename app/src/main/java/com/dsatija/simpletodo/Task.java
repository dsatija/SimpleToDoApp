package com.dsatija.simpletodo;

/**
 * Created by disha_000 on 9/22/2016.
 */
public class Task {
    public String taskName;
    private int id;
    private int status;
    public int year;
    public int month;
    public int day;
    private String priority;

    public Task(String taskName, int status, int year, int month, int day, String priority) {
        this.taskName = taskName;
        this.status = status;
        this.year = year;
        this.month = month;
        this.day = day;
        this.priority = priority;

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.taskName.toString();
    }
}

