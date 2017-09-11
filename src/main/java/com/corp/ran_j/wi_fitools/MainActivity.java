package com.corp.ran_j.wi_fitools;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    WifiManager mainWifi;
    TextView texto;
    int conectado=0;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        texto =(TextView) findViewById(R.id.textView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toob1);
        toolbar.setTitle("Wi-fi Tools");
        starttask();

    }

    public  void starttask(){
        conectado=0;
        new Thread(new Task()).start();
    }

    class Task implements Runnable {
        @Override
        public void run() {
            //aguarda uma conexão
            while(conectado==0){
                mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                if (!mainWifi.isWifiEnabled()) {
                    //se o wifi estiver desabilidado
                    settext("Wi-fi desconectado");
                }else {
                    //verifica se esta conectado a a algum wifi
                    settext("Abrindo menu de opções");
                    ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = conectivtyManager.getActiveNetworkInfo();
                    WifiInfo info = mainWifi.getConnectionInfo();
                    //verifica se esta conectado a um wifi
                    if (!(netInfo ==null)) {
                        int netType = netInfo.getType();
                        if (netType == ConnectivityManager.TYPE_WIFI) {
                            Variaveis.wifiname = info.getSSID();
                            conectado=1;
                            nextmenu();
                        }
                    }
                }
            }
        }
    }

    public void settext(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                texto.setText(message);
            }
        });
    }

    public void nextmenu(){
        //chamar outra act
        Intent intent = new Intent(MainActivity.this, Menudeop.class);
        startActivity(intent);
        finish();

     }

}
