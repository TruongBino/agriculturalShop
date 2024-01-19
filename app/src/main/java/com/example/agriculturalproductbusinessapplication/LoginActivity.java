package com.example.agriculturalproductbusinessapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView tvSignUp;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvSignUp = findViewById(R.id.tvsignUp);
        editTextEmail = findViewById(R.id.edt_email);
        editTextPassword = findViewById(R.id.edt_password);
        buttonLogin = findViewById(R.id.btn_login);
        databaseHelper = new DatabaseHelper(this);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    // Kiểm tra role là admin hoặc user
                    int role = 0; // 0: user, 1: admin
                    if (email.equals("admin@gmail.com")) {
                        role = 1;
                    }

                    if (role == 1) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công với vai trò admin", Toast.LENGTH_SHORT).show();
                        Intent adminIntent = new Intent(LoginActivity.this, AdminManagementActivity.class);
                        startActivity(adminIntent);
                    } else {
                        if (databaseHelper.checkUser(email, password, role)) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            // Thực hiện một số hành động sau khi đăng nhập thành công, như khởi động một hoạt động mới
                        } else {
                            Toast.makeText(LoginActivity.this, "Sai Email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}