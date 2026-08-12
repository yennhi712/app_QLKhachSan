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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;


public class DichvuFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button btnTaoYeuCauDichVu, btnTim;
    private EditText edtTimSoPhong;
    private Spinner spnLocDichVu;

    private List<DichVu> danhSach = new ArrayList<>();
    private DichVuDaDatAdapter adapter;
    private DatabaseReference dbRef;

    private View layoutMain;
    private View fragmentContainer;

    private final List<DichVu> danhSachGoc = new ArrayList<>(); // lưu bản gốc để lọc

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dichvu, container, false);

        // Ánh xạ view
        recyclerView = view.findViewById(R.id.recyclerViewDichVuDaDat);
        btnTaoYeuCauDichVu = view.findViewById(R.id.btnTaoYeuCauDichVu);
        btnTim = view.findViewById(R.id.btnTim);
        edtTimSoPhong = view.findViewById(R.id.edtTimSoPhong);
        spnLocDichVu = view.findViewById(R.id.spnLocDichVu);
        layoutMain = view.findViewById(R.id.layoutMainDichVu);
        fragmentContainer = view.findViewById(R.id.dichvu_fragment_container);

        // Spinner: danh sách loại dịch vụ
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Tất cả", "Ủi đồ", "Giặt khô", "Fitness center / Gym", "Dọn phòng hàng ngày", "Ăn sáng",
                        "Spa", "Đưa đón sân bay", "Karaoke", "Chăm sóc thú cưng", "Thuê xe tự lái"}
        );
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLocDichVu.setAdapter(adapterSpinner);

        // Adapter danh sách
        adapter = new DichVuDaDatAdapter(getContext(), danhSach);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("yeu_cau_dich_vu");
        loadDanhSachDaDat();

        // Tạo yêu cầu
        btnTaoYeuCauDichVu.setOnClickListener(v -> {
            layoutMain.setVisibility(View.GONE);
            fragmentContainer.setVisibility(View.VISIBLE);

            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.dichvu_fragment_container, new TaoYeuCauDichVuFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Bấm vào EditText chọn phòng
        edtTimSoPhong.setFocusable(false);
        edtTimSoPhong.setClickable(true);
        edtTimSoPhong.setOnClickListener(v -> hienThiDanhSachPhong());

        // Tìm kiếm / lọc
        btnTim.setOnClickListener(v -> {
            try {
                String soPhong = edtTimSoPhong.getText().toString().trim();
                String loaiDichVu = spnLocDichVu.getSelectedItem().toString();

                List<DichVu> loc = new ArrayList<>();
                for (DichVu dv : danhSachGoc) {
                    boolean matchPhong = soPhong.isEmpty() || (dv.getSoPhong() != null && dv.getSoPhong().equalsIgnoreCase(soPhong));
                    boolean matchLoai = loaiDichVu.equals("Tất cả") || (dv.getTen() != null && dv.getTen().equalsIgnoreCase(loaiDichVu));

                    if (matchPhong && matchLoai) {
                        loc.add(dv);
                    }
                }

                adapter.capNhatDanhSach(loc);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Lỗi tìm kiếm: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });

        return view;
    }

    private void loadDanhSachDaDat() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhSach.clear();
                danhSachGoc.clear();
                int stt = 1;

                for (DataSnapshot data : snapshot.getChildren()) {
                    DichVu dv = data.getValue(DichVu.class);
                    if (dv != null) {
                        dv.setThutu(stt++);
                        danhSach.add(dv);
                        danhSachGoc.add(dv);
                    }
                }

                adapter.capNhatDanhSach(danhSach);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hienThiDanhSachPhong() {
        dbRef.get().addOnSuccessListener(snapshot -> {
            Set<String> phongSet = new HashSet<>();
            for (DataSnapshot data : snapshot.getChildren()) {
                String soPhong = data.child("soPhong").getValue(String.class);
                if (soPhong != null) {
                    phongSet.add(soPhong);
                }
            }

            if (phongSet.isEmpty()) {
                Toast.makeText(getContext(), "Không có phòng nào đã đặt dịch vụ", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] phongArray = phongSet.toArray(new String[0]);
            new AlertDialog.Builder(getContext())
                    .setTitle("Chọn phòng đã đặt dịch vụ")
                    .setItems(phongArray, (dialog, which) -> edtTimSoPhong.setText(phongArray[which]))
                    .show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Lỗi tải danh sách phòng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
