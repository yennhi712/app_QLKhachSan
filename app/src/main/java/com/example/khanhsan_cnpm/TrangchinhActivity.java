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

public class TrangchinhActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchinh);

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.getMenu().findItem(R.id.nav_nhanvien).setVisible(false); // ẩn nv
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Gán toolbar custom
        Toolbar toolbar = findViewById(R.id.custom_toolbar); // toolbar này nằm trong custom_toolbar.xml
        setSupportActionBar(toolbar);

        // Toggle để mở/đóng menu trái
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setupNavigationView();

        // Xử lý menu dưới (duoi_menu)
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_sodophong) {
                Intent intent = new Intent(TrangchinhActivity.this, SodophongActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_datphong) {
                Intent intent = new Intent(TrangchinhActivity.this, DatphongActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_main, new DashboardFragment())
                .commit();


    }
    // Xử lý khi chọn item trong menu trái (nav_menu)
    private void setupNavigationView() {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_trangchu) {
                    loadFragment(new DashboardFragment());
                } else if (id == R.id.nav_dichvu) {
                    loadFragment(new DichvuFragment());
                } else if (id == R.id.nav_khachhang) {
                    loadFragment(new KhachHangFragment());
                } else if (id == R.id.nav_logout) {
                    Toast.makeText(TrangchinhActivity.this, "Đăng xuất!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TrangchinhActivity.this, ChonvaitroActivity.class));
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    //ấn logout để out ra
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
