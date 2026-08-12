package com.example.khanhsan_cnpm;

public class DichVu {
    private int thutu;
    private String ten;
    private int donGia;
    private int soLuong;

    private String maYeuCau;
    private String hoTen;
    private String soPhong;

    private String thoiGian;
    private String nhanVien;
    private String noiDung;
    private String loaiDichVu;

    public DichVu() {}

    // Constructor khi hiển thị danh sách
    public DichVu(int thutu, String ten, int donGia) {
        this.thutu = thutu;
        this.ten = ten;
        this.donGia = donGia;
        this.soLuong = 0;
    }

    // Constructor khi lưu Firebase
    public DichVu(String hoTen, String soPhong, String ten, int donGia, int soLuong) {
        this.hoTen = hoTen;
        this.soPhong = soPhong;
        this.ten = ten;
        this.donGia = donGia;
        this.soLuong = soLuong;
    }

    public int getThutu() {
        return thutu;
    }

    public void setThutu(int thutu) {
        this.thutu = thutu;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getMaYeuCau() {
        return maYeuCau;
    }

    public void setMaYeuCau(String maYeuCau) {
        this.maYeuCau = maYeuCau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(String nhanVien) {
        this.nhanVien = nhanVien;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getLoaiDichVu() {
        return loaiDichVu;
    }

    public void setLoaiDichVu(String loaiDichVu) {
        this.loaiDichVu = loaiDichVu;
    }

    public int tinhTongTien() {
        return donGia * soLuong;
    }
}
