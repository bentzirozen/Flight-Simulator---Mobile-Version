package com.example.bentz.simulator_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.OutputStream;

public class JoysticActivity extends AppCompatActivity implements JoystickView.JoystickListener {
    private Connection tcp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip_text");
        String port = intent.getStringExtra("port_text");
        int port1 = Integer.parseInt(intent.getStringExtra("port_text"));
        try {
            this.tcp = new Connection();
            this.tcp.execute(ip, port);
        }catch(Exception e) {
            System.out.println(e.toString());
        }
        JoystickView joyStrickView = new JoystickView(this);
        setContentView(joyStrickView);
    }
    public void onDestroy() {
        this.tcp.close();
        super.onDestroy();
    }
    //set the aileron and the elevator when the joystick is moved.
    @Override
    public void onJoystickMoved(float x, float y) {
        tcp.sendCommands("set /controls/flight/aileron " + String.valueOf(x) + "\r\n");
        tcp.sendCommands("set /controls/flight/elevator "+ String.valueOf(y) + "\r\n");

    }
}
