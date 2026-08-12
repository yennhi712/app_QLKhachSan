package com.example.khanhsan_cnpm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ThemNhanVienFragment extends Fragment {

    private EditText edtMaNV, edtHoTen, edtNgaySinh, edtSoDT, edtEmail, edtCCCD, edtMatKhau;
    private Spinner spinnerGioiTinh;
    private RadioGroup radioGroupChucVu;
    private RadioButton radioNhanVien, radioQuanLy;
    private Button btnTaoTaiKhoan;

    private DatabaseReference nhanVienRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_themnhanvien, container, false);

        // Ánh xạ các view từ layout XML
        edtMaNV = view.findViewById(R.id.edtMaNV);
        edtHoTen = view.findViewById(R.id.edtHoTen);
        edtNgaySinh = view.findViewById(R.id.edtNgaySinh);
        edtSoDT = view.findViewById(R.id.edtSoDT);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtCCCD = view.findViewById(R.id.edtCCCD);
        edtMatKhau = view.findViewById(R.id.edtMatKhau);
        spinnerGioiTinh = view.findViewById(R.id.spinnerGioiTinh);
        btnTaoTaiKhoan = view.findViewById(R.id.btnTaoTaiKhoan);

        radioGroupChucVu = view.findViewById(R.id.radioGroupChucVu);
        radioNhanVien = view.findViewById(R.id.radioNhanVien);
        radioQuanLy = view.findViewById(R.id.radioQuanLy);

        // Gán sự kiện mở DatePicker khi bấm vào edtNgaySinh
        edtNgaySinh.setOnClickListener(v -> showDatePickerDialog());

        // Khởi tạo tham chiếu tới Firebase Realtime Database
        nhanVienRef = FirebaseDatabase.getInstance().getReference("nhanvien");

        // Bắt sự kiện nút tạo tài khoản
        btnTaoTaiKhoan.setOnClickListener(v -> themNhanVien());

        return view;
    }

    private void themNhanVien() {
        String maNV = edtMaNV.getText().toString().trim();
        String hoTen = edtHoTen.getText().toString().trim();
        String ngaySinh = edtNgaySinh.getText().toString().trim();
        String gioiTinh = spinnerGioiTinh.getSelectedItem().toString();
        String soDT = edtSoDT.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String cccd = edtCCCD.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();

        // Lấy chức vụ từ RadioGroup
        String chucVu = "";
        int selectedId = radioGroupChucVu.getCheckedRadioButtonId();
        if (selectedId == R.id.radioNhanVien) {
            chucVu = "Nhân viên";
        } else if (selectedId == R.id.radioQuanLy) {
            chucVu = "Quản lý";
        } else {
            Toast.makeText(getContext(), "Vui lòng chọn chức vụ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra các trường bắt buộc
        if (maNV.isEmpty() || hoTen.isEmpty() || matKhau.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ Mã NV, Họ tên và Mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng nhân viên
        NhanVien nv = new NhanVien(maNV, hoTen, ngaySinh, gioiTinh, soDT, email, cccd, matKhau, chucVu);

        // Đẩy dữ liệu lên Firebase
        nhanVienRef.child(maNV).setValue(nv)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Đã thêm nhân viên", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack(); // Quay lại màn hình trước
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi khi thêm: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    // Hiển thị ngày sinh theo định dạng dd/MM/yyyy
                    String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    edtNgaySinh.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Giới hạn tối đa là ngày hiện tại (không được chọn ngày tương lai)
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }
}
