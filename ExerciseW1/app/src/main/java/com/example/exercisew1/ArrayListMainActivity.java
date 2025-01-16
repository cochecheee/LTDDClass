package com.example.exercisew1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ArrayListMainActivity extends AppCompatActivity {
    private static final String TAG = "ArrayListMainActivity";
    private ArrayList<Integer> numbers;

    private EditText inputEditText;
    private Button processButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_array_list_main);

        // Ánh xạ các view
        inputEditText = (EditText) findViewById(R.id.arrayInput);
        processButton = (Button) findViewById(R.id.processButton);

        numbers = new ArrayList<>();

        processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy chuỗi từ EditText và xử lý
                String input = inputEditText.getText().toString().trim();
                parseInputAndProcessNumbers(input);
            }
        });
    }

    // Hàm xử lý chuỗi input và tìm số nguyên tố
    private void parseInputAndProcessNumbers(String input) {
        numbers.clear(); // Xóa dữ liệu cũ

        // Tách chuỗi thành mảng các số
        String[] numberStrings = input.split("\\s+");

        // Chuyển đổi từng phần tử thành số và thêm vào ArrayList
        try {
            for (String numStr : numberStrings) {
                int num = Integer.parseInt(numStr);
                numbers.add(num);
            }
            // In ra các số nguyên tố
            printPrimeNumbers(numbers);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập các số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm kiểm tra số nguyên tố
    private boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    // Hàm in ra các số nguyên tố từ ArrayList
    private void printPrimeNumbers(ArrayList<Integer> list) {
        StringBuilder result = new StringBuilder("Các số nguyên tố: ");

        for (Integer num : list) {
            if (isPrime(num)) {
                Log.d(TAG, "Số nguyên tố: " + num);
                result.append(num).append(" ");
            }
        }
    }
}