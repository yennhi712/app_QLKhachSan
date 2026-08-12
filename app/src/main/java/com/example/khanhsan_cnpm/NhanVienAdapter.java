package com.example.khanhsan_cnpm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.NhanVienViewHolder> {

    private Context context;
    private List<NhanVien> nhanVienList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(NhanVien nhanVien);
        void onDeleteClick(NhanVien nhanVien);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public NhanVienAdapter(Context context, List<NhanVien> nhanVienList) {
        this.context = context;
        this.nhanVienList = nhanVienList;
    }

    @NonNull
    @Override
    public NhanVienViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nhanvien, parent, false);
        return new NhanVienViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NhanVienViewHolder holder, int position) {
        NhanVien nv = nhanVienList.get(position);
        holder.txtMaNV.setText("Mã NV: " + nv.getMaNV());
        holder.txtHoTen.setText("Họ tên: " + nv.getHoTen());
        holder.txtChucVu.setText("Chức vụ: " + nv.getChucVu());

        holder.btnSua.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(nv);
            }
        });

        holder.btnXoa.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa nhân viên này không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (listener != null) {
                            listener.onDeleteClick(nv);
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return nhanVienList.size();
    }

    public static class NhanVienViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaNV, txtHoTen, txtChucVu;
        Button btnSua, btnXoa;

        public NhanVienViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaNV = itemView.findViewById(R.id.txtMaNV);
            txtHoTen = itemView.findViewById(R.id.txtHoTen);
            txtChucVu = itemView.findViewById(R.id.txtChucVu);
            btnSua = itemView.findViewById(R.id.btnSua);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }
    }
}
