package nikazzz.esplampapp;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    View colorView;
    NsdUtils mNsdUtils;
    private UDP_Client Client;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;
    private TextView redTextView;
    private TextView greenTextView;
    private TextView blueTextView;
    private View redView;
    private View greenView;
    private View blueView;
    ProgressDialog progressDialog;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        redTextView = findViewById(R.id.redTextView);
        greenTextView = findViewById(R.id.greenTextView);
        blueTextView = findViewById(R.id.blueTextView);

        redView = findViewById(R.id.redView);
        redView.setBackgroundColor(Color.RED);
        greenView = findViewById(R.id.greenView);
        greenView.setBackgroundColor(Color.GREEN);
        blueView = findViewById(R.id.blueView);
        blueView.setBackgroundColor(Color.BLUE);
        colorView = findViewById(R.id.colorView);
        colorView.setBackgroundColor(Color.WHITE);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int red = redSeekBar.getProgress();
        int green = greenSeekBar.getProgress();
        int blue = blueSeekBar.getProgress();

        colorView.setBackgroundColor(Color.rgb(red, green, blue));
        redView.setBackgroundColor(Color.rgb(red, 0, 0));
        greenView.setBackgroundColor(Color.rgb(0, green, 0));
        blueView.setBackgroundColor(Color.rgb(0, 0, blue));

        redTextView.setText(String.valueOf(red));
        greenTextView.setText(String.valueOf(green));
        blueTextView.setText(String.valueOf(blue));

        Client.Message = String.valueOf(Color.rgb(red, green, blue));
        Client.NachrichtSenden(mNsdUtils.IP, mNsdUtils.PORT);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public class NsdUtils{
        private Context mContext;

        private NsdManager mNsdManager;
        NsdManager.ResolveListener mResolveListener;
        NsdManager.DiscoveryListener mDiscoveryListener;

        static final String SERVICE_TYPE = "_http._tcp.";
        static final String TAG = "NsdHelper";
        static final String mServiceName = "esp8266";

        InetAddress IP;
        int PORT;

        private NsdServiceInfo mService;

        NsdUtils(Context context) {
            mContext = context;
            mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        }

        public void initializeNsd() {
            initializeResolveListener();
            initializeDiscoveryListener();
        }

        private void initializeDiscoveryListener() {
            mDiscoveryListener = new NsdManager.DiscoveryListener() {

                @Override
                public void onDiscoveryStarted(String regType) {
                    Log.d(TAG, "Service discovery started");
                    loadingScreen(true);
                }

                @Override
                public void onServiceFound(NsdServiceInfo service) {
                    Log.d(TAG, "Service discovery success: " + service);
                    if (!service.getServiceType().equals(SERVICE_TYPE)) {
                        Log.d(TAG, "Unknown Service Type: " + service.getServiceType());

                    } else if (service.getServiceName().equals(mServiceName) || service.getServiceName().contains(mServiceName)) {
                        Log.d(TAG, "Same machine: " + mServiceName + " ");
                        mNsdManager.resolveService(service, mResolveListener);

                    }
                }

                @Override
                public void onServiceLost(NsdServiceInfo service) {
                    Log.e(TAG, "service lost" + service);
                    if (mService == service) {
                        mService = null;
                    }
                    loadingScreen(true);
                }

                @Override
                public void onDiscoveryStopped(String serviceType) {
                    Log.i(TAG, "Discovery stopped: " + serviceType);
                }

                @Override
                public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                    Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                    mNsdManager.stopServiceDiscovery(this);
                }

                @Override
                public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                    Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                    mNsdManager.stopServiceDiscovery(this);
                }
            };
        }

        public void initializeResolveListener() {
            mResolveListener = new NsdManager.ResolveListener() {

                @Override
                public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                    Log.e(TAG, "Resolve failed" + errorCode);
                }

                @Override
                public void onServiceResolved(NsdServiceInfo serviceInfo) {
                    Log.e(TAG, "Resolve Succeeded. " + serviceInfo);
                    if (serviceInfo.getServiceName().equals(mServiceName)) {
                        Log.d(TAG, "Same IP.");
                        IP = serviceInfo.getHost();
                        PORT = serviceInfo.getPort();
                        loadingScreen(false);
                        return;
                    }
                    mService = serviceInfo;
                }

            };
        }
        public void discoverServices() {
            mNsdManager.discoverServices(
                    SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
        }
    }

    public void loadingScreen(final boolean b){
        progressBar = findViewById(R.id.progressBar);
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(b){
                    redSeekBar.setVisibility(View.INVISIBLE);
                    greenSeekBar.setVisibility(View.INVISIBLE);
                    blueSeekBar.setVisibility(View.INVISIBLE);

                    redTextView.setVisibility(View.INVISIBLE);
                    greenTextView.setVisibility(View.INVISIBLE);
                    blueTextView.setVisibility(View.INVISIBLE);

                    redView.setVisibility(View.INVISIBLE);
                    greenView.setVisibility(View.INVISIBLE);
                    blueView.setVisibility(View.INVISIBLE);
                    colorView.setVisibility(View.INVISIBLE);

                    progressBar.setVisibility(View.VISIBLE);
                }
                else{
                    redSeekBar.setVisibility(View.VISIBLE);
                    greenSeekBar.setVisibility(View.VISIBLE);
                    blueSeekBar.setVisibility(View.VISIBLE);

                    redTextView.setVisibility(View.VISIBLE);
                    greenTextView.setVisibility(View.VISIBLE);
                    blueTextView.setVisibility(View.VISIBLE);

                    redView.setVisibility(View.VISIBLE);
                    greenView.setVisibility(View.VISIBLE);
                    blueView.setVisibility(View.VISIBLE);
                    colorView.setVisibility(View.VISIBLE);

                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        });

    }

}


