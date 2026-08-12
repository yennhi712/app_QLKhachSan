package com.example.khanhsan_cnpm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SodophongActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private Spinner spinnerLau;
    private LinearLayout layoutPhong;
    private List<Sodophong> danhSachPhong = new ArrayList<>();
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sodophong);

        // Drawer & Toolbar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Tùy chỉnh tiêu đề Toolbar
        TextView title = new TextView(this);
        title.setText("Sơ đồ phòng");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(android.R.color.white));
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT,
                android.view.Gravity.CENTER
        );
        title.setLayoutParams(params);
        toolbar.addView(title);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Layout & Spinner
        spinnerLau = findViewById(R.id.spinnerLau);
        layoutPhong = findViewById(R.id.layoutPhong);
        setupSpinner();

        databaseRef = FirebaseDatabase.getInstance().getReference("phong");
        loadPhongTuFirebase();
    }

    private void setupSpinner() {
        List<String> lauList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) lauList.add("Lầu " + i);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lauList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLau.setAdapter(adapter);

        spinnerLau.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hienThiLau(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadPhongTuFirebase() {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhSachPhong.clear();
                for (DataSnapshot loaiPhongSnap : snapshot.getChildren()) {
                    for (DataSnapshot phongSnap : loaiPhongSnap.getChildren()) {
                        Datphong datphong = phongSnap.getValue(Datphong.class);
                        if (datphong != null) {
                            danhSachPhong.add(new Sodophong(
                                    datphong.getTenPhong(),
                                    datphong.getLoaiPhong(),
                                    datphong.getTrangThai()
                            ));
                        }
                    }
                }
                spinnerLau.setSelection(0); // Mặc định Lầu 1
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SodophongActivity.this, "Lỗi tải dữ liệu phòng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hienThiLau(int lauSo) {
        layoutPhong.removeAllViews();

        List<Sodophong> don = new ArrayList<>();
        List<Sodophong> doi = new ArrayList<>();
        List<Sodophong> vip = new ArrayList<>();

        for (Sodophong p : danhSachPhong) {
            if (p.getLoaiPhong().equalsIgnoreCase("Phòng đơn")) don.add(p);
            else if (p.getLoaiPhong().equalsIgnoreCase("Phòng đôi")) doi.add(p);
            else if (p.getLoaiPhong().equalsIgnoreCase("Phòng VIP")) vip.add(p);
        }

        // Mỗi lầu lấy 2 phòng mỗi loại
        int start = (lauSo - 1) * 2;
        List<Sodophong> phongLau = new ArrayList<>();

        phongLau.addAll(layPhongTuLoai(don, start, 2));
        phongLau.addAll(layPhongTuLoai(doi, start, 2));
        phongLau.addAll(layPhongTuLoai(vip, start, 2));

        // Hiển thị
        for (Sodophong phong : phongLau) {
            TextView txt = new TextView(this);
            txt.setText(phong.getTenPhong() + " - " + phong.getLoaiPhong() + " - " + phong.getTrangThai());
            txt.setPadding(16, 8, 16, 8);
            txt.setBackgroundResource(R.drawable.border_phong);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 16); // margin dưới
            txt.setLayoutParams(params);

            layoutPhong.addView(txt);
        }

        if (phongLau.isEmpty()) {
            TextView txt = new TextView(this);
            txt.setText("Không có phòng ở lầu này.");
            txt.setPadding(16, 8, 16, 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 16);
            txt.setLayoutParams(params);

            layoutPhong.addView(txt);
        }
    }

    private List<Sodophong> layPhongTuLoai(List<Sodophong> list, int start, int count) {
        List<Sodophong> sub = new ArrayList<>();
        for (int i = start; i < start + count && i < list.size(); i++) {
            sub.add(list.get(i));
        }
        return sub;
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
}
