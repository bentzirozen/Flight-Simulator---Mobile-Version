package com.example.bentz.simulator_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.OutputStream;

public class JoysticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Connection tcp = new Connection();
        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip_text");
        int port = Integer.parseInt(intent.getStringExtra("port_text"));
        tcp.connect(ip,port);
        tcp.sendCommands(1,2);

    }
}
