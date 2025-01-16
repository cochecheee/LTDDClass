package com.example.exercisew2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity {
    private ImageView imgView;
    private ConstraintLayout bg;
    private ImageButton imgBtn;
    private Switch sw;
    private CheckBox ck1;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide header bar
        this.hideHeaderBar();
        setContentView(R.layout.activity_main2);

        // binding
        this.bindingViewToActivity();

        // set image
        imgView.setImageResource(R.drawable.tomoe);

        // set background
        bg.setBackgroundColor(Color.BLUE);
        //bg.setBackgroundResource(R.drawable.bg3);
        // random background
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.drawable.bg1);
        arrayList.add(R.drawable.bg2);
        arrayList.add(R.drawable.bg3);
        arrayList.add(R.drawable.bg4);
        Random random = new Random();
        int vitri = random.nextInt(arrayList.size());
        bg.setBackgroundResource(arrayList.get(vitri));

        // image button on click
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgView.setImageResource(R.drawable.naomi);
                imgView.getLayoutParams().width=550;
                imgView.getLayoutParams().height=550;
            }
        });

        // listen to switch event
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){ //isChecked = true
                    Toast.makeText(MainActivity2.this,"Wifi đang bật",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity2.this,"Wifi đang tắt",Toast.LENGTH_LONG).show();
                }
            }
        });

        // listen to checkbox event
        ck1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    bg.setBackgroundResource(R.drawable.bg3);
                }else{
                    bg.setBackgroundResource(R.drawable.bg4);
                }
            }
        });

        // listen to radio group event
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This method is triggered whenever a radio button is checked
                if (checkedId == R.id.radioButton) {
                    bg.setBackgroundResource(R.drawable.bg3);
                } else if (checkedId == R.id.radioButton2) {
                    bg.setBackgroundResource(R.drawable.bg4);
                }
            }
        });
    }

    // binding
    private void bindingViewToActivity() {
        this.imgView = (ImageView) findViewById(R.id.imageView1);
        this.bg = (ConstraintLayout) findViewById(R.id.constraintLayout1);
        this.imgBtn = (ImageButton) findViewById(R.id.imageButton1);
        this.sw = (Switch) findViewById(R.id.switch1);
        this.ck1 = (CheckBox) findViewById(R.id.checkBox);
        this.radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
    }
    private void hideHeaderBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        // this -> current instance (MainActivity)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}