package com.example.gproqmsnhanxe;

import java.io.Serializable;

public class DichVu implements Serializable {
    private int Id ;
    private String Name ;
    private String Code ;
    public DichVu(int _id, String _name, String _code ){
        Id = _id;
        Name = _name;
        Code = _code;
    }
    public int getId() {
        return Id;
    }
    public void setId(int _id) {
        Id = _id;
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
    @Override
    public String toString() {
        return Name;
    }
}
