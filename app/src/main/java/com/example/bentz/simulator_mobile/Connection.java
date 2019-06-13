package com.example.bentz.simulator_mobile;
import android.app.NotificationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Connection extends AsyncTask<String,String,Void> {
    private Socket socket;
    private DataOutputStream outputStream;
    private BlockingQueue<String> queue = new LinkedBlockingDeque<>();
    public Connection(){}

    @Override
    protected Void doInBackground(String... strings) {
        String ip = strings[0];
        int port = Integer.parseInt(strings[1]);
        try {
            socket = new Socket(ip, port);
            outputStream = new DataOutputStream(socket.getOutputStream());
            while (true) {
                outputStream.write((queue.take()).getBytes());
                outputStream.flush();
            }
        } catch (Exception e) {
            Log.e("TCP", "the create socket fail:", e);
        }
        return null;
    }

    public void connect(final String ip,final int port) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(ip, port);
                    outputStream = new DataOutputStream(socket.getOutputStream());
                    while (true) {
                        outputStream.write((queue.take()).getBytes());
                        outputStream.flush();
                    }
                } catch (Exception e) {
                    Log.e("TCP", "the create socket fail:", e);
                }
            }
        });
        thread.start();
    }
    public void sendCommands(String str) {
        try {
            this.queue.put(str);
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void close() {
        try {
            this.socket.close();
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }
}
