package com.example.bentz.simulator_mobile;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText  ip = (EditText) findViewById(R.id.ip_text);
        final EditText port = (EditText) findViewById(R.id.port_text);
        Button connect_button = (Button) findViewById(R.id.connect_button);
        final Intent intent = new Intent(this,JoysticActivity.class);
        connect_button.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        String ip_text= ip.getText().toString();
                        String port_text = port.getText().toString();
                        intent.putExtra("ip_text",ip_text);
                        intent.putExtra("port_text",port_text);
                        startActivity(intent);
                    }
                });
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
