package com.example.bt1.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bt1.R;

import java.util.ArrayList;

public class GridViewActivity extends AppCompatActivity {
    //khai báo
    GridView gridView;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grid_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeData();

        //Tạo ArrayAdapter
        ArrayAdapter adapter = new ArrayAdapter(
                GridViewActivity.this,
                android.R.layout.simple_list_item_1,
                arrayList
        );

        //truyền dữ liệu từ adapter ra gridview
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?>
                                                adapterView, View view, int i, long l) {
                    //code yêu cầu
                    //i: trả về vị trí click chuột trên GridView -> i ban đầu =0
                    Toast.makeText(GridViewActivity.this,"" + i, Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void initializeData() {
        //ánh xạ
        gridView = (GridView) findViewById(R.id.gridview1);
        //Thêm dữ liệu vào List
        arrayList = new ArrayList<>();
        arrayList.add("Java");
        arrayList.add("C#");
        arrayList.add("PHP");
        arrayList.add("Kotlin");
        arrayList.add("Dart");
    }
}