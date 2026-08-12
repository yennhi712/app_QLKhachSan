package com.example.khanhsan_cnpm;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginQuanLyActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_quanly);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String emailInput = edtEmail.getText().toString().trim();
        String passwordInput = edtPassword.getText().toString().trim();

        if (emailInput.isEmpty() || passwordInput.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference nhanVienRef = FirebaseDatabase.getInstance().getReference("nhanvien");

        nhanVienRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isAuthenticated = false;

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String email = userSnapshot.child("email").getValue(String.class);
                    String password = userSnapshot.child("matKhau").getValue(String.class);
                    String chucVu = userSnapshot.child("chucVu").getValue(String.class);

                    if (emailInput.equals(email)
                            && passwordInput.equals(password)
                            && "Quản lý".equalsIgnoreCase(chucVu)) {

                        isAuthenticated = true;
                        Intent intent = new Intent(LoginQuanLyActivity.this, Trangchinh_QL_Activity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }

                if (!isAuthenticated) {
                    Toast.makeText(LoginQuanLyActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginQuanLyActivity.this, "Lỗi kết nối Firebase!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}