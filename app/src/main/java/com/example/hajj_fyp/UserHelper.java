package com.example.hajj_fyp;

public class UserHelper {

    String email, name, password, age;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public UserHelper() {
    }

    public UserHelper(String email, String name, String password, String age) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.age = age;
    }
}