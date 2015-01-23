package com.example.jellepc.marioenco;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class InformatieFragment extends Fragment {
    public String ip;
    public int port;
    public static String servicenaam;
    private String informatie;

    View rootview;

    public static Fragment fragmenthome = new HomeFragment();
    public static Fragment fragmentservice = new ServiceFragment();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.informatie_layout, container, false);

        ip = HomeFragment.serverIp;
        port = HomeFragment.serverPort;
        servicenaam = HomeFragment.servicenaam;
        getActivity().setTitle("Informatie over "+servicenaam);

        getServiceInformatie();

        TextView infoServiceNaam = (TextView) rootview.findViewById(R.id.infoServiceNaam);
        infoServiceNaam.setText(servicenaam);
        TextView InfoVeld = (TextView) rootview.findViewById(R.id.infoVeld);
        InfoVeld.setText(informatie);

        buttonHandler();

        return rootview;
    }

    //Ophalen van uitgebreide informatie per dienst
    public void getServiceInformatie() {
        JSONObject infoObject = new JSONObject();
        try {
            infoObject.put("informatie", servicenaam);
        } catch (JSONException e) {

        }
        try {
            try {
                informatie = new Server(ip,
                        port, infoObject.toString()).execute().get();
            //afvangen exceptions
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

    //Handelt buttons af
    public void buttonHandler()
    {
        Button annuleerKnop = (Button) rootview.findViewById(R.id.annuleerKnop);
        annuleerKnop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragmenthome)
                        .commit();
            }
        });

        Button bestelKnop = (Button) rootview.findViewById(R.id.bestelBevestigen);
        bestelKnop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragmentservice)
                        .commit();
            }
        });

    }
}