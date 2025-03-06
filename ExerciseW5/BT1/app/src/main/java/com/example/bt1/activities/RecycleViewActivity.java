package com.example.bt1.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt1.R;
import com.example.bt1.adapters.SongAdapter;
import com.example.bt1.model.SongModel;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewActivity extends AppCompatActivity {
    // khai bào
    private List<SongModel> songs;
    RecyclerView rvSongs;
    SongAdapter adapter;
    LinearLayoutManager layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recycle_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeData();

        adapter = new SongAdapter(this, songs);
        rvSongs.setAdapter(adapter);
        layout = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        rvSongs.setLayoutManager(layout);
    }
    private void initializeData() {
        rvSongs = (RecyclerView) findViewById(R.id.rv_songs);
        songs = new ArrayList<>();
        // khoi tao lust song
        songs.add(new SongModel("69696",  "NẾU EM CÒN TỒN TẠI", "Khi anh bắt đầu 1 tình yêu Là lúc anh tự thay",  "Trịnh Đình Quang"));
        songs.add(new SongModel("60781",  "NGỐC", "Có rất nhiều những câu chuyện Em dấu riêng mình em biết",  "Khắc Việt"));
        songs.add(new SongModel("68650", "HÃY TIN ANH LẦN NỮA","Dầu cho ta đã sai khi ở bên nhau Cô yêu thương",  "Thiên Dũng"));
        songs.add(new SongModel("68618", "CHUỖI NGÀY VẮNG EM",  "Từ khi em bước ra đi cõi lòng anh ngập tràn bao", "Duy Cường"));
        songs.add(new SongModel( "68656","KHI NGƯỜI MÌNH YÊU KHÓC",  "Nước mắt em đang rơi trên những ngón tay Nước mắt em",  "Phan Mạnh Quỳnh"));
        songs.add(new SongModel("60685",  "NỢ",  "Anh mơ gặp em anh mơ được ôm anh mơ được gần", "Trịnh Thăng Bình"));
        songs.add(new SongModel("60752",  "TÌNH YÊU CHẤP VÁ", "Muốn đi xa nơi yêu thương mình từng có Để không nghe",  "Mr. Siro"));
        songs.add(new SongModel( "68688",  "CHỜ NGÀY MƯA TAN",  "1 ngày mưa và em khuất xa nơi anh bóng dáng cũ", "Trung Đức"));
        songs.add(new SongModel("68603",  "CÂU HỎI EM CHƯA TRẢ LỜI", "Cần nơi em 1 lời giải thích thật lòng Đừng lặng im", "Yuki Huy Nam"));
        songs.add(new SongModel("60720",  "QUA ĐI LẶNG LẼ",  "Đôi khi đến với nhau yêu thương chẳng được lâu nhưng khi",  "Phan Mạnh Quỳnh"));
        songs.add(new SongModel("68856",  "QUÊN ANH LÀ ĐIỀU EM KHÔNG THỂ - REMIX",  "Cần thêm bao lâu để em quên đi niềm đau Cần thêm",  "Thiện Ngôn"));
    }
}