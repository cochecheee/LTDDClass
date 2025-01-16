package com.example.exercisew1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PerfectSquareMainActivity extends AppCompatActivity {
    private EditText edtArraySize;
    private EditText edtNumbers;
    private Button btnProcess;
    private TextView tvResult;
    private ArrayList<Integer> numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_square_main);
        // Ánh xạ các view
        bindingView();
        numbers = new ArrayList<>();
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processInput();
            }
        });
    }
    private void processInput() {
        try {
            // Lấy số phần tử từ EditText
            int size = Integer.parseInt(edtArraySize.getText().toString().trim());

            // Lấy chuỗi số từ EditText
            String input = edtNumbers.getText().toString().trim();
            String[] numberStrings = input.split("\\s+");

            // Kiểm tra số lượng phần tử nhập vào
            if (numberStrings.length != size) {
                Toast.makeText(this, "Vui lòng nhập đúng " + size + " số", Toast.LENGTH_SHORT).show();
                return;
            }
            // Xóa dữ liệu cũ trong ArrayList
            numbers.clear();
            // Chuyển đổi và thêm các số vào ArrayList
            for (String numStr : numberStrings) {
                numbers.add(Integer.parseInt(numStr));
            }
            // Tìm và hiển thị các số chính phương
            findPerfectSquares();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
    // Kiểm tra số chính phương
    private boolean isPerfectSquare(int number) {
        if (number < 0) return false;
        int sqrt = (int) Math.sqrt(number);
        return sqrt * sqrt == number;
    }
    // Tìm và hiển thị các số chính phương
    private void findPerfectSquares() {
        StringBuilder result = new StringBuilder("Các số chính phương: ");
        StringBuilder toastMessage = new StringBuilder();

        for (Integer num : numbers) {
            if (isPerfectSquare(num)) {
                result.append(num).append(" ");
                toastMessage.append(num).append(" ");
            }
        }
        // Hiển thị kết quả trên TextView
        tvResult.setText(result.toString());
        // Hiển thị Toast
        if (toastMessage.length() > 0) {
            Toast.makeText(this, "Số chính phương: " + toastMessage.toString(),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Không có số chính phương",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void bindingView() {
        edtArraySize = (EditText) findViewById(R.id.edtArraySize);
        edtNumbers = (EditText) findViewById(R.id.edtNumbers);
        btnProcess = (Button) findViewById(R.id.btnProcess);
        tvResult = (TextView) findViewById(R.id.tvResult);
    }
}