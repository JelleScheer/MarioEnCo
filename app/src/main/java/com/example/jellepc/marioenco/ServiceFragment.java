package com.example.jellepc.marioenco;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutionException;

public class ServiceFragment extends Fragment {
    public String ip;
    public int port;
    public static String servicenaam;
    public String naam, adres, nummer, email;
    public TextView naamxml, adresxml, nummerxml, emailxml;
    public String beknopteinformatie;
    public String responseFix;

    View rootview;

    public static Fragment fragmentinformatie = new InformatieFragment();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.service_layout, container, false);

        ip = HomeFragment.serverIp;
        port = Server.port;

        servicenaam = HomeFragment.servicenaam;
        TextView infoServiceNaam = (TextView) rootview.findViewById(R.id.serviceNaam);
        infoServiceNaam.setText(servicenaam);

        getActivity().setTitle("Dienst "+servicenaam+" bestellen");

        final TextView serviceBeknopteinformatie = (TextView) rootview.findViewById(R.id.aanvraagBeknopteinformatie);
        try {
            serviceBeknopteinformatie.setText(HomeFragment.beknopteInformatielijst.get(HomeFragment.selectedPosition).getString("informatiebeknopt"));
        } catch (JSONException e) {

        }

        naamxml = (TextView) rootview.findViewById(R.id.naam);
        adresxml = (TextView) rootview.findViewById(R.id.adres);
        nummerxml = (TextView) rootview.findViewById(R.id.nummer);
        emailxml = (TextView) rootview.findViewById(R.id.email);

        buttonHandler();

        return rootview;
    }
    private void buttonHandler()
    {
        Button annuleerKnop = (Button) rootview.findViewById(R.id.annuleerBestelling);
        annuleerKnop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragmentinformatie)
                        .commit();
            }
        });

        Button bestelEnVerwerk = (Button) rootview.findViewById(R.id.bestelBevestigenEnVerwerken);
        if(CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
        {
            bestelEnVerwerk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    verzendBestelling();
                }
            });
        }
        else
        {
            Toast.makeText(getActivity().getApplicationContext(), "Geen internet, kan niet versturen!", Toast.LENGTH_LONG).show();
        }
    }

    private void verzendBestelling()
    {
        naam = naamxml.getText().toString();
        adres = adresxml.getText().toString();
        nummer = nummerxml.getText().toString();
        email = emailxml.getText().toString();

        JSONObject bestelling = new JSONObject();
        JSONObject service = new JSONObject();
        JSONObject gegevens = new JSONObject();
        JSONArray bestelArray = new JSONArray();

        try {
            service.put("servicenaam", servicenaam);
            gegevens.put("kopernaam", naam);
            gegevens.put("koperadres", adres);
            gegevens.put("kopertelnr", nummer);
            gegevens.put("koperemail", email);

            bestelArray.put(service);
            bestelArray.put(gegevens);

            bestelling.put("aanvraag", bestelArray);

        } catch (JSONException e) {

        }
        String response = null;

        try {
            try {
                response = new Server(ip,
                        port, bestelling.toString()).execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        if(response == null)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Geen verbinding mogelijk!", Toast.LENGTH_LONG).show();        }
        else{
            responseFix = response.replace("null", "");

            Toast.makeText(getActivity().getApplicationContext(), "Bestelling verzonden!", Toast.LENGTH_LONG).show();

        }
        naam = naamxml.getText().toString();
        adres = adresxml.getText().toString();
        nummer = nummerxml.getText().toString();
        email = emailxml.getText().toString();
    }
}