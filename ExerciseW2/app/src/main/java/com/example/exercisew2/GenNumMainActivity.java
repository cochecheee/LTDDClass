package com.example.exercisew2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class GenNumMainActivity extends AppCompatActivity {

    // 1. initialize view component
    private Button genNumBtn;
    private TextView showGenNumTxt;
    private EditText number1EditTxt;
    private EditText number2EditTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide header bar
        this.hideHeaderBar();
        // create View
        setContentView(R.layout.activity_gen_num_main);

        //binding
        this.bindingViewToActivity();

        //3. process button onClick event
        this.genNumBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 //tạo số ngẫu nhiên
                 Random random = new Random();
                 // take to number from input fields
                 Integer number1 = Integer.parseInt(number1EditTxt.getText().toString());
                 Integer number2 = Integer.parseInt(number2EditTxt.getText().toString());
                 // gen num
                 int number = random.nextInt((number2 - number1) + 1) + number1;
                 showGenNumTxt.setText(number + ""); //number + "" ép kiểu
             }
        });
    }

    private void hideHeaderBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        // this -> current instance (MainActivity)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //2. Binding view to Activity
    private void bindingViewToActivity() {
        this.genNumBtn = (Button) findViewById(R.id.buttonRnd);
        this.showGenNumTxt = (TextView) findViewById(R.id.textViewSo);
        this.number1EditTxt = (EditText) findViewById(R.id.editTextText);
        this.number2EditTxt = (EditText) findViewById(R.id.editTextText2);
    }
}