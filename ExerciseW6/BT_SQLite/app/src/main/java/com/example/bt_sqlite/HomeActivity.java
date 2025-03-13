package com.example.bt_sqlite;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bt_sqlite.adapters.ListUserAdapter;
import com.example.bt_sqlite.databinding.ActivityHomeBinding;
import com.example.bt_sqlite.models.User;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements ListUserAdapter.OnItemClickListener {
    // moi lan tao class -> rebuild project

    // Khi giá trị của ObservableField thay đổi,
    // UI được cập nhật tự động mà không cần gọi notifyChange() như Observable thông thường.
    public ObservableField<String> title = new ObservableField<>();
    //initialize adapter for recycleview
    private ListUserAdapter listUserAdapter;
    private ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // binding view from layout activity home
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        title.set("Danh sách người dùng");
        binding.setHome(this); // set data for home variable in home activity layout

        // initilize data for adapter
        setData();
//        if (listUserAdapter != null) {
//            Log.d("NULL", "list user adapter null!");
//        }

        binding.rcView.setAdapter(listUserAdapter);
        binding.rcView.setLayoutManager(new LinearLayoutManager(this));
        listUserAdapter.setOnItemClickListener(this);
    }

    private void setData() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setLastName("Tien " + i);
            user.setFirstName("Bui " + i);
            userList.add(user);
        }
        // initilize adapter
        listUserAdapter = new ListUserAdapter(userList);
    }

    @Override
    public void itemClick(User user) {
        Toast.makeText(this,"bạn vừa click vào user " + user.getFirstName() + " " + user.getLastName(), Toast.LENGTH_SHORT).show();
    }
}