package com.example.khanhsan_cnpm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ThaotacnhanhAdapter extends RecyclerView.Adapter<ThaotacnhanhAdapter.ViewHolder> {
    private List<Thaotacnhanh> danhSach;
    private Context context;

    public ThaotacnhanhAdapter(Context context, List<Thaotacnhanh> danhSach) {
        this.context = context;
        this.danhSach = danhSach;
    }

    @NonNull
    @Override
    public ThaotacnhanhAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thaotacnhanh, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThaotacnhanhAdapter.ViewHolder holder, int position) {
        Thaotacnhanh item = danhSach.get(position);
        holder.txtTieude.setText(item.getTitle());
        holder.imgIcon.setImageResource(item.getIconRes());

        holder.itemView.setOnClickListener(v -> {
            String tieude = item.getTitle();
            if ("Đặt phòng mới".equalsIgnoreCase(tieude)) {
                Intent intent = new Intent(context, DatphongActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Cần thiết khi dùng context là Fragment
                context.startActivity(intent);
            } else if ("Sơ đồ phòng".equalsIgnoreCase(tieude)) {
                Intent intent = new Intent(context, SodophongActivity.class);
                context.startActivity(intent);
            }

            // Bạn có thể thêm xử lý cho các thao tác khác ở đây nếu cần
            // else if ("Check-in".equalsIgnoreCase(tieude)) { ... }
        });
    }

    @Override
    public int getItemCount() {
        return danhSach.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTieude;
        ImageView imgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTieude = itemView.findViewById(R.id.txtTieude);
            imgIcon = itemView.findViewById(R.id.imgIcon);
        }
    }
}
