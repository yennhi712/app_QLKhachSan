package com.example.khanhsan_cnpm;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DichVuDaDatAdapter extends RecyclerView.Adapter<DichVuDaDatAdapter.ViewHolder> {

    private List<DichVu> danhSach;
    private Context context;

    public DichVuDaDatAdapter(Context context, List<DichVu> danhSach) {
        this.context = context;
        this.danhSach = danhSach;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStt, tvTenKhach, tvSoPhong, tvTenDichVu;
        ImageButton btnXoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStt = itemView.findViewById(R.id.tvStt);
            tvTenKhach = itemView.findViewById(R.id.tvTenKhach);
            tvSoPhong = itemView.findViewById(R.id.tvSoPhong);
            tvTenDichVu = itemView.findViewById(R.id.tvTenDichVu);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }

        public void bind(DichVu dv, int position, List<DichVu> danhSach, Context context, DichVuDaDatAdapter adapter) {
            tvStt.setText(String.valueOf(position + 1));
            tvTenKhach.setText(dv.getHoTen() != null ? dv.getHoTen() : "Không rõ");
            tvSoPhong.setText(dv.getSoPhong() != null ? dv.getSoPhong() : "Không rõ");
            tvTenDichVu.setText(dv.getTen());

            btnXoa.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xoá")
                        .setMessage("Bạn có chắc muốn xoá yêu cầu này không?")
                        .setPositiveButton("Xoá", (dialog, which) -> {
                            if (dv.getMaYeuCau() != null) {
                                String path = "yeu_cau_dich_vu/" + dv.getMaYeuCau() + "_" + dv.getTen().replace(" ", "_");
                                FirebaseDatabase.getInstance().getReference(path)
                                        .removeValue()
                                        .addOnSuccessListener(aVoid -> {
                                            danhSach.remove(position);
                                            adapter.notifyItemRemoved(position);
                                            adapter.notifyItemRangeChanged(position, danhSach.size());
                                            Toast.makeText(context, "Đã xoá yêu cầu", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context, "Lỗi khi xoá: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Toast.makeText(context, "Không tìm thấy mã yêu cầu!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Huỷ", null)
                        .show();
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dsdichvu, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(danhSach.get(position), position, danhSach, context, this);
    }

    @Override
    public int getItemCount() {
        return danhSach.size();
    }

    // ✅ Thêm phương thức cập nhật danh sách
    // Thêm vào bên dưới getItemCount():
    public void capNhatDanhSach(List<DichVu> danhSachMoi) {
        this.danhSach = danhSachMoi;
        notifyDataSetChanged();
    }

}
