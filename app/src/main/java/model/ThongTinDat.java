package model;

public class ThongTinDat {
        private String ten;
        private String sdt;
        private String cccd;
        private String ngayDat;
        private String ngayTra;

        public ThongTinDat() {
            // Required for Firebase
        }

        public ThongTinDat(String ten, String sdt, String cccd, String ngayDat, String ngayTra) {
            this.ten = ten;
            this.sdt = sdt;
            this.cccd = cccd;
            this.ngayDat = ngayDat;
            this.ngayTra = ngayTra;
        }

        public String getTen() {
            return ten;
        }

        public void setTen(String ten) {
            this.ten = ten;
        }

        public String getSdt() {
            return sdt;
        }

        public void setSdt(String sdt) {
            this.sdt = sdt;
        }

        public String getCccd() {
            return cccd;
        }

        public void setCccd(String cccd) {
            this.cccd = cccd;
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
    }

