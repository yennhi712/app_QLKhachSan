package com.example.khanhsan_cnpm;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.KhachHangViewHolder> {

    private List<KhachHang> danhSach;
    private OnItemActionListener listener;

    public interface OnItemActionListener {
        void onEdit(KhachHang khachHang);
        void onDelete(KhachHang khachHang);
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.listener = listener;
    }

    public KhachHangAdapter(List<KhachHang> danhSach) {
        this.danhSach = danhSach;
    }

    // Hàm cập nhật dữ liệu mới cho adapter
    public void updateData(List<KhachHang> danhSachMoi) {
        this.danhSach = danhSachMoi;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KhachHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_khachhang, parent, false);
        return new KhachHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhachHangViewHolder holder, int position) {
        KhachHang khachHang = danhSach.get(position);
        holder.bind(khachHang);
    }

    @Override
    public int getItemCount() {
        return danhSach == null ? 0 : danhSach.size();
    }

    class KhachHangViewHolder extends RecyclerView.ViewHolder {

        TextView tvHoTen, tvSDT, tvCCCD;
        Button btnCapNhat, btnXoa;

        public KhachHangViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHoTen = itemView.findViewById(R.id.tvHoTen);
            tvSDT = itemView.findViewById(R.id.tvSDT);
            tvCCCD = itemView.findViewById(R.id.tvCCCD);
            btnCapNhat = itemView.findViewById(R.id.btnCapNhat);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }

        public void bind(final KhachHang khachHang) {
            if (khachHang == null) return;

            tvHoTen.setText("Họ tên: " + (khachHang.hoTen == null ? "" : khachHang.hoTen));
            tvSDT.setText("SĐT: " + (khachHang.soDienThoai == null ? "" : khachHang.soDienThoai));
            tvCCCD.setText("CCCD: " + (khachHang.cccd == null ? "" : khachHang.cccd));

            btnCapNhat.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEdit(khachHang);
                }
            });

            btnXoa.setOnClickListener(v -> {
                if (listener != null) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Xoá khách hàng")
                            .setMessage("Bạn có chắc chắn muốn xoá khách hàng này không?")
                            .setPositiveButton("Xoá", (dialog, which) -> {
                                listener.onDelete(khachHang);
                            })
                            .setNegativeButton("Huỷ", null)
                            .show();
                }
            });
        }
    }
}
