package com.example.jellepc.marioenco;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class InfoScherm extends Activity {
    public String ip;
    public int port;
    public static String servicenaam;
    private String informatie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info_scherm);

        ip = HomeFragment.serverIp;
        port = HomeFragment.serverPort;
        servicenaam = HomeFragment.servicenaam;
        getInformatie();
        setTitle(servicenaam);

        TextView infoServiceNaam = (TextView) findViewById(R.id.infoServiceNaam);
        infoServiceNaam.setText(servicenaam);
        TextView InfoVeld = (TextView) findViewById(R.id.infoVeld);
        InfoVeld.setText(informatie);

        Button bestelKnop = (Button) findViewById(R.id.bestelBevestigen);
        Button annuleerKnop = (Button) findViewById(R.id.annuleerKnop);

        annuleerKnop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(home);
            }
        });
        bestelKnop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent bestel = new Intent(getApplicationContext(), ServiceBestellen.class);

                startActivity(bestel);
            }
        });

        Log.d("Servicenaam", servicenaam);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info_scherm, menu);
        return true;
    }


    public void getInformatie()

    {
        JSONObject infoObject = new JSONObject();
        try {
            infoObject.put("informatie", servicenaam);
        } catch (JSONException e) {

        }
        try {
            try {
                informatie = new Server(ip,
                        port, infoObject.toString()).execute().get();

            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        String infoFix = informatie.replace("null", "");
        try {
            JSONObject fixedInfo = new JSONObject(infoFix);
            informatie = fixedInfo.getString("informatie");

        } catch (JSONException e) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}