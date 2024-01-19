package com.example.agriculturalproductbusinessapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageButton btnuploadAvatar;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editPhone;
    private Button buttonSignUp;
    private TextView tvLogin;
    private DatabaseHelper databaseHelper;

    private Uri selectedImageUri;
    private byte[] avatar;
    private ImageView imageViewAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tvLogin = findViewById(R.id.tv_login);
        imageViewAvatar=findViewById(R.id.view_avatar);
        btnuploadAvatar = findViewById(R.id.btn_uploadAvatar);
        editTextUsername = findViewById(R.id.edt_userName);
        editTextEmail = findViewById(R.id.edt_email);
        editPhone = findViewById(R.id.edt_phone);
        editTextPassword = findViewById(R.id.edt_password);
        buttonSignUp = findViewById(R.id.btn_signUp);
        databaseHelper = new DatabaseHelper(this);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnuploadAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String phone = editPhone.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng điền đầy đủ thông tin đăng ký", Toast.LENGTH_SHORT).show();
                } else {
                    if (databaseHelper.isEmailExists(email)) {
                        Toast.makeText(SignUpActivity.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!isValidEmail(email)) {
                            Toast.makeText(SignUpActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                        } else if (!checkPass()) {
                            // Mật khẩu không đúng định dạng, không tiếp tục đăng ký
                        } else {
                            int role = 0; // 0: user
                            if (email.equals("admin@gmail.com")) {
                                role = 1; // Nếu email là "admin@gmail.com", thì vai trò là admin
                            }
                            // Kiểm tra vai trò là user
                            databaseHelper.addUser(username, phone, email, password, role, avatar);
                            Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
            }
        });
    }

    private boolean checkPass() {
        String password = editTextPassword.getText().toString().trim();

        if (password.length() < 8) {
            Toast.makeText(SignUpActivity.this, "Mật khẩu phải có ít nhất 8 kí tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(SignUpActivity.this, "Mật khẩu phải có chữ cái đầu viết hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            Toast.makeText(SignUpActivity.this, "Mật khẩu phải có ít nhất một số", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh đại diện"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imageViewAvatar.setImageBitmap(bitmap);

                // Convert bitmap to byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                avatar = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("SignUpActivity", "lỗi tải ảnh từ thư viện: " + e.getMessage());
            }
        }
    }
}