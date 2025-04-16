package com.example.myapplication;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //khai báo biến toàn cục
    DatabaseHandler databaseHandler;
    ListView listView;
    ArrayList<NotesModel> arrayList;
    NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fabAddNote = findViewById(R.id.fabAddNote);
        fabAddNote.setOnClickListener(view -> DialogThem());

//        // Gán Toolbar vào ActionBar
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //gọi hàm databaseSQLite
        InitDatabaseSQLite();

        // Ánh xạ ListView và khởi tạo adapter
        listView = (ListView) findViewById(R.id.listView1);
        arrayList = new ArrayList<>();
        adapter = new NotesAdapter(this, R.layout.row_notes, arrayList);
        listView.setAdapter(adapter);

        //create databaseSQLite
        databaseSQLite();
    }

    private void createDatabaseSQLite(){
        //thêm dữ liệu vào bảng
        databaseHandler.QueryData("INSERT INTO Notes VALUES(null, ' Ví dụ SQLite 1')");
        databaseHandler.QueryData("INSERT INTO Notes VALUES(null, ' Ví dụ SQLite 2')");
    }

    private void InitDatabaseSQLite(){
        //khởi tạo database
        databaseHandler = new DatabaseHandler(this, "notes.sqlite", null, 1);
        //tạo bảng Notes
        databaseHandler.QueryData("CREATE TABLE IF NOT EXISTS Notes(Id INTEGER PRIMARY KEY AUTOINCREMENT, NameNotes VARCHAR(200))");
    }

    private void databaseSQLite(){
        //lấy dữ liệu
        Cursor cursor = databaseHandler.GetData("SELECT * FROM Notes");
        while(cursor.moveToNext()){
            //thêm dữ liệu vào ArrayList
            String name = cursor.getString(1);
            int id = cursor.getInt(0);
            arrayList.add(new NotesModel(id, name));
//            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
            Log.d("DatabaseData", "ID: " + id + ", Name: " + name); // Log dữ liệu ra Logcat
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuAddNotes) {
            DialogThem();
        }
        return super.onOptionsItemSelected(item);
    }

    private void DialogThem() {
        // Tạo dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_note);

        //mở rộng Dialog
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Ánh xạ view
        EditText editText = dialog.findViewById(R.id.editTextName);
        Button buttonAdd = dialog.findViewById(R.id.buttonEdit);
        Button buttonHuy = dialog.findViewById(R.id.buttonHuy);

        // Xử lý khi nhấn nút Thêm
        buttonAdd.setOnClickListener(view -> {
            String note = editText.getText().toString().trim();
            if (!note.isEmpty()) {
                // Thêm vào database
                databaseHandler.QueryData("INSERT INTO Notes VALUES(null, '" + note + "')");

                // Cập nhật ListView
                arrayList.add(new NotesModel(arrayList.size() + 1, note));
                adapter.notifyDataSetChanged();

                // Đóng dialog
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Vui lòng nhập ghi chú!", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý khi nhấn nút Hủy
        buttonHuy.setOnClickListener(view -> dialog.dismiss()); // Đóng dialog khi bấm Hủy

        dialog.show();
    }

    public void LoadData() {
        arrayList.clear(); // Xóa dữ liệu cũ để tránh bị trùng lặp

        Cursor cursor = databaseHandler.GetData("SELECT * FROM Notes");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            arrayList.add(new NotesModel(id, name));
        }
        cursor.close();

        adapter.notifyDataSetChanged(); // Cập nhật giao diện ListView
    }

    public void DialogCapNhat(int id, String oldNote) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_note);

        //mở rộng dialog
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText editTextUpdate = dialog.findViewById(R.id.editTextUpdate);
        Button buttonConfirmUpdate = dialog.findViewById(R.id.buttonConfirmUpdate);
        Button buttonCancelUpdate = dialog.findViewById(R.id.buttonCancelUpdate);

        // Gán nội dung cũ vào EditText
        editTextUpdate.setText(oldNote);

        // Xử lý cập nhật
        buttonConfirmUpdate.setOnClickListener(view -> {
            String newNote = editTextUpdate.getText().toString().trim();
            if (!newNote.isEmpty()) {
                databaseHandler.QueryData("UPDATE Notes SET NameNotes = '" + newNote + "' WHERE Id = " + id);
                LoadData(); // Cập nhật lại danh sách
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Ghi chú không được để trống!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCancelUpdate.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    public void DialogXoa(int id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Xác nhận xóa");
        dialog.setMessage("Bạn có chắc muốn xóa ghi chú này?");
        dialog.setPositiveButton("Xóa", (dialogInterface, i) -> {
            databaseHandler.QueryData("DELETE FROM Notes WHERE Id = " + id);
            LoadData(); // Cập nhật lại danh sách
            Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show();
        });
        dialog.setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        dialog.show();
    }

}