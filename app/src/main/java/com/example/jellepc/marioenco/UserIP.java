package com.example.jellepc.marioenco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class UserIP extends Activity {
    private Boolean serverCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Loodgieter");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_invoeren);

        Button ipButton = (Button) findViewById(R.id.ipKnop);
        ipButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                checkServer();
            }

        });


    }

    public void checkServer() {
        TextView ipVeld = (TextView) findViewById(R.id.ipInvoer);
        String ip = ipVeld.getText().toString();
        Log.i("ip", ip);

        String response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("servicelijst", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            try {
                response = new Server(ip,
                        4444, jsonObject.toString()).execute().get();

            } catch (InterruptedException e) {

            }
        } catch (ExecutionException e1) {

        }
        if (response == null) {
            serverCheck = false;
            Toast.makeText(this, "Server reageert niet", Toast.LENGTH_LONG).show();

        } else {
            serverCheck = true;
            HomeFragment.serverIp = ip;
            Toast.makeText(this, "Server succesvol verbonden", Toast.LENGTH_SHORT).show();
            Intent startApp = new Intent(this, MainActivity.class);
            startActivity(startApp);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ip_invoeren, menu);
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
