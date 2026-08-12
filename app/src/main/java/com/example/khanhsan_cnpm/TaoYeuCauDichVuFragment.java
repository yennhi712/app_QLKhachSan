package com.example.khanhsan_cnpm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaoYeuCauDichVuFragment extends Fragment {

    private Spinner spinnerSoPhong, spnLoaiDichVu;
    private EditText edtHoTen, edtThoiGian, edtNhanVien, edtNoiDung;
    private TextView tvTongTien;
    private Button btnCapNhat;
    private RecyclerView recyclerView;

    private List<DichVu> danhSach;
    private DichVuAdapter adapter;
    private Map<String, String> phongToKhach = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taoyeucaudichvu, container, false);

        spinnerSoPhong = view.findViewById(R.id.spinnerSoPhong);
        edtHoTen = view.findViewById(R.id.edtHoTen);
        edtThoiGian = view.findViewById(R.id.edtThoiGian);
        edtNhanVien = view.findViewById(R.id.edtNhanVien);
        edtNoiDung = view.findViewById(R.id.edtNoiDung);
        spnLoaiDichVu = view.findViewById(R.id.spnLoaiDichVu);
        tvTongTien = view.findViewById(R.id.tvTongTien);
        btnCapNhat = view.findViewById(R.id.btnCapNhat);
        recyclerView = view.findViewById(R.id.recyclerViewYeuCau);

        setThoiGianNow();

        danhSach = getDanhSachDichVu();
        adapter = new DichVuAdapter(danhSach);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() { return false; }
        });
        recyclerView.setAdapter(adapter);

        loadPhongDaDat();

        spinnerSoPhong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String soPhong = spinnerSoPhong.getSelectedItem().toString();
                edtHoTen.setText(phongToKhach.getOrDefault(soPhong, ""));
                setThoiGianNow();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnCapNhat.setOnClickListener(v -> luuYeuCau());

        return view;
    }

    private void luuYeuCau() {
        String soPhong = spinnerSoPhong.getSelectedItem() != null ?
                spinnerSoPhong.getSelectedItem().toString() : "";
        String hoTen = edtHoTen.getText().toString().trim();
        String thoiGian = edtThoiGian.getText().toString().trim();
        String nhanVien = edtNhanVien.getText().toString().trim();
        String noiDung = edtNoiDung.getText().toString().trim();
        String loaiDichVu = spnLoaiDichVu.getSelectedItem() != null ?
                spnLoaiDichVu.getSelectedItem().toString() : "";

        if (soPhong.isEmpty() || hoTen.isEmpty()) {
            Toast.makeText(getContext(), "Chưa chọn phòng hoặc không có tên khách.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nhanVien.isEmpty() || noiDung.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập Nhân viên & Nội dung!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean coDichVu = false;
        String maYeuCau = UUID.randomUUID().toString();
        long tong = 0;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("yeu_cau_dich_vu");

        for (DichVu dv : danhSach) {
            if (dv.getSoLuong() > 0) {
                coDichVu = true;
                tong += dv.tinhTongTien();

                DichVu dvCopy = new DichVu(hoTen, soPhong, dv.getTen(), dv.getDonGia(), dv.getSoLuong());
                dvCopy.setMaYeuCau(maYeuCau);
                dvCopy.setThoiGian(thoiGian);
                dvCopy.setNhanVien(nhanVien);
                dvCopy.setNoiDung(noiDung);
                dvCopy.setLoaiDichVu(loaiDichVu);

                ref.child(maYeuCau + "_" + dv.getTen().replace(" ", "_"))
                        .setValue(dvCopy)
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }

        if (!coDichVu) {
            Toast.makeText(getContext(), "Bạn chưa chọn dịch vụ nào!", Toast.LENGTH_SHORT).show();
            return;
        }

        tvTongTien.setText("Tổng Tiền: " + tong + "VNĐ");
        Toast.makeText(getContext(), "Yêu cầu đã lưu!", Toast.LENGTH_SHORT).show();

        resetForm();
    }

    private void resetForm() {
        edtNhanVien.setText("");
        edtNoiDung.setText("");
        setThoiGianNow();
        for (DichVu dv : danhSach) dv.setSoLuong(0);
        adapter.notifyDataSetChanged();
    }

    private void setThoiGianNow() {
        edtThoiGian.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));
    }

    private void loadPhongDaDat() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("phong");
        phongToKhach.clear();
        ref.get().addOnSuccessListener(snapshot -> {
            List<String> phongList = new ArrayList<>();

            for (DataSnapshot loaiPhongSnap : snapshot.getChildren()) {
                for (DataSnapshot phongSnap : loaiPhongSnap.getChildren()) {
                    String tenPhong = phongSnap.child("tenPhong").getValue(String.class);
                    String maKhachHang = phongSnap.child("maKhachHang").getValue(String.class);
                    String trangThai = phongSnap.child("trangThai").getValue(String.class);

                    if ("Đã đặt".equalsIgnoreCase(trangThai) && tenPhong != null && maKhachHang != null) {
                        phongList.add(tenPhong);

                        DatabaseReference khachHangRef = FirebaseDatabase.getInstance().getReference("KhachHang").child(maKhachHang);
                        khachHangRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String hoTen = snapshot.child("hoTen").getValue(String.class);
                                if (hoTen != null) {
                                    phongToKhach.put(tenPhong, hoTen);

                                    if (spinnerSoPhong.getSelectedItem() != null &&
                                            spinnerSoPhong.getSelectedItem().toString().equals(tenPhong)) {
                                        edtHoTen.setText(hoTen);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item, phongList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSoPhong.setAdapter(adapter);

            if (!phongList.isEmpty()) {
                spinnerSoPhong.setSelection(0);
                String firstPhong = phongList.get(0);
                String hoTen = phongToKhach.get(firstPhong);
                edtHoTen.setText(hoTen != null ? hoTen : "");
            }
        }).addOnFailureListener(e ->
                Toast.makeText(getContext(), "Lỗi tải phòng: " + e.getMessage(), Toast.LENGTH_LONG).show()
        );
    }

    private List<DichVu> getDanhSachDichVu() {
        return Arrays.asList(
                new DichVu(1, "Ủi đồ", 30000),
                new DichVu(2, "Giặt khô", 30000),
                new DichVu(3, "Gym", 30000),
                new DichVu(4, "Dọn phòng hàng ngày", 40000),
                new DichVu(5, "Ăn sáng", 40000),
                new DichVu(6, "Spa", 100000),
                new DichVu(7, "Đưa đón sân bay", 250000),
                new DichVu(8, "Karaoke", 130000),
                new DichVu(9, "Chăm sóc thú cưng", 100000),
                new DichVu(10, "Thuê xe tự lái", 900000)
        );
    }
}
