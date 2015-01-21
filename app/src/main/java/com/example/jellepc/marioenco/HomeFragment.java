package com.example.jellepc.marioenco;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {

    public static String serverIp;
    public static int serverPort = 4444;
    public static ArrayList<String> serviceLijst;
    public static ArrayList<JSONObject> beknopteInformatielijst;
    public static String informatiebeknopt = null;
    private static View rootview;
    private Spinner service_spinner;
    public static String servicenaam;
    public static Boolean eersteVerbinding = true;
    public static int geselecteerdeDienst;
    public static int selectedPosition;

    public static Fragment fragmentinformatie = new InformatieFragment();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.home_layout, container, false);
        if(eersteVerbinding == true) {
            dataOphalen();
            dataInvullen();
        } else {
            dataInvullen();
            service_spinner.setSelection(geselecteerdeDienst);
        }
        return rootview;
    }


    public void dataOphalen() {

        //ophalen van de services
        serviceLijst = new ArrayList<String>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("servicelijst", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String response = null;
        try {
            try {
                // Dit IP adres moet IP adres van server zijn.
                response = new Server(serverIp,
                        serverPort, jsonObject.toString()).execute().get();

            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        if (response == null) {

            Toast.makeText(rootview.getContext(), "Kan geen verbinding maken met de server.", Toast.LENGTH_LONG).show();
        } else {
            // Haal de null naam weg van de JSONArray (Voorkomt error)
            String jsonFix = response.replace("null", "");

            JSONArray JArray = null;
            try {
                JArray = new JSONArray(jsonFix);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jObject = null;
            String value = null;
            serviceLijst = new ArrayList<String>();

            for (int i = 0; i < JArray.length(); i++) {
                try {
                    jObject = JArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    value = jObject.getString("naam");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                serviceLijst.add(value);

            }
            // haal beknopte informatie op
            beknopteInformatielijst = new ArrayList<JSONObject>();
            JSONObject beknoptjObject = new JSONObject();
            try {
                for (int i = 0; i < serviceLijst.size(); i++) {
                    beknoptjObject.put("informatiebeknopt", serviceLijst.get(i));
                    try {
                        try {
                            informatiebeknopt = new Server(serverIp,
                                    serverPort, beknoptjObject.toString()).execute().get();

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    String infoFix = informatiebeknopt.replace("null", "");
                    JSONObject fixedjObject = new JSONObject(infoFix);
                    beknopteInformatielijst.add(fixedjObject);

                    Log.i("informatiebeknopt", infoFix);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            eersteVerbinding = false;

        }


        // Locate the spinner in activity_main.xml
        service_spinner = (Spinner) rootview.findViewById(R.id.spinner);

        // Spinner adapter
        service_spinner
                .setAdapter(new ArrayAdapter<String>(rootview.getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        serviceLijst));

        // Spinner on item click listener
        service_spinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View arg1, int position, long arg3) {
                        // Locate the textviews in activity_main.xml
                        TextView beknopteinfo = (TextView) rootview.findViewById(R.id.textView4);

                        try {
                            // Set the text followed by the position
                            beknopteinfo.setText(beknopteInformatielijst.get(position).getString("informatiebeknopt"));
                            servicenaam = serviceLijst.get(position);

                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

    }

    private void dataInvullen() {
        // Locate the spinner in activity_main.xml
        service_spinner = (Spinner) rootview.findViewById(R.id.spinner);

        // Spinner adapter
        service_spinner
                .setAdapter(new ArrayAdapter<String>(rootview.getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        serviceLijst));

        // Spinner on item click listener
        service_spinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View arg1, int position, long arg3) {
                        // TODO Auto-generated method stub
                        // Locate the textviews in activity_main.xml
                        TextView beknopteinfo = (TextView) rootview.findViewById(R.id.textView4);

                        try {
                            // Set the text followed by the position
                            beknopteinfo.setText(beknopteInformatielijst.get(position).getString("informatiebeknopt"));
                            servicenaam = serviceLijst.get(position);

                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });


        Button infoknop = (Button) rootview.findViewById(R.id.infoknop);
        infoknop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedPosition = service_spinner.getSelectedItemPosition();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragmentinformatie)
                        .commit();
            }
        });

    }
}