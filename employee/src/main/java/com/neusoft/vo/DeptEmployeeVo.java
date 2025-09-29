package com.neusoft.vo;

public class DeptEmployeeVo {
    private Integer id;
    private String name;
    private String job;
    private Integer job_level;
    private String gender;
    private int age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getJob_level() {
        return job_level;
    }

    public void setJob_level(Integer job_level) {
        this.job_level = job_level;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
