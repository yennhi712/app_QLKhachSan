package com.example.khanhsan_cnpm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class Trangchinh_QL_Activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchinh_quanly);

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.custom_toolbar);

        setSupportActionBar(toolbar);

        // Setup toggle cho menu trái
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Xử lý menu trái (Navigation Drawer)
        setupNavigationView();

        // Xử lý menu dưới (Bottom Navigation)
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_sodophong) {
                startActivity(new Intent(this, SodophongActivity.class));
                return true;
            } else if (id == R.id.nav_datphong) {
                startActivity(new Intent(this, DatphongActivity.class));
                return true;
            }
            return false;
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_main, new DashboardFragment())
                .commit();

    }

    private void setupNavigationView() {
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_trangchu) {
                loadFragment(new DashboardFragment());
            } else if (id == R.id.nav_dichvu) {
                    loadFragment(new DichvuFragment());
            } else if (id == R.id.nav_khachhang) {
                loadFragment(new KhachHangFragment());
            } else if (id == R.id.nav_nhanvien) {
                loadFragment(new QLNhanVienFragment());
            } else if (id == R.id.nav_thongke) {
                loadFragment(new DoanhThuFragment());
            } else if (id == R.id.nav_logout) {
                Toast.makeText(this, "Đăng xuất...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ChonvaitroActivity.class));
                finish();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_main, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
