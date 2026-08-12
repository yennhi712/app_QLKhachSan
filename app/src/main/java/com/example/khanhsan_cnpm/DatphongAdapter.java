package com.example.khanhsan_cnpm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DatphongAdapter extends RecyclerView.Adapter<DatphongAdapter.ViewHolder> {

    private Context context;
    private List<Datphong> danhSach;
    private String ngayDat;
    private String ngayTra;

    public DatphongAdapter(Context context, List<Datphong> danhSach) {
        this.context = context;
        this.danhSach = danhSach;
    }

    public void setDanhSachPhong(List<Datphong> danhSachMoi) {
        this.danhSach = danhSachMoi;
        notifyDataSetChanged();
    }

    public void setNgayDatVaHenTra(String ngayDat, String ngayTra) {
        this.ngayDat = ngayDat;
        this.ngayTra = ngayTra;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_datphong, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Datphong phong = danhSach.get(position);

        holder.tvTenPhong.setText(phong.getTenPhong());
        holder.tvLoaiPhong.setText(phong.getLoaiPhong());
        holder.tvGia.setText(phong.getGia());
        holder.tvTrangThai.setText(phong.getTrangThai());

        if (phong.getTrangThai().equalsIgnoreCase("Trống")) {
            holder.tvTrangThai.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
        } else if (phong.getTrangThai().equalsIgnoreCase("Đã đặt")) {
            holder.tvTrangThai.setTextColor(android.graphics.Color.parseColor("#F44336"));
        } else {
            holder.tvTrangThai.setTextColor(android.graphics.Color.DKGRAY);
        }

        holder.btnDat.setVisibility(phong.getTrangThai().equals("Trống") ? View.VISIBLE : View.GONE);
        holder.btnTraPhong.setVisibility(phong.getTrangThai().equals("Đã đặt") ? View.VISIBLE : View.GONE);

        holder.btnDat.setOnClickListener(v -> showDialogDatPhong(holder, phong));
        holder.btnTraPhong.setOnClickListener(v -> showDialogTraPhong(holder, phong));
    }

    private void showDialogDatPhong(ViewHolder holder, Datphong phong) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_datphong, null);
        builder.setView(dialogView);

        EditText edtTenKhach = dialogView.findViewById(R.id.edtTenKhach);
        EditText edtSDT = dialogView.findViewById(R.id.edtSDT);
        EditText edtCCCD = dialogView.findViewById(R.id.edtCCCD);
        TextView txtNgayDat = dialogView.findViewById(R.id.txtNgayDat);
        TextView txtNgayTra = dialogView.findViewById(R.id.txtNgayTra);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        txtNgayDat.setText(sdf.format(new Date()));

        txtNgayTra.setOnClickListener(v -> showDatePicker(txtNgayTra));

        builder.setTitle("Nhập thông tin đặt phòng");

        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String tenKhach = edtTenKhach.getText().toString().trim();
            String sdt = edtSDT.getText().toString().trim();
            String cccd = edtCCCD.getText().toString().trim();
            String ngayDat = txtNgayDat.getText().toString();
            String ngayTra = txtNgayTra.getText().toString();

            if (tenKhach.isEmpty() || sdt.isEmpty() || cccd.isEmpty() || ngayTra.isEmpty()) {
                Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference khachHangRef = FirebaseDatabase.getInstance().getReference("KhachHang");
            khachHangRef.orderByChild("cccd").equalTo(cccd).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String maKhachHang;
                            if (snapshot.exists()) {
                                for (DataSnapshot item : snapshot.getChildren()) {
                                    maKhachHang = item.getKey();
                                    capNhatPhong(phong, maKhachHang, ngayDat, ngayTra, holder);
                                    break;
                                }
                            } else {
                                String key = khachHangRef.push().getKey();
                                if (key != null) {
                                    KhachHang kh = new KhachHang();
                                    kh.key = key;
                                    kh.hoTen = tenKhach;
                                    kh.soDienThoai = sdt;
                                    kh.cccd = cccd;

                                    khachHangRef.child(key).setValue(kh)
                                            .addOnSuccessListener(aVoid -> capNhatPhong(phong, key, ngayDat, ngayTra, holder))
                                            .addOnFailureListener(e -> Toast.makeText(context, "Lỗi thêm khách: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "Lỗi Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void capNhatPhong(Datphong phong, String maKhachHang, String ngayDat, String ngayTra, ViewHolder holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("phong")
                .child(phong.getLoaiPhong())
                .child(phong.getTenPhong());

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("trangThai", "Đã đặt");
        updateData.put("maKhachHang", maKhachHang);
        updateData.put("ngayDat", ngayDat);
        updateData.put("ngayTra", ngayTra);

        ref.updateChildren(updateData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Đặt phòng thành công", Toast.LENGTH_SHORT).show();
                    phong.setTrangThai("Đã đặt");
                    phong.setMaKhachHang(maKhachHang);
                    phong.setNgayDat(ngayDat);
                    phong.setNgayTra(ngayTra);
                    notifyItemChanged(holder.getAdapterPosition());
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Lỗi đặt phòng: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showDatePicker(TextView txtNgayTra) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                txtNgayTra.getContext(),
                (view, year, month, dayOfMonth) -> txtNgayTra.setText(
                        String.format(Locale.getDefault(), "%02d/%02d/%d 14:00", dayOfMonth, month + 1, year)
                ),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showDialogTraPhong(ViewHolder holder, Datphong phong) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("phong")
                .child(phong.getLoaiPhong())
                .child(phong.getTenPhong());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Datphong phongMoi = snapshot.getValue(Datphong.class);
                if (phongMoi == null) {
                    Toast.makeText(context, "Không tìm thấy thông tin phòng!", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_xacnhan_traphong, null);
                builder.setView(view);

                String maKH = phongMoi.getMaKhachHang();
                DatabaseReference khRef = FirebaseDatabase.getInstance().getReference("KhachHang").child(maKH);
                khRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        KhachHang kh = snapshot.getValue(KhachHang.class);
                        if (kh != null) {
                            ((TextView) view.findViewById(R.id.txtTenKhach)).setText("Tên khách: " + kh.hoTen);
                            ((TextView) view.findViewById(R.id.txtCCCD)).setText("CCCD: " + kh.cccd);
                            ((TextView) view.findViewById(R.id.txtSDT)).setText("SĐT: " + kh.soDienThoai);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Lỗi tải khách hàng!", Toast.LENGTH_SHORT).show();
                    }
                });

                ((TextView) view.findViewById(R.id.txtNgayDat)).setText("Ngày đặt: " + phongMoi.getNgayDat());
                ((TextView) view.findViewById(R.id.txtNgayHenTra)).setText("Ngày hẹn trả: " + phongMoi.getNgayTra());

                String ngayTraThucTe = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
                ((TextView) view.findViewById(R.id.txtNgayTraThucTe)).setText("Ngày trả thực tế: " + ngayTraThucTe);

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                    Date dateNgayDat = sdf.parse(phongMoi.getNgayDat());
                    Date dateHenTra = sdf.parse(phongMoi.getNgayTra());
                    Date dateThucTe = sdf.parse(ngayTraThucTe);

                    final long soNgay = Math.max((dateHenTra.getTime() - dateNgayDat.getTime()) / (1000 * 60 * 60 * 24), 1);

                    final long[] soNgayTre = new long[1];
                    soNgayTre[0] = (dateThucTe.getTime() - dateHenTra.getTime()) / (1000 * 60 * 60 * 24);
                    if (soNgayTre[0] < 0) soNgayTre[0] = 0;

                    String giaString = phongMoi.getGia().replace(".", "").replace("đ", "").trim();
                    int giaPhong = Integer.parseInt(giaString);

                    final long tienThue = giaPhong * soNgay;
                    final long[] tienTre = new long[1];
                    tienTre[0] = giaPhong * soNgayTre[0];

                    DatabaseReference dvRef = FirebaseDatabase.getInstance().getReference("yeu_cau_dich_vu");
                    dvRef.orderByChild("soPhong").equalTo(phongMoi.getTenPhong())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotDV) {
                                    long tongTienDichVu = 0;
                                    for (DataSnapshot item : snapshotDV.getChildren()) {
                                        DichVu dv = item.getValue(DichVu.class);
                                        if (dv != null) {
                                            tongTienDichVu += dv.getDonGia() * dv.getSoLuong();
                                        }
                                    }

                                    long tongTien = tienThue + tienTre[0] + tongTienDichVu;

                                    ((TextView) view.findViewById(R.id.txtTienThue)).setText("Tiền thuê cơ bản: " + tienThue + " VNĐ");
                                    ((TextView) view.findViewById(R.id.txtSoNgayTre)).setText("Số ngày trễ: " + soNgayTre[0]);
                                    ((TextView) view.findViewById(R.id.txtTienTre)).setText("Tiền trễ: " + tienTre[0] + " VNĐ");
                                    ((TextView) view.findViewById(R.id.txtTongTienDichVu)).setText("Tiền dịch vụ: " + tongTienDichVu + " VNĐ");
                                    ((TextView) view.findViewById(R.id.txtTongTien)).setText("Tổng tiền: " + tongTien + " VNĐ");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(context, "Lỗi tải dịch vụ!", Toast.LENGTH_SHORT).show();
                                }
                            });

                } catch (ParseException e) {
                    Toast.makeText(context, "Lỗi định dạng ngày!", Toast.LENGTH_SHORT).show();
                }

                builder.setPositiveButton("Trả phòng", (dialog, which) -> {
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("trangThai", "Trống");
                    updateData.put("maKhachHang", null);
                    updateData.put("ngayDat", null);
                    updateData.put("ngayTra", null);

                    ref.updateChildren(updateData)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(context, "Trả phòng thành công", Toast.LENGTH_SHORT).show();
                                phong.setTrangThai("Trống");
                                notifyItemChanged(holder.getAdapterPosition());
                            })
                            .addOnFailureListener(e -> Toast.makeText(context, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                });

                builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
                builder.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Lỗi tải lại thông tin phòng!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return danhSach.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenPhong, tvLoaiPhong, tvGia, tvTrangThai;
        TextView btnDat, btnTraPhong;
        LinearLayout btnContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenPhong = itemView.findViewById(R.id.tvTenPhong);
            tvLoaiPhong = itemView.findViewById(R.id.tvLoaiPhong);
            tvGia = itemView.findViewById(R.id.tvGia);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            btnDat = itemView.findViewById(R.id.btnDat);
            btnTraPhong = itemView.findViewById(R.id.btnTraPhong);
            btnContainer = itemView.findViewById(R.id.btnContainer);
        }
    }

}
