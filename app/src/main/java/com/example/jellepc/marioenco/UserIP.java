package com.example.jellepc.marioenco;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class UserIP extends Activity {
    private Boolean serverCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Mario&Co");

        //Check voor internet verbinding
        if(CheckNetwork.isInternetAvailable(UserIP.this))
        {
        }
        else
        {
            Toast.makeText(UserIP.this,"Geen internet verbinding, laatst opgehaalde informatie wordt getoond!",Toast.LENGTH_LONG).show();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_invoeren);

        Button ipButton = (Button) findViewById(R.id.infoknop);
        ipButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                checkServer();
            }

        });

        //Enter key afvangen
        EditText ipInvoer = (EditText) findViewById(R.id.ipInvoer);
        ipInvoer.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                switch(keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        checkServer();
                        break;

                    default:
                        return false;
                }

                return true;

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
            Toast.makeText(this, "Verbinden met server mislukt, staat server aan?", Toast.LENGTH_LONG).show();

        } else {
            serverCheck = true;
            HomeFragment.serverIp = ip;
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
