package com.example.usbmaster;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UsbStorageManager {
    private static final String TAG = "UsbStorageManager";
    private Context context;

    public interface ProgressCallback {
        void onProgress(int progress);
    }

    public UsbStorageManager(Context context) {
        this.context = context;
    }

    /**
     * Fills the remaining storage space with a specific pattern (FF or 00).
     * Uses smaller chunks to avoid memory issues.
     */
    public void fillStorageWithPattern(File directory, byte pattern, ProgressCallback callback) throws IOException {
        long freeSpace = directory.getFreeSpace();
        if (freeSpace <= 0) {
            Log.d(TAG, "No free space available");
            return;
        }

        byte[] buffer = new byte[1024 * 1024]; // 1MB buffer
        
        // Fill buffer with pattern
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = pattern;
        }

        int fileIndex = 0;
        long written = 0;
        long lastProgressUpdate = 0;

        while (written < freeSpace) {
            File fillFile = new File(directory, "fill_" + String.format("%02X", pattern) + "_" + fileIndex + ".bin");
            
            try (FileOutputStream fos = new FileOutputStream(fillFile)) {
                int bytesWritten = 0;
                while (bytesWritten < 5 * 1024 * 1024 && written < freeSpace) { // 5MB per file
                    int toWrite = (int) Math.min(buffer.length, freeSpace - written);
                    fos.write(buffer, 0, toWrite);
                    written += toWrite;
                    bytesWritten += toWrite;
                    
                    // Update progress every 10MB
                    if (written - lastProgressUpdate >= 10 * 1024 * 1024) {
                        int progress = (int) ((written * 100) / freeSpace);
                        if (callback != null) {
                            callback.onProgress(Math.min(progress, 99));
                        }
                        lastProgressUpdate = written;
                    }
                }
            } catch (IOException e) {
                // Storage full or write error, stop
                Log.d(TAG, "Storage full or write error: " + e.getMessage());
                break;
            }
            
            fileIndex++;
        }

        if (callback != null) {
            callback.onProgress(100);
        }
        
        Log.d(TAG, "Fill operation complete. Written: " + written + " bytes");
    }

    /**
     * Wipes the storage by deleting all files and directories.
     */
    public void wipeStorage(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    recursiveDelete(file);
                }
            }
        }
    }

    private void recursiveDelete(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    recursiveDelete(child);
                }
            }
        }
        boolean deleted = file.delete();
        if (!deleted) {
            Log.w(TAG, "Failed to delete: " + file.getAbsolutePath());
        }
    }

    /**
     * Requests format operation (system-level).
     */
    public void requestFormat(File path, boolean isExFat) {
        Log.d(TAG, "Requesting format for: " + path.getAbsolutePath() + " to " + (isExFat ? "exFAT" : "FAT32"));
    }
}
