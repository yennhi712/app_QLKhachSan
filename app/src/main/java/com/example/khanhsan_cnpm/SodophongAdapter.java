package com.example.khanhsan_cnpm;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SodophongAdapter extends RecyclerView.Adapter<SodophongAdapter.ViewHolder> {

    private Context context;
    private List<Sodophong> danhSachPhong;

    public SodophongAdapter(Context context, List<Sodophong> danhSachPhong) {
        this.context = context;
        this.danhSachPhong = danhSachPhong;
    }

    @NonNull
    @Override
    public SodophongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sodophong, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SodophongAdapter.ViewHolder holder, int position) {
        Sodophong phong = danhSachPhong.get(position);

        holder.tvRoomName.setText(phong.getTenPhong());
        holder.tvRoomStatus.setText(phong.getTrangThai());
        holder.tvRoomNote.setText(phong.getLoaiPhong());

        // Ẩn/hiện nút "Xem" tuỳ trạng thái phòng
        if ("Đã đặt".equals(phong.getTrangThai())) {
            holder.btnView.setVisibility(View.VISIBLE);
        } else {
            holder.btnView.setVisibility(View.GONE);
        }

        // Sự kiện nút "Xem"
        holder.btnView.setOnClickListener(v -> {
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Datphong");
            dbRef.orderByChild("tenPhong").equalTo(phong.getTenPhong())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                Datphong dp = data.getValue(Datphong.class);
                                if (dp != null) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Thông tin phòng");

                                    String msg = "Phòng: " + dp.getTenPhong()
                                            + "\nLoại: " + dp.getLoaiPhong()
                                            + "\nGiá: " + dp.getGia()
                                            + "\nKhách: " + dp.getTenKhach()
                                            + "\nCCCD: " + dp.getCccd()
                                            + "\nSĐT: " + dp.getSoDienThoai()
                                            + "\nNgày đặt: " + dp.getNgayDat()
                                            + "\nNgày trả: " + dp.getNgayTra();

                                    builder.setMessage(msg);
                                    builder.setPositiveButton("Đóng", null);
                                    builder.show();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Tạm thời giữ nút "Xóa" như cũ
        holder.btnDelete.setOnClickListener(v ->
                Toast.makeText(context, "Xóa " + phong.getTenPhong(), Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return danhSachPhong.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvRoomStatus, tvRoomNote;
        ImageView btnView, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomStatus = itemView.findViewById(R.id.tvRoomStatus);
            tvRoomNote = itemView.findViewById(R.id.tvRoomNote);
            btnView = itemView.findViewById(R.id.btnView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public void updateList(List<Sodophong> newList) {
        this.danhSachPhong = newList;
        notifyDataSetChanged();
    }
}
