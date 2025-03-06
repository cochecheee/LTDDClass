package com.example.bt1.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bt1.R;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {
    // fake database (listview data)
    // khai báo
    ListView listView;
    ArrayList<String> arrayList;
    Button btnAdd;
    EditText tvAdd;
    // For update item
    int vitri = -1;
    Button btnUpdate;
    // For delete
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // step 2: initialize data
        bindingView();
        initilizeData();

        // https://hiepsiit.com/detail/android/laptrinhandroid/arrayadapter#google_vignette
        // step 3: create array adapter
        ArrayAdapter adapter = new ArrayAdapter(
                this,//context
                R.layout.simple_layout_list_1, //listview
                R.id.textView, //render data to this textview
                arrayList
        );

        //step 4: truyền data từ adapter ra view
        listView.setAdapter(adapter);

        // step 5: bat sự kiện trên listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //code yêu cầu
                    //i: trả về vị trí click chuột trên ListView -> i ban đầu =0
                    Toast.makeText(ListViewActivity.this, ""+i, Toast.LENGTH_SHORT).show();
                }
            });

        // step 6: add data to listview
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tvAdd.getText().toString();
                arrayList.add(name);
                adapter.notifyDataSetChanged(); // reset adapter whenever arraylist changed
            }
        });

        // step 7: update data
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //lấy nội dung đua lên edittext
                tvAdd.setText(arrayList.get(i));
                vitri = i;
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  arrayList.set(vitri, tvAdd.getText().toString());
                  adapter.notifyDataSetChanged();
              }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.remove(vitri);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void initilizeData() {
        //Thêm dữ liệu vào List
        arrayList = new ArrayList<>();
        arrayList.add("Java");
        arrayList.add("C#");
        arrayList.add("PHP");
        arrayList.add("Kotlin");
        arrayList.add("Dart");
    }
    private void bindingView() {
        //ánh xạ
        listView = (ListView) findViewById(R.id.listview);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        tvAdd = (EditText) findViewById(R.id.tvAdd);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
    }
}