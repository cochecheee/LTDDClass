package com.example.bt_sqlite;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InternalStorageLoginActivity extends AppCompatActivity {

    // khai bao cac bien toan cuc
    private Button buttonTxt;
    private EditText usernameTxt, passwordTxt;
    private CheckBox cbRememberMe;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_internal_storage_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AnhXa();

        buttonTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameTxt.getText().toString().trim();
                String password = passwordTxt.getText().toString().trim();
                if(username.equals("admin") && password.equals("admin")) {
                    Toast.makeText(InternalStorageLoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    // neu co check checkbox
                    if(cbRememberMe.isChecked()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("taikhoan", username);
                        editor.putString("matkhau", password);
                        editor.putBoolean("trangthai", true);
                        //xac nhan luu
                        editor.commit();
                    }else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("taikhoan");
                        editor.remove("matkhau");
                        editor.remove("trangthai");
                        editor.commit();
                    }
                }else {
                    Toast.makeText(InternalStorageLoginActivity.this, "Dang nhap that bai", Toast.LENGTH_SHORT).show();
                    sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
                    usernameTxt.setText(sharedPreferences.getString("taikhoan",""));
                    passwordTxt.setText(sharedPreferences.getString("matkhau",""));
                    cbRememberMe.setChecked(sharedPreferences.getBoolean("trangthai", false));
                }
            }
        });
    }
    private void AnhXa() {
        buttonTxt = (Button) findViewById(R.id.buttonTxt);
        usernameTxt = (EditText) findViewById(R.id.usernameTxt);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt);
        cbRememberMe = (CheckBox) findViewById(R.id.cbmemberme);
    }
}