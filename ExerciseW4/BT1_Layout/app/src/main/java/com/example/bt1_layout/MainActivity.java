package com.example.bt1_layout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayout1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // binding image view to main activity
        ImageView img1= (ImageView)
                findViewById(R.id.imageView1);
        img1.setImageResource(R.drawable.kotlin);

        // binding layout to main activity
        ConstraintLayout bg = (ConstraintLayout)
                findViewById(R.id.constraintLayout1);
        bg.setBackgroundColor(Color.BLUE);
        bg.setBackgroundResource(R.drawable.bg2);

        // load random
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.drawable.bg1);
        arrayList.add(R.drawable.bg2);
        arrayList.add(R.drawable.bg3);
        arrayList.add(R.drawable.bg4);

        Random random = new Random();
        int vitri = random.nextInt(arrayList.size());
        bg.setBackgroundResource(arrayList.get(vitri));

        // set changing image when clicking button
        ImageButton img2 = (ImageButton)
                findViewById(R.id.imageButton1);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.setImageResource(R.drawable.dart);
                img1.getLayoutParams().width=550;
                img1.getLayoutParams().height=550;
            }
        });

        //switch
        Switch sw = (Switch) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){ //isChecked = true
                    Toast.makeText(MainActivity.this,"Wifi đang bật",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this,"Wifi đang tắt",Toast.LENGTH_LONG).show();
                }
            }
        });

        // checkbox
        CheckBox ck1 = (CheckBox) findViewById(R.id.checkBox2);
        ck1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
               {
                   if(isChecked){
                       bg.setBackgroundResource(R.drawable.bg3);
                   }else{
                       bg.setBackgroundResource(R.drawable.bg4);
                   }
               }
           });
        //RadioGroup
        RadioGroup radioGroup = (RadioGroup)
                findViewById(R.id.radioGroup1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup group,
                                       int checkedId) {
            //checkID trả về ID radio
              if (checkedId == R.id.radioButton) {
                  bg.setBackgroundResource(R.drawable.bg3);
              } else if (checkedId == R.id.radioButton2) {
                  bg.setBackgroundResource(R.drawable.bg4);
              } else {
                  bg.setBackgroundResource(R.drawable.default_bg); // Nếu không có radio nào phù hợp
              }
          }
      });

        //progrebar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tự đếm progress
                CountDownTimer countDownTimer = new CountDownTimer(10000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int current = progressBar.getProgress();
                        //chạy đều đều >100 quay về 0
                        if (current>= progressBar.getMax()){
                            current=0;
                        }
                        progressBar.setProgress(current + 10); //thiết lập progress
                    }
                    @Override
                    public void onFinish() {
                        Toast.makeText(MainActivity.this,"Hết giờ",Toast.LENGTH_LONG).show();
                    }
                };
                countDownTimer.start();
            }
        });

        //Seekbar
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //progress: giá trị của seekbar
                Log.d("AAA","Giá trị:" + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("AAA","Start");
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("AAA","Stop");
            }});
    }
}