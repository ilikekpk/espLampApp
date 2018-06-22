package nikazzz.esplampapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP_Client {
    private AsyncTask<Void, Void, Void> async_cient;
    public String Message;

    @SuppressLint({"NewApi", "StaticFieldLeak"})
    public void NachrichtSenden(final InetAddress ip, final int port) {
        async_cient = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                DatagramSocket ds = null;

                try {
                    ds = new DatagramSocket();
                    DatagramPacket dp;
                    InetAddress local = InetAddress.getByName(MainActivity.IP.getHostAddress());
                    dp = new DatagramPacket(Message.getBytes(), Message.length(), local, MainActivity.PORT);
                    //ds.setBroadcast(true);
                    ds.connect(ip, port);
                    ds.send(dp);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (ds != null) {
                        ds.close();
                    }
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
            }
        };

        if (Build.VERSION.SDK_INT >= 11)
            async_cient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else async_cient.execute();
    }
}
