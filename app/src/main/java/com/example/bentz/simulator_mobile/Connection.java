package com.example.bentz.simulator_mobile;
import android.app.NotificationManager;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
public class Connection {
    private Socket socket;
    private OutputStream outputStream;
    private List<File> allFiles;

    public void connect(String ip,int port) {
        try {
            InetAddress serverAddr = InetAddress.getByName(ip);
            socket = new Socket(serverAddr, port);
            try {
                outputStream = socket.getOutputStream();
            } catch (Exception e) {
                Log.e("TCP", "S: Error:", e);
            }
        } catch (Exception e) {
            Log.e("TCP", "S: Error:", e);
        }

    }








}
