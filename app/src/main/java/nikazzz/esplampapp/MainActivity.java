package nikazzz.esplampapp;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    View colorView;
    TextView connectTxt;
    NsdUtils mNsdUtils;
    static InetAddress IP;
    static int PORT;
    static boolean LAMPONLINE = false;
    private UDP_Client Client;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colorView = findViewById(R.id.colorView);
        connectTxt = findViewById(R.id.connectTxt);
        connectTxt.setTextColor(0xFFFF0000);

        connectTxt.setText("Disconnected");

        mNsdUtils = new NsdUtils(this);
        mNsdUtils.initializeNsd();
        mNsdUtils.discoverServices();
        Client = new UDP_Client();

        redSeekBar = findViewById(R.id.redSeekBar);
        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar = findViewById(R.id.greenSeekBar);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar = findViewById(R.id.blueSeekBar);
        blueSeekBar.setOnSeekBarChangeListener(this);



    }
    public void connectBtnOnClick(View view){
        if (LAMPONLINE) {

            connectTxt.setText("Connected");
            connectTxt.setTextColor(0xFF00FF00);
        }
        else{
            connectTxt.setTextColor(0xFFFF0000);
            connectTxt.setText("Disconnected");
        }

    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (LAMPONLINE){
            int red = redSeekBar.getProgress();
            int green = greenSeekBar.getProgress();
            int blue = blueSeekBar.getProgress();
            colorView.setBackgroundColor(Color.rgb(red, green, blue));
            Client.Message = String.valueOf(seekBar.getProgress());
            Client.NachrichtSenden(IP, 9876);
            connectTxt.setText(String.valueOf(seekBar.getProgress()));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}


