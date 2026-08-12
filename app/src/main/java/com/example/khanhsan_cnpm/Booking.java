package com.example.khanhsan_cnpm;

public class Booking {
    private String tenKhach;
    private String soPhong;
    private String ngayDat;
    private String trangThai;

    public Booking() {}

    public Booking(String tenKhach, String soPhong, String ngayDat, String trangThai) {
        this.tenKhach = tenKhach;
        this.soPhong = soPhong;
        this.ngayDat = ngayDat;
        this.trangThai = trangThai;
    }

    public String getTenKhach() { return tenKhach; }
    public void setTenKhach(String tenKhach) { this.tenKhach = tenKhach; }

    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }

    public String getNgayDat() { return ngayDat; }
    public void setNgayDat(String ngayDat) { this.ngayDat = ngayDat; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
