package com.example.khanhsan_cnpm;

import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable; // ✅ Thêm import Serializable

@IgnoreExtraProperties
public class NhanVien implements Serializable { // ✅ Implement Serializable

    public String maNV;
    public String hoTen;
    public String ngaySinh;
    public String gioiTinh;
    public String soDT;
    public String email;
    public String cccd;
    public String matKhau;
    public String chucVu;  // ✅ Thêm trường chức vụ

    // Constructor mặc định (bắt buộc cho Firebase)
    public NhanVien() {
    }

    // Constructor đầy đủ có chức vụ
    public NhanVien(String maNV, String hoTen, String ngaySinh, String gioiTinh,
                    String soDT, String email, String cccd, String matKhau, String chucVu) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDT = soDT;
        this.email = email;
        this.cccd = cccd;
        this.matKhau = matKhau;
        this.chucVu = chucVu;
    }

    // Getters
    public String getMaNV() {
        return maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public String getSoDT() {
        return soDT;
    }

    public String getEmail() {
        return email;
    }

    public String getCccd() {
        return cccd;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public String getChucVu() {
        return chucVu;
    }

    // (Có thể thêm setter nếu cần chỉnh sửa sau này)
}
