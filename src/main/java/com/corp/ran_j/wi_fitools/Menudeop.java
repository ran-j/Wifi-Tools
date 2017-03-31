package com.corp.ran_j.wi_fitools;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.corp.ran_j.wi_fitools.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class Menudeop extends AppCompatActivity {
    ConnectivityManager conectivtyManager;
    ConnectivityManager conectivtyManager2;
    protected Context context;
    NetworkInfo netInfo;
    WifiManager mainWifi;
    WifiInfo info;
    DhcpInfo dh;
    int vai = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toob2);

        //botao de voltar da toolbar
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        ArrayList<String> values = new ArrayList<String>();
        ArrayList<String> values2 = new ArrayList<String>();

        values.add("Retronar IP");
        values.add("DNS Primario e secundario");
        values.add("Abrir pagina do Roteador");
        values.add("Frequência da Rede");
        values.add("Mascara de Rede");

        values2.add("mostra o ip local do celular na rede");
        values2.add("mostar o DNS primario e secundario do celular");
        values2.add("abre a pagina do gatway paadrão");
        values2.add("mostar a frequencia da rede");
        values2.add("mostar a mascara de rede");

        //setando os adpters
        MyAdapter mAdapter = new MyAdapter(values, values2);
        recyclerView.setAdapter(mAdapter);


        //pega o ssdi salvo na varialvel global
        String ssdi = Variaveis.wifiname;

        //seta nome do ssdi no titulo
        if (!(ssdi == null)) {
            toolbar.setTitle("Conectado ao " + ssdi);
        } else {
            toolbar.setTitle("Não foi possivel pegar o SSDI");
        }

        new Thread(new Task()).start();

    }

    class Task implements Runnable {
        @Override
        public void run() {

            while (vai == 0) {

                if (Variaveis.modo == 1) {
                    Variaveis.modo = 0;
                    dados(3);
                } else if (Variaveis.modo == 2) {
                    Variaveis.modo = 0;
                    dados(1);
                } else if (Variaveis.modo == 3) {
                    Variaveis.modo = 0;
                    dados(5);
                } else if (Variaveis.modo == 4) {
                    Variaveis.modo = 0;
                    dados(4);
                } else if (Variaveis.modo == 5) {
                    Variaveis.modo = 0;
                    //dados(2);
                    dados(6);

                }
            }
        }
    }


        public void dados(final int op) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    netInfo = conectivtyManager.getActiveNetworkInfo();
                    info = mainWifi.getConnectionInfo();
                    dh = mainWifi.getDhcpInfo();
                    List<ScanResult> networkList = mainWifi.getScanResults();

                    if (!(netInfo == null)) {
                        if (op == 1) {
                            Toast.makeText(Menudeop.this, "IP: " + intToIp(dh.ipAddress), Toast.LENGTH_LONG).show();
                        } else if (op == 2) {
                            Toast.makeText(Menudeop.this, "Gatway: " + intToIp(dh.gateway), Toast.LENGTH_LONG).show();
                        } else if (op == 3) {
                            Toast.makeText(Menudeop.this, "DNS 1: " + intToIp(dh.dns1) + ", DNS 2: " + intToIp(dh.dns2), Toast.LENGTH_LONG).show();
                        } else if (op == 4) {
                            Toast.makeText(Menudeop.this, "Masacara de rede: " + intToIp(dh.netmask), Toast.LENGTH_LONG).show();
                        } else if (op == 5){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Toast.makeText(Menudeop.this, String.format("Frequência: %d", info.getFrequency()), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Menudeop.this, "Não disponivel", Toast.LENGTH_LONG).show();
                            }
                        }else if (op == 6) {
                            WifiInfo wi = mainWifi.getConnectionInfo();
                            String currentSSID = wi.getSSID();
                            Toast.makeText(Menudeop.this, currentSSID, Toast.LENGTH_LONG).show();

                            if (networkList != null) {
                                for (ScanResult network : networkList) {
                                    //check if current connected SSID
                                    if (currentSSID.equals(network.SSID)) {
                                        //get capabilities of current connection
                                        String Capabilities = network.capabilities;
                                        if (Capabilities.contains("WPA2")) {
                                            Toast.makeText(Menudeop.this, "WPA2", Toast.LENGTH_LONG).show();
                                        } else if (Capabilities.contains("WPA")) {
                                            Toast.makeText(Menudeop.this, "WPA", Toast.LENGTH_LONG).show();
                                        } else if (Capabilities.contains("WEP")) {
                                            Toast.makeText(Menudeop.this, "WEP", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                            }
                        }
                    } else {
                        //chamar outra act
                        Toast.makeText(Menudeop.this, "Wi-fi desconectado", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Menudeop.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }

        public String intToIp(int addr) {
            return ((addr & 0xFF) + "." +
                    ((addr >>>= 8) & 0xFF) + "." +
                    ((addr >>>= 8) & 0xFF) + "." +
                    ((addr >>>= 8) & 0xFF));
        }

}
