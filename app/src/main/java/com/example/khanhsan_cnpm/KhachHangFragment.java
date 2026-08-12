package com.example.khanhsan_cnpm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class KhachHangFragment extends Fragment {

    private EditText edtSearchHoTen, edtSearchSDT, edtSearchCCCD;
    private Button btnTimKiem;
    private RecyclerView recyclerKhachHang;

    private List<KhachHang> danhSach = new ArrayList<>();
    private KhachHangAdapter adapter;
    private DatabaseReference khachHangRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_khachhang, container, false);

        edtSearchHoTen = view.findViewById(R.id.edtSearchHoTen);
        edtSearchSDT = view.findViewById(R.id.edtSearchSDT);
        edtSearchCCCD = view.findViewById(R.id.edtSearchCCCD);
        btnTimKiem = view.findViewById(R.id.btnTimKiem);
        recyclerKhachHang = view.findViewById(R.id.recyclerKhachHang);

        khachHangRef = FirebaseDatabase.getInstance().getReference("KhachHang");

        adapter = new KhachHangAdapter(danhSach);
        recyclerKhachHang.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerKhachHang.setAdapter(adapter);

        // Sự kiện sửa & xoá
        adapter.setOnItemActionListener(new KhachHangAdapter.OnItemActionListener() {
            @Override
            public void onEdit(KhachHang khachHang) {
                DialogCapNhatKhachHangFragment fragment = new DialogCapNhatKhachHangFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("khachHang", khachHang);
                fragment.setArguments(bundle);
                fragment.show(getParentFragmentManager(), "CapNhatKhachHang");
            }

            @Override
            public void onDelete(KhachHang khachHang) {
                if (khachHang.key == null || khachHang.key.isEmpty()) {
                    Toast.makeText(getContext(), "Không có ID khách hàng để xoá", Toast.LENGTH_SHORT).show();
                    return;
                }

                khachHangRef.child(khachHang.key).removeValue()
                        .addOnSuccessListener(unused -> Toast.makeText(getContext(), "Xoá thành công", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Xoá thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        // Nhận kết quả từ Dialog cập nhật
        getParentFragmentManager().setFragmentResultListener("capnhat_khachhang", this, (key, bundle) -> {
            boolean capNhatThanhCong = bundle.getBoolean("capnhat_thanhcong", false);
            if (capNhatThanhCong) {
                Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                hideKeyboard();
            }
        });

        btnTimKiem.setOnClickListener(v -> {
            hideKeyboard();
            searchKhachHang();
        });

        // Lắng nghe dữ liệu realtime từ Firebase
        khachHangRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhSach.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    KhachHang kh = child.getValue(KhachHang.class);
                    if (kh != null) {
                        kh.key = child.getKey();
                        danhSach.add(kh);
                    }
                }
                adapter.updateData(danhSach);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu khách hàng", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void searchKhachHang() {
        String hoTen = edtSearchHoTen.getText().toString().trim().toLowerCase();
        String sdt = edtSearchSDT.getText().toString().trim();
        String cccd = edtSearchCCCD.getText().toString().trim();

        List<KhachHang> ketQua = new ArrayList<>();
        for (KhachHang kh : danhSach) {
            boolean matchHoTen = hoTen.isEmpty() || (kh.hoTen != null && kh.hoTen.toLowerCase().contains(hoTen));
            boolean matchSDT = sdt.isEmpty() || (kh.soDienThoai != null && kh.soDienThoai.contains(sdt));
            boolean matchCCCD = cccd.isEmpty() || (kh.cccd != null && kh.cccd.contains(cccd));

            if (matchHoTen && matchSDT && matchCCCD) {
                ketQua.add(kh);
            }
        }

        if (ketQua.isEmpty()) {
            Toast.makeText(getContext(), "Không tìm thấy khách hàng nào phù hợp", Toast.LENGTH_SHORT).show();
        }

        adapter.updateData(ketQua);
    }

    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
