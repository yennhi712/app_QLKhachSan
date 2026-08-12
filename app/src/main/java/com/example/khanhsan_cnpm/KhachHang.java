package com.example.khanhsan_cnpm;

import java.io.Serializable;

public class KhachHang implements Serializable {

    public String key;

    public String hoTen;
    public String soDienThoai;
    public String cccd;

    // Constructor rỗng – Required for Firebase
    public KhachHang() {
    }

    // Constructor đầy đủ khi khởi tạo khách hàng mới
    public KhachHang(String key, String hoTen, String soDienThoai, String cccd) {
        this.key = key;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.cccd = cccd;
    }

    // Getter & Setter (nếu cần)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }
}
