package com.example.bm2025hafta4002;

public class ToDoItem {
    private int Id;
    private String Task;
    private int Status;

    public ToDoItem( int status, String task) {
        Status = status;
        Task = task;
    }

    public void setTask(String task) {
        Task = task;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getTask() {
        return Task;
    }

    public int getId() {
        return Id;
    }

    public int getStatus() {
        return Status;
    }

    @Override
    public String toString() {
        return  Task +" "+ (Status==1?" Tamamlandı":"Tamamlanmadı");
    }
}
