package nikazzz.esplampapp;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView txt;
    NsdUtils mNsdUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.text);
        Button btn = findViewById(R.id.btn);
        mNsdUtils = new NsdUtils(this);
        mNsdUtils.initializeNsd();
        mNsdUtils.discoverServices();

    }
    public void btnCl(View view){

    }
}




