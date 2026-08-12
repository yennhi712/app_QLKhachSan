package com.example.khanhsan_cnpm;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DoanhThuFragment extends Fragment {

    private Button btnChonTuNgay, btnChonDenNgay, btnThongKe;
    private TextView tvTongDoanhThu;
    private Spinner spTuyChon;
    private BarChart barChart;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private Date tuNgay, denNgay;
    private DatabaseReference phongRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doanhthu, container, false);

        // Ánh xạ view
        btnChonTuNgay = view.findViewById(R.id.btnChonTuNgay);
        btnChonDenNgay = view.findViewById(R.id.btnChonDenNgay);
        btnThongKe = view.findViewById(R.id.btnThongKe);
        tvTongDoanhThu = view.findViewById(R.id.tvTongDoanhThu);
        spTuyChon = view.findViewById(R.id.spTuyChon);
        barChart = view.findViewById(R.id.barChart);

        phongRef = FirebaseDatabase.getInstance().getReference("phong");

        btnChonTuNgay.setOnClickListener(v -> showDatePicker(true));
        btnChonDenNgay.setOnClickListener(v -> showDatePicker(false));

        btnThongKe.setOnClickListener(v -> {
            if (tuNgay == null || denNgay == null) {
                Toast.makeText(getContext(), "Vui lòng chọn khoảng ngày", Toast.LENGTH_SHORT).show();
            } else {
                tinhDoanhThu();
            }
        });

        // Spinner chọn nhanh
        spTuyChon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return; // Bỏ qua item "-- Tùy chọn nhanh --"

                Calendar calendar = Calendar.getInstance();
                denNgay = calendar.getTime();

                switch (position) {
                    case 1: calendar.add(Calendar.DAY_OF_MONTH, -1); break; // 1 ngày qua
                    case 2: calendar.add(Calendar.DAY_OF_MONTH, -7); break; // 1 tuần qua
                    case 3: calendar.add(Calendar.MONTH, -1); break;        // 1 tháng qua
                    case 4: calendar.add(Calendar.YEAR, -1); break;         // 1 năm qua
                }

                tuNgay = calendar.getTime();
                btnChonTuNgay.setText(sdf.format(tuNgay));
                btnChonDenNgay.setText(sdf.format(denNgay));
                tinhDoanhThu();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    private void showDatePicker(boolean isTuNgay) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getContext(), (DatePicker view, int year, int month, int dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);
            String selectedDateStr = sdf.format(selected.getTime());

            if (isTuNgay) {
                tuNgay = selected.getTime();
                btnChonTuNgay.setText(selectedDateStr);
            } else {
                denNgay = selected.getTime();
                btnChonDenNgay.setText(selectedDateStr);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void tinhDoanhThu() {
        phongRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long tongTien = 0;
                HashMap<String, Long> doanhThuTheoNgay = new HashMap<>();

                for (DataSnapshot loaiPhongSnap : snapshot.getChildren()) {
                    for (DataSnapshot phongSnap : loaiPhongSnap.getChildren()) {
                        Datphong datphong = phongSnap.getValue(Datphong.class);
                        if (datphong != null && "Đã đặt".equalsIgnoreCase(datphong.getTrangThai())) {
                            try {
                                String ngayDatFull = datphong.getNgayDat(); // vd: "26/07/2025 11:14"
                                String[] parts = ngayDatFull.split(" ");
                                if (parts.length > 0) {
                                    String ngayStr = parts[0];
                                    Date ngayDat = sdf.parse(ngayStr);

                                    if (ngayDat != null && !ngayDat.before(tuNgay) && !ngayDat.after(denNgay)) {
                                        long tien = datphong.getTienPhong();
                                        tongTien += tien;

                                        doanhThuTheoNgay.put(ngayStr, doanhThuTheoNgay.getOrDefault(ngayStr, 0L) + tien);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                DecimalFormat formatter = new DecimalFormat("#,###");
                tvTongDoanhThu.setText("Tổng doanh thu: " + formatter.format(tongTien) + "đ");

                veBieuDo(doanhThuTheoNgay);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi đọc dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void veBieuDo(HashMap<String, Long> data) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        List<String> sortedDates = new ArrayList<>(data.keySet());
        sortedDates.sort((d1, d2) -> {
            try {
                return sdf.parse(d1).compareTo(sdf.parse(d2));
            } catch (Exception e) {
                return 0;
            }
        });

        for (int i = 0; i < sortedDates.size(); i++) {
            String date = sortedDates.get(i);
            long value = data.get(date);
            entries.add(new BarEntry(i, value));
            labels.add(date);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu");
        dataSet.setColor(Color.parseColor("#FFA726"));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-45);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);

        barChart.getDescription().setEnabled(false);
        barChart.invalidate();
    }
}
