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

public class LoginNhanVienActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_nhanvien);

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

                for (DataSnapshot nvSnapshot : snapshot.getChildren()) {
                    String email = nvSnapshot.child("email").getValue(String.class);
                    String password = nvSnapshot.child("matKhau").getValue(String.class);

                    if (emailInput.equals(email) && passwordInput.equals(password)) {
                        isAuthenticated = true;

                        // Chuyển qua trang chính nhân viên
                        Intent intent = new Intent(LoginNhanVienActivity.this, TrangchinhActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }

                if (!isAuthenticated) {
                    Toast.makeText(LoginNhanVienActivity.this, "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginNhanVienActivity.this, "Lỗi kết nối Firebase!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
