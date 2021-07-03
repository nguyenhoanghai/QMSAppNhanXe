package com.example.gproqmsnhanxe;

import java.io.Serializable;

public class KhachHang implements Serializable {
    private String Name ;
    private String Code ;
    private String Phone ;
    private String Address ;
    private String LicensePlate ;
    private int Times ;

    public KhachHang(String _name, String _code,String _phone, String _address,String _licensePlate, int _times  ){
        Name = _name;
        Code = _code;
        Phone = _phone;
        Address = _address;
        LicensePlate = _licensePlate;
        Times = _times;
    }
    public String getCode() {
        return Code;
    }
    public void setCode(String _code) {
        Code = _code;
    }

    public String getName() {
        return Name;
    }
    public void setName(String _name) {
        Name = _name;
    }

    public String getPhone() {
        return Phone;
    }
    public void setPhone(String _phone) {
        Phone = _phone;
    }

    public String getAddress() {
        return Address;
    }
    public void setAddress(String _address) {
        Address = _address;
    }

    public String getLicensePlate() {
        return LicensePlate;
    }
    public void setLicensePlate(String _licensePlate) {
        LicensePlate = _licensePlate;
    }

    public int getTimes() {
        return Times;
    }
    public void setTimes(int _times) {
        Times = _times;
    }

    @Override
    public String toString() {
        return Name;
    }
}
