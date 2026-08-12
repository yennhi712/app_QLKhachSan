package com.example.khanhsan_cnpm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SuaNhanVienDialogFragment extends DialogFragment {

    private EditText edtHoTen, edtNgaySinh, edtSoDT, edtEmail, edtCCCD, edtMatKhau;
    private Spinner spinnerGioiTinh;
    private RadioGroup radioGroupChucVu;
    private Button btnCapNhat;
    private NhanVien nhanVien;

    public static SuaNhanVienDialogFragment newInstance(NhanVien nhanVien) {
        SuaNhanVienDialogFragment fragment = new SuaNhanVienDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("nhanvien", nhanVien);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        nhanVien = (NhanVien) getArguments().getSerializable("nhanvien");

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_sua_nhanvien, null);

        // Ánh xạ view
        edtHoTen = view.findViewById(R.id.edtHoTen);
        edtNgaySinh = view.findViewById(R.id.edtNgaySinh);
        spinnerGioiTinh = view.findViewById(R.id.spinnerGioiTinh);
        edtSoDT = view.findViewById(R.id.edtSoDT);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtCCCD = view.findViewById(R.id.edtCCCD);
        edtMatKhau = view.findViewById(R.id.edtMatKhau);
        radioGroupChucVu = view.findViewById(R.id.radioGroupChucVu);
        btnCapNhat = view.findViewById(R.id.btnCapNhat);

        // Gán dữ liệu hiện tại
        edtHoTen.setText(nhanVien.getHoTen());
        edtNgaySinh.setText(nhanVien.getNgaySinh());
        edtSoDT.setText(nhanVien.getSoDT());
        edtEmail.setText(nhanVien.getEmail());
        edtCCCD.setText(nhanVien.getCccd());
        edtMatKhau.setText(nhanVien.getMatKhau());

        // Spinner giới tính
        String[] gioiTinhArr = getResources().getStringArray(R.array.gioitinh_array);
        int gioiTinhIndex = 0;
        for (int i = 0; i < gioiTinhArr.length; i++) {
            if (gioiTinhArr[i].equalsIgnoreCase(nhanVien.getGioiTinh())) {
                gioiTinhIndex = i;
                break;
            }
        }
        spinnerGioiTinh.setSelection(gioiTinhIndex);

        // Radio Chức vụ
        if ("Quản lý".equalsIgnoreCase(nhanVien.getChucVu())) {
            radioGroupChucVu.check(R.id.radioQuanLy);
        } else {
            radioGroupChucVu.check(R.id.radioNhanVien);
        }

        // Cập nhật dữ liệu khi nhấn nút
        btnCapNhat.setOnClickListener(v -> {
            String hoTen = edtHoTen.getText().toString();
            String ngaySinh = edtNgaySinh.getText().toString();
            String gioiTinh = spinnerGioiTinh.getSelectedItem().toString();
            String soDT = edtSoDT.getText().toString();
            String email = edtEmail.getText().toString();
            String cccd = edtCCCD.getText().toString();
            String matKhau = edtMatKhau.getText().toString();

            String chucVu = radioGroupChucVu.getCheckedRadioButtonId() == R.id.radioQuanLy
                    ? "Quản lý"
                    : "Nhân viên";

            // Cập nhật Firebase
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nhanvien").child(nhanVien.getMaNV());

            NhanVien nvMoi = new NhanVien(
                    nhanVien.getMaNV(),
                    hoTen, ngaySinh, gioiTinh,
                    soDT, email, cccd, matKhau, chucVu
            );

            ref.setValue(nvMoi);
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }
}
