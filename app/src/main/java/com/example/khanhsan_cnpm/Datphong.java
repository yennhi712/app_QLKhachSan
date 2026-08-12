package com.example.khanhsan_cnpm;

public class Datphong {
    private String tenPhong;
    private String loaiPhong;
    private String gia;
    private String trangThai;
    private String keyKhachHang;
    private String tenKhachHang;
    private String soDienThoaiKhachHang;
    private String cccdKhachHang;
    private String key;

    private String tenKhach;
    private String cccd;
    private String soDienThoai;
    private String ngayDat;
    private String ngayTra;
    private String maKhachHang;

    public Datphong() {}

    public Datphong(String tenPhong, String loaiPhong, String gia, String trangThai) {
        this.tenPhong = tenPhong;
        this.loaiPhong = loaiPhong;
        this.gia = gia;
        this.trangThai = trangThai;
    }

    public Datphong(String tenPhong, String loaiPhong, String gia, String trangThai,
                    String tenKhach, String cccd, String soDienThoai, String ngayDat, String ngayTra) {
        this.tenPhong = tenPhong;
        this.loaiPhong = loaiPhong;
        this.gia = gia;
        this.trangThai = trangThai;
        this.tenKhach = tenKhach;
        this.cccd = cccd;
        this.soDienThoai = soDienThoai;
        this.ngayDat = ngayDat;
        this.ngayTra = ngayTra;
    }

    // Getter và Setter cơ bản
    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(String loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getLoai() {
        return loaiPhong;
    }

    public String getTenKhach() {
        return tenKhach;
    }

    public void setTenKhach(String tenKhach) {
        this.tenKhach = tenKhach;
    }

    public String getKeyKhachHang() {
        return keyKhachHang;
    }

    public void setKeyKhachHang(String keyKhachHang) {
        this.keyKhachHang = keyKhachHang;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(String ngayTra) {
        this.ngayTra = ngayTra;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    // ✅ Hàm chuyển giá sang số
    public long getTienPhongAsLong() {
        if (gia == null) return 0;
        try {
            String digits = gia.replaceAll("[^\\d]", ""); // Bỏ tất cả ký tự không phải số
            return Long.parseLong(digits);
        } catch (Exception e) {
            return 0;
        }
    }
    public long getTienPhong() {
        try {
            String giaSo = gia.replace("đ", "").replace(".", "").replace(",", "").trim();
            return Long.parseLong(giaSo);
        } catch (Exception e) {
            return 0;
        }
    }

}
