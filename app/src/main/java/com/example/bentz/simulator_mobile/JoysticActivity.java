package com.example.bentz.simulator_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.OutputStream;

public class JoysticActivity extends AppCompatActivity {
    private Connection tcp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip_text");
        int port = Integer.parseInt(intent.getStringExtra("port_text"));
        try {
            this.tcp = new Connection();
            this.tcp.connect(ip, port);
        }catch(Exception e) {
            System.out.println(e.toString());
        }
        JoystickView joyStrickView = new JoystickView(this,tcp);
        setContentView(joyStrickView);
    }
    public void onDestroy() {
        this.tcp.close();
        super.onDestroy();
    }
}
