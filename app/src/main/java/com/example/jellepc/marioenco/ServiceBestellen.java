package com.example.jellepc.marioenco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class ServiceBestellen extends Activity {
    private String servicenaam;
    private String ip;
    Button Annuleerknop;
    private int port = 4444;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_bestellen);
        servicenaam = InformatieFragment.servicenaam;
        ip = HomeFragment.serverIp;

        final TextView serviceNaam = (TextView) findViewById(R.id.serviceNaam);
        serviceNaam.setText(servicenaam);

        final TextView koperNaam = (TextView) findViewById(R.id.naamVeld);
        final TextView koperAdres = (TextView) findViewById(R.id.adresVeld);
        final TextView koperTelefoon = (TextView) findViewById(R.id.telefoonVeld);
        final TextView koperEmail = (TextView) findViewById(R.id.emailVeld);

        Button infoknop = (Button) findViewById(R.id.bestelBevestigen);
        infoknop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String naam = koperNaam.getText().toString();
                String adres = koperAdres.getText().toString();
                String telefoon = koperTelefoon.getText().toString();
                String email = koperEmail.getText().toString();



                JSONObject bestelling = new JSONObject();
                JSONObject service = new JSONObject();
                JSONObject gegevens = new JSONObject();
                JSONArray bestelArray = new JSONArray();

                try{
                    service.put("aanvraag:", servicenaam);
                    gegevens.put("kopernaam", naam);
                    gegevens.put("koperadres", adres);
                    gegevens.put("kopertelnr", telefoon);
                    gegevens.put("koperemail", email);

                    bestelArray.put(service);
                    bestelArray.put(gegevens);

                    bestelling.put("aanvraag:", bestelArray);

                }
                catch(JSONException e)
                {

                }


                String response = null;
                try {
                    try {
                        // Dit IP adres moet IP adres van server zijn.
                        response = new Server(ip,
                                port, bestelling.toString()).execute().get();

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

            }
        });

        Annuleerknop = (Button) findViewById(R.id.annuleerBestelling);
        Annuleerknop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(ServiceBestellen.this, InformatieFragment.class);

                startActivity(i);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_service_bestellen, menu);
        return true;
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