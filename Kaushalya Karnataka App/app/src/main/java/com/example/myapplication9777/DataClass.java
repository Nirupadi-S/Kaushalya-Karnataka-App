package com.example.myapplication9777;

public class DataClass {

    private String workerId;
    private String workerName;
    private String department;
    private String salary;
    private String phoneNumber;
    private String shift;
    private String dataImage;
    private String key;

    public DataClass() {

    }

    public DataClass(String workerId, String workerName,
                     String department, String salary,
                     String phoneNumber, String shift,
                     String dataImage) {

        this.workerId = workerId;
        this.workerName = workerName;
        this.department = department;
        this.salary = salary;
        this.phoneNumber = phoneNumber;
        this.shift = shift;
        this.dataImage = dataImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getWorkerId() {
        return workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public String getDepartment() {
        return department;
    }

    public String getSalary() {
        return salary;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getShift() {
        return shift;
    }

    public String getDataImage() {
        return dataImage;
    }
}