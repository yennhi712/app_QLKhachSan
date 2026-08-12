package com.example.khanhsan_cnpm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.Gravity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatphongActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerDanhSachPhong;
    private List<Datphong> danhSachPhong;
    private List<Datphong> danhSachGoc;

    private DatphongAdapter adapter;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private Spinner spinnerLoaiPhong;

    private DatabaseReference databaseRef;

    private Button startDateButton, endDateButton;
    private String ngayDat = "", ngayHenTra = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datphong);

        databaseRef = FirebaseDatabase.getInstance().getReference("phong");

        setupToolbar();
        setupDrawer();
        setupSpinner();
        setupRecyclerView();
        setupDatePickers();
        loadDataFromFirebase();
        listenForCustomerUpdates();
    }



    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView title = new TextView(this);
        title.setText("Đặt phòng");
        title.setTextSize(20);
        title.setTextColor(Color.WHITE);
        title.setTypeface(null, Typeface.BOLD);
        title.setLayoutParams(new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));
        toolbar.addView(title);
    }

    private void setupDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupSpinner() {
        spinnerLoaiPhong = findViewById(R.id.spinnerLoaiPhong);
        String[] loaiPhongArray = {"Tất cả", "Phòng đơn", "Phòng đôi", "Phòng VIP"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, loaiPhongArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiPhong.setAdapter(spinnerAdapter);

        spinnerLoaiPhong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String loaiChon = loaiPhongArray[position];
                if (loaiChon.equals("Tất cả")) {
                    adapter.setDanhSachPhong(new ArrayList<>(danhSachGoc));
                } else {
                    List<Datphong> danhSachLoc = new ArrayList<>();
                    for (Datphong phong : danhSachGoc) {
                        if (phong.getLoai().equalsIgnoreCase(loaiChon)) {
                            danhSachLoc.add(phong);
                        }
                    }
                    adapter.setDanhSachPhong(danhSachLoc);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupRecyclerView() {
        recyclerDanhSachPhong = findViewById(R.id.recyclerDanhSachPhong);
        recyclerDanhSachPhong.setLayoutManager(new LinearLayoutManager(this));
        danhSachPhong = new ArrayList<>();
        danhSachGoc = new ArrayList<>();
        adapter = new DatphongAdapter(this, danhSachPhong);
        recyclerDanhSachPhong.setAdapter(adapter);
    }

    private void setupDatePickers() {
        startDateButton = findViewById(R.id.startDate);
        endDateButton = findViewById(R.id.endDate);

        startDateButton.setOnClickListener(v -> showDatePickerDialog(true));
        endDateButton.setOnClickListener(v -> showDatePickerDialog(false));
    }

    private void loadDataFromFirebase() {
        databaseRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                danhSachPhong.clear();
                if (!dataSnapshot.exists()) {
                    danhSachPhong.addAll(getDummyData());
                } else {
                    for (DataSnapshot loaiPhongSnap : dataSnapshot.getChildren()) {
                        for (DataSnapshot phongSnap : loaiPhongSnap.getChildren()) {
                            Datphong phong = phongSnap.getValue(Datphong.class);
                            danhSachPhong.add(phong);
                        }
                    }
                }
                danhSachGoc = new ArrayList<>(danhSachPhong);
                adapter.setDanhSachPhong(new ArrayList<>(danhSachPhong));
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {}
        });
    }

    private void listenForCustomerUpdates() {
        getSupportFragmentManager().setFragmentResultListener("reloadKhachHang", this, (requestKey, result) -> {
            if (result.getBoolean("capNhatThanhCong", false)) {
                Toast.makeText(this, "Cập nhật khách hàng thành công", Toast.LENGTH_SHORT).show();
                loadDatphong();
            }
        });
    }

    private List<Datphong> getDummyData() {
        List<Datphong> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String maPhong = "P1" + String.format("%02d", i);
            Datphong phong = new Datphong(maPhong, "Phòng đơn", "500.000đ", "Trống");
            list.add(phong);
            databaseRef.child("Phòng đơn").child(maPhong).setValue(phong);
        }
        for (int i = 1; i <= 10; i++) {
            String maPhong = "P2" + String.format("%02d", i);
            Datphong phong = new Datphong(maPhong, "Phòng đôi", "700.000đ", "Trống");
            list.add(phong);
            databaseRef.child("Phòng đôi").child(maPhong).setValue(phong);
        }
        for (int i = 1; i <= 10; i++) {
            String maPhong = "P3" + String.format("%02d", i);
            Datphong phong = new Datphong(maPhong, "Phòng VIP", "1.200.000đ", "Trống");
            list.add(phong);
            databaseRef.child("Phòng VIP").child(maPhong).setValue(phong);
        }
        return list;
    }

    private void showDatePickerDialog(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    if (isStartDate) {
                        startDateButton.setText(date);
                        ngayDat = date;
                    } else {
                        endDateButton.setText(date);
                        ngayHenTra = date;
                    }
                    adapter.setNgayDatVaHenTra(ngayDat, ngayHenTra);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_trangchu) {
            startActivity(new Intent(this, TrangchinhActivity.class));
        } else if (id == R.id.nav_logout) {
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void loadDatphong() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Datphong");
        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhSachPhong.clear();
                danhSachGoc.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Datphong dp = snap.getValue(Datphong.class);
                    if (dp != null) {
                        if (dp.getKey() == null) {
                            dp.setKey(snap.getKey());
                        }
                        danhSachPhong.add(dp);
                    }
                }
                danhSachGoc.addAll(danhSachPhong);
                adapter.setDanhSachPhong(new ArrayList<>(danhSachPhong));
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                Toast.makeText(DatphongActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
