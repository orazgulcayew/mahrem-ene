package com.gocreative.tm.lukman.Models;

import java.io.Serializable;

public class User implements Serializable {
    private String profile_image, name, surname, f_name, passport, birthday, province,address, hospital, doctor, city, user_id;

    public User(){}

    public User(String name, String surname, String f_name, String passport, String birthday, String province, String address, String hospital, String doctor, String city) {
        this.name = name;
        this.surname = surname;
        this.f_name = f_name;
        this.passport = passport;
        this.birthday = birthday;
        this.province = province;
        this.address = address;
        this.hospital = hospital;
        this.doctor = doctor;
        this.city = city;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
