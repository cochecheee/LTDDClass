package com.example.bt1.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bt1.R;
import com.example.bt1.adapters.MonHocAdapter;
import com.example.bt1.model.MonHoc;

import java.util.ArrayList;
import java.util.Collections;

public class CustomListViewActivity extends AppCompatActivity {
    //khai báo
    ListView listView; // listview
    ArrayList<MonHoc> arrayList; //fake data
    MonHocAdapter adapter; // render data to listview
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_custom_list_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();

        // create adapter
        adapter = new MonHocAdapter(this, R.layout.row_monhoc, arrayList);

        listView.setAdapter(adapter);
    }

    private void bindingView() {
        listView = findViewById(R.id.customListView);

        // initialize data to Monhoc list
        arrayList = new ArrayList<>();
        arrayList.add(new MonHoc("Java","Java 1",R.drawable.java1));
        arrayList.add(new MonHoc("C#","C# 1",R.drawable.c));
        arrayList.add(new MonHoc("PHP","PHP 1",R.drawable.php));
        arrayList.add(new MonHoc("Kotlin","Kotlin 1",R.drawable.kotlin));
        arrayList.add(new MonHoc("Dart","Dart 1",R.drawable.dart));
    }
}