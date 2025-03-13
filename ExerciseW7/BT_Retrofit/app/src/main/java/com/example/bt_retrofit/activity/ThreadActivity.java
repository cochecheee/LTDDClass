package com.example.bt_retrofit.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bt_retrofit.MainActivity;
import com.example.bt_retrofit.R;

public class ThreadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thread);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                int n = 0;
                try {
                    do {
                        if (n >= 2000) {
                            ThreadActivity.this.finish();
                            Intent intent = new Intent(ThreadActivity.this.getApplicationContext(), MainActivity.class);
                            ThreadActivity.this.startActivity(intent);
                            return;
                        }
                        Thread.sleep(100);
                        n += 100;
                    } while (true);
                } catch (InterruptedException interruptedException) {
                    ThreadActivity.this.finish();
                    Intent intent = new Intent(ThreadActivity.this.getApplicationContext(), MainActivity.class);
                    ThreadActivity.this.startActivity(intent);
                } catch (Throwable throwable) {
                    ThreadActivity.this.finish();
                    Intent intent = new Intent(ThreadActivity.this.getApplicationContext(), MainActivity.class);
                    ThreadActivity.this.startActivity(intent);
                    throw throwable;
                }
            }
        }).start();
    }
}