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
    public Connection(){};
    public void connect(final String ip,final int port){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(ip, port);
                    try {
                        outputStream = socket.getOutputStream();
                    } catch (Exception e) {
                        Log.e("TCP", "the socket not connect:", e);
                    }
                } catch (Exception e) {
                    Log.e("TCP", "the create socket fail:", e);
                }
            }
        });
        thread.start();
    }
    public void sendCommands(final float x, final float y){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String aileronPath = " /controls/flight/aileron ";
                String elevatiorPath = " /controls/flight/elevator ";
                String aileronCommand = "set" + aileronPath + String.valueOf(x) + "\r\n";
                String elevatorCommand = "set " + elevatiorPath + String.valueOf(y) + "\r\n";
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    String sendToClient = "Hey, my name is Server007 B)";
                    out.println(sendToClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }


    public OutputStream getOutputSteam() {
        return outputStream;
    }
}
