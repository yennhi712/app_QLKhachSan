package com.example.khanhsan_cnpm;

import java.io.Serializable;

public class Sodophong implements Serializable {
    private String tenPhong;
    private String loaiPhong;
    private String trangThai;

    public Sodophong(String tenPhong, String loaiPhong, String trangThai) {
        this.tenPhong = tenPhong;
        this.loaiPhong = loaiPhong;
        this.trangThai = trangThai;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    public String getTrangThai() {
        return trangThai;
    }
}
