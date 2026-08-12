package com.example.khanhsan_cnpm;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DialogCapNhatKhachHangFragment extends DialogFragment {

    private EditText edtHoTen, edtSoDienThoai, edtCCCD;
    private Button btnLuu, btnHuy;
    private KhachHang khachHang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_capnhat_khachhang, container, false);

        edtHoTen = view.findViewById(R.id.edtHoTen);
        edtSoDienThoai = view.findViewById(R.id.edtSoDienThoai);
        edtCCCD = view.findViewById(R.id.edtCCCD);
        btnLuu = view.findViewById(R.id.btnLuu);
        btnHuy = view.findViewById(R.id.btnHuy);

        // Lấy dữ liệu khách hàng truyền vào
        if (getArguments() != null) {
            khachHang = (KhachHang) getArguments().getSerializable("khachHang");
            if (khachHang != null) {
                edtHoTen.setText(khachHang.hoTen);
                edtSoDienThoai.setText(khachHang.soDienThoai);
                edtCCCD.setText(khachHang.cccd);
            }
        }

        btnLuu.setOnClickListener(v -> {
            String hoTen = edtHoTen.getText().toString().trim();
            String sdt = edtSoDienThoai.getText().toString().trim();
            String cccd = edtCCCD.getText().toString().trim();

            if (TextUtils.isEmpty(hoTen) || TextUtils.isEmpty(sdt) || TextUtils.isEmpty(cccd)) {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference refKhach = FirebaseDatabase.getInstance().getReference("KhachHang");
            DatabaseReference refDatphong = FirebaseDatabase.getInstance().getReference("Datphong");

            if (khachHang != null && khachHang.key != null) {
                khachHang.hoTen = hoTen;
                khachHang.soDienThoai = sdt;
                khachHang.cccd = cccd;

                refKhach.child(khachHang.key).setValue(khachHang)
                        .addOnSuccessListener(unused -> {
                            // ✅ Sau khi cập nhật KhachHang, cập nhật các đặt phòng liên quan
                            refDatphong.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snap : snapshot.getChildren()) {
                                        Datphong dp = snap.getValue(Datphong.class);
                                        if (dp != null && dp.getKeyKhachHang().equals(khachHang.key)) {
                                            dp.setTenKhach(hoTen);
                                            dp.setSoDienThoai(sdt);
                                            dp.setCccd(cccd);
                                            dp.setKey(snap.getKey());
                                            refDatphong.child(snap.getKey()).setValue(dp);
                                        }
                                    }

                                    // ✅ Gọi lại loadDatphong() sau khi cập nhật xong
                                    if (getActivity() instanceof DatphongActivity) {
                                        ((DatphongActivity) getActivity()).loadDatphong();
                                    }

                                    dismiss(); // Đóng dialog
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), "Lỗi cập nhật thông tin đặt phòng", Toast.LENGTH_SHORT).show();
                                }
                            });

                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        btnHuy.setOnClickListener(v -> dismiss());

        return view;
    }
}

