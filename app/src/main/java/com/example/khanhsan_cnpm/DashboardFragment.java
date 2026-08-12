package com.example.khanhsan_cnpm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private TextView tvTotalRooms, tvBookedRooms, tvCustomers, tvRevenue;
    private TextView tvOccupancyRate, tvOccupancyPercent;
    private ProgressBar progressBar;
    private RecyclerView recyclerQuickActions, recyclerRecentBookings;

    private DatabaseReference dbRef;
    private BookingAdapter bookingAdapter;
    private List<Booking> recentBookings = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Ánh xạ
        tvTotalRooms = view.findViewById(R.id.tvTotalRooms);
        tvBookedRooms = view.findViewById(R.id.tvBookedRooms);
        tvCustomers = view.findViewById(R.id.tvCustomers);
        tvRevenue = view.findViewById(R.id.tvRevenue);
        tvOccupancyRate = view.findViewById(R.id.tvOccupancyRate);
        tvOccupancyPercent = view.findViewById(R.id.tvOccupancyPercent);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerQuickActions = view.findViewById(R.id.recyclerQuickActions);
        recyclerRecentBookings = view.findViewById(R.id.recyclerRecentBookings);

        recyclerQuickActions.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerQuickActions.setAdapter(new ThaotacnhanhAdapter(getContext(), getQuickActions()));

        recyclerRecentBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        bookingAdapter = new BookingAdapter(recentBookings);
        recyclerRecentBookings.setAdapter(bookingAdapter);

        dbRef = FirebaseDatabase.getInstance().getReference();

        loadDashboardData();

        return view;
    }

    private void loadDashboardData() {
        // Load total rooms & booked rooms
        dbRef.child("phong").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int total = 0, booked = 0;
                recentBookings.clear();

                for (DataSnapshot loaiPhongSnap : snapshot.getChildren()) {
                    for (DataSnapshot phongSnap : loaiPhongSnap.getChildren()) {
                        total++;
                        String tenPhong = phongSnap.child("tenPhong").getValue(String.class);
                        String trangThai = phongSnap.child("trangThai").getValue(String.class);
                        String ngayDat = phongSnap.child("ngayDat").getValue(String.class);
                        String maKhachHang = phongSnap.child("maKhachHang").getValue(String.class);

                        if ("Đã đặt".equalsIgnoreCase(trangThai) && maKhachHang != null) {
                            booked++;

                            DatabaseReference khachRef = FirebaseDatabase.getInstance().getReference("KhachHang").child(maKhachHang);
                            khachRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String tenKhach = snapshot.child("hoTen").getValue(String.class);

                                    recentBookings.add(new Booking(
                                            tenKhach != null ? tenKhach : "Chưa rõ",
                                            tenPhong != null ? tenPhong : "",
                                            ngayDat != null ? ngayDat : "",
                                            trangThai
                                    ));

                                    bookingAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { }
                            });
                        }
                    }
                }

                tvTotalRooms.setText(String.valueOf(total));
                tvBookedRooms.setText(String.valueOf(booked));

                double percent = total > 0 ? ((double) booked / total * 100) : 0;
                tvOccupancyRate.setText(booked + " / " + total + " phòng đã được đặt");
                tvOccupancyPercent.setText(String.format("%.1f%%", percent));
                progressBar.setProgress((int) percent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        // Load tổng khách hàng
        dbRef.child("KhachHang").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvCustomers.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        // Load tổng doanh thu dịch vụ
        dbRef.child("yeu_cau_dich_vu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long tong = 0;
                for (DataSnapshot dvSnap : snapshot.getChildren()) {
                    DichVu dv = dvSnap.getValue(DichVu.class);
                    if (dv != null) tong += dv.tinhTongTien();
                }
                tvRevenue.setText("₫" + tong + " VNĐ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private List<Thaotacnhanh> getQuickActions() {
        List<Thaotacnhanh> list = new ArrayList<>();
        list.add(new Thaotacnhanh("Đặt phòng mới", R.drawable.baseline_calendar_month_24));
        list.add(new Thaotacnhanh("Check-in", R.drawable.outline_auto_awesome_24));
        list.add(new Thaotacnhanh("Sơ đồ phòng", R.drawable.baseline_backup_table_24));
        list.add(new Thaotacnhanh("Báo cáo", R.drawable.outline_assessment_24));
        return list;
    }
}
