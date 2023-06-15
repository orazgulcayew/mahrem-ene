package com.gocreative.tm.mahrem_ene.Models;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Doctor implements Serializable {
    private String address, birthday, city, f_name, hospital, job, name, passport, phone_number, profile_image, province, surname, user_id;

    public Doctor(){}

    public Doctor(String address, String birthday, String city, String f_name, String hospital, String job, String name, String passport, String phone_number, String profile_image, String province, String surname, String user_id, Timestamp date_created) {
        this.address = address;
        this.birthday = birthday;
        this.city = city;
        this.f_name = f_name;
        this.hospital = hospital;
        this.job = job;
        this.name = name;
        this.passport = passport;
        this.phone_number = phone_number;
        this.profile_image = profile_image;
        this.province = province;
        this.surname = surname;
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
