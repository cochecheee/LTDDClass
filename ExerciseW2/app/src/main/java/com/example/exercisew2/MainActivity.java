package com.example.exercisew2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide header bar
        this.hideHeaderBar();

        setContentView(R.layout.activity_main);
    }

    private void hideHeaderBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        // this -> current instance (MainActivity)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}