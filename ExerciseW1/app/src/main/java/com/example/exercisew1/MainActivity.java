package com.example.exercisew1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView info_text;
    private String content = "Bùi Lê Thủy Tiên\n Mssv: 22162048";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide header bar
        this.hideHeaderBar();
        setContentView(R.layout.activity_main);
        // binding
        this.bindingView();
        info_text.setText(content);
    }
    private void hideHeaderBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        // this -> current instance (MainActivity)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    private void bindingView() {
        this.info_text = (TextView) findViewById(R.id.info_text);
    }
}