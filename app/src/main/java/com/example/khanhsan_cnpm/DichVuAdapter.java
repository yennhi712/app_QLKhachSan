package com.example.khanhsan_cnpm;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DichVuAdapter extends RecyclerView.Adapter<DichVuAdapter.ViewHolder> {

    private List<DichVu> danhSach;

    public DichVuAdapter(List<DichVu> danhSach) {
        this.danhSach = danhSach;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvThutu, tvTenDichVu, tvDonGia, tvTong;
        EditText edtSoLuong;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvThutu = itemView.findViewById(R.id.tvThutu);
            tvTenDichVu = itemView.findViewById(R.id.tvTenDichVu);
            tvDonGia = itemView.findViewById(R.id.tvDonGia);
            edtSoLuong = itemView.findViewById(R.id.edtSoLuong);
            tvTong = itemView.findViewById(R.id.tvTong);
        }

        public void bind(DichVu dv) {
            tvThutu.setText(String.valueOf(dv.getThutu()));
            tvTenDichVu.setText(dv.getTen());
            tvDonGia.setText(dv.getDonGia() + "VNĐ");

            // Gỡ TextWatcher cũ nếu có
            if (edtSoLuong.getTag() instanceof TextWatcher) {
                edtSoLuong.removeTextChangedListener((TextWatcher) edtSoLuong.getTag());
            }

            edtSoLuong.setText(String.valueOf(dv.getSoLuong()));
            tvTong.setText(dv.tinhTongTien() + "VNĐ");

            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    int sl = 0;
                    try {
                        sl = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        sl = 0;
                    }
                    dv.setSoLuong(sl);
                    tvTong.setText(dv.tinhTongTien() + "K");
                }
            };

            edtSoLuong.addTextChangedListener(watcher);
            edtSoLuong.setTag(watcher);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dichvu, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(danhSach.get(position));
    }

    @Override
    public int getItemCount() {
        return danhSach.size();
    }

    // ✅ Thêm dịch vụ mới từ ngoài
    public void addDichVu(DichVu dv) {
        dv.setThutu(danhSach.size() + 1);
        danhSach.add(dv);
        notifyItemInserted(danhSach.size() - 1);
    }

    // ✅ Tính tổng tiền tất cả dịch vụ
    public int getTongTienTatCa() {
        int tong = 0;
        for (DichVu dv : danhSach) {
            tong += dv.tinhTongTien();
        }
        return tong;
    }

    // ✅ Trả về danh sách dịch vụ đang có
    public List<DichVu> getDanhSach() {
        return danhSach;
    }
}
