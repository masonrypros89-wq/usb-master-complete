package com.example.usbmaster;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private UsbStorageManager usbManager;
    private TextView statusText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usbManager = new UsbStorageManager(this);
        statusText = findViewById(R.id.status_text);
        progressBar = findViewById(R.id.progress_bar);

        Button btnFillFF = findViewById(R.id.btn_fill_ff);
        Button btnFill00 = findViewById(R.id.btn_fill_00);
        Button btnWipe = findViewById(R.id.btn_wipe);
        Button btnFat32 = findViewById(R.id.btn_format_fat32);
        Button btnExfat = findViewById(R.id.btn_format_exfat);

        btnFillFF.setOnClickListener(v -> handleFillFF());
        btnFill00.setOnClickListener(v -> handleFill00());
        btnWipe.setOnClickListener(v -> handleWipe());
        btnFat32.setOnClickListener(v -> handleFormat(false));
        btnExfat.setOnClickListener(v -> handleFormat(true));
    }

    private void handleFillFF() {
        new Thread(() -> {
            runOnUiThread(() -> {
                progressBar.setVisibility(View.VISIBLE);
                statusText.setText("Filling storage with FF pattern...");
            });

            try {
                File usbDir = getExternalFilesDir(null);
                if (usbDir != null) {
                    usbManager.fillStorageWithPattern(usbDir, (byte) 0xFF, (progress) -> {
                        runOnUiThread(() -> progressBar.setProgress(progress));
                    });
                    runOnUiThread(() -> Toast.makeText(this, "Storage Filled with FF", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            } finally {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    statusText.setText("USB Master - Idle");
                });
            }
        }).start();
    }

    private void handleFill00() {
        new Thread(() -> {
            runOnUiThread(() -> {
                progressBar.setVisibility(View.VISIBLE);
                statusText.setText("Filling storage with 00 pattern...");
            });

            try {
                File usbDir = getExternalFilesDir(null);
                if (usbDir != null) {
                    usbManager.fillStorageWithPattern(usbDir, (byte) 0x00, (progress) -> {
                        runOnUiThread(() -> progressBar.setProgress(progress));
                    });
                    runOnUiThread(() -> Toast.makeText(this, "Storage Filled with 00", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            } finally {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    statusText.setText("USB Master - Idle");
                });
            }
        }).start();
    }

    private void handleWipe() {
        new Thread(() -> {
            runOnUiThread(() -> {
                progressBar.setVisibility(View.VISIBLE);
                statusText.setText("Wiping storage...");
            });

            File usbDir = getExternalFilesDir(null);
            if (usbDir != null) {
                usbManager.wipeStorage(usbDir);
                runOnUiThread(() -> Toast.makeText(this, "Storage Wiped", Toast.LENGTH_SHORT).show());
            }

            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                statusText.setText("USB Master - Idle");
            });
        }).start();
    }

    private void handleFormat(boolean isExFat) {
        File usbDir = getExternalFilesDir(null);
        if (usbDir != null) {
            usbManager.requestFormat(usbDir, isExFat);
            Toast.makeText(this, "Format request sent for " + (isExFat ? "exFAT" : "FAT32"), Toast.LENGTH_SHORT).show();
        }
    }
}
