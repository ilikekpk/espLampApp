package nikazzz.esplampapp;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    TextView txt;
    NsdUtils mNsdUtils;
    static InetAddress IP;
    static int PORT;
    static boolean LAMPONLINE = false;
    private UDP_Client Client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.text);
        mNsdUtils = new NsdUtils(this);
        mNsdUtils.initializeNsd();
        mNsdUtils.discoverServices();
        Client = new UDP_Client();

        final SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);



    }
    public void btnCl(View view){
        txt.setText(IP.getHostAddress() + ": " + PORT);

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (LAMPONLINE){
            Client.Message = String.valueOf(seekBar.getProgress());
            Client.NachrichtSenden();
            txt.setText(String.valueOf(seekBar.getProgress()));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}


