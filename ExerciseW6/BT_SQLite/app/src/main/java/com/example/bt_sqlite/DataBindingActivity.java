package com.example.bt_sqlite;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.example.bt_sqlite.databinding.ActivityDataBindingBinding;
import com.example.bt_sqlite.models.User;

public class DataBindingActivity extends AppCompatActivity {
    // initialize data
    private User userModel;
    // <xml_filename>Binding
    private ActivityDataBindingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_binding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // trả về binding ánh xạ các view của layout (không cần findViewById() nữa
        binding = DataBindingUtil.setContentView(this,R.layout.activity_data_binding);
        // khởi tạo user
        userModel = new User("Co", "cheche");
        binding.setUser(userModel);
        userModel.setFirstName("Bui");
        userModel.setLastName("Tien");
    }
}