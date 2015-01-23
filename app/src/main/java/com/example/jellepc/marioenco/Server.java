package com.example.jellepc.marioenco;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public class Server extends AsyncTask<Void, Void, String>{

    private String message;
    private String ip;
    public static int port = 4444;
    private String serverResponse = null;

    public Server(String ip, int port, String message ) {
        super();
        //IP, Port en bericht om naar server te sturen
        this.message = message;
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            Socket serverSocket = new Socket();
            serverSocket.connect(new InetSocketAddress(this.ip, this.port), 4444);

            this.sendMessage(message, serverSocket);

            InputStream input;

            try {
                input = serverSocket.getInputStream();
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(input));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                responseStreamReader.close();

                this.serverResponse = stringBuilder.toString();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            System.out.println("Response: " + serverResponse);

        } catch (UnknownHostException e) {
            Log.d("debug", "can't find host");
        } catch (SocketTimeoutException e) {
            Log.d("debug", "time-out");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    private void sendMessage(String message, Socket serverSocket) {
        OutputStreamWriter outputStreamWriter = null;

        try {
            outputStreamWriter = new OutputStreamWriter(serverSocket.getOutputStream());
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        if (outputStreamWriter != null) {
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            PrintWriter writer = new PrintWriter(bufferedWriter, true);

            writer.println(message);
        }
    }
}
