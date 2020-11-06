package com.example.soa_ea2.services;

import android.util.Log;
import com.example.soa_ea2.Constantes;
import com.example.soa_ea2.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RefreshToken extends Thread  {

    private boolean running;
    private boolean mPaused;
    private Object mPauseLock;
    public static RefreshToken instance = null;

    public static RefreshToken getInstance() {
        if(instance == null) {
            instance = new RefreshToken();
        }

        return instance;
    }

    private RefreshToken() {
        mPauseLock = new Object();
        mPaused = false;
    }

    public void run() {
        instance.setRunning(true);
        /*
        Cuando se crea el hilo, se duerme durante 28 minutos
        cuando se "despierta", realiza el token refresh y se llama a si mismo
        */
        while (running) {
            try {
                Thread.sleep(Constantes.MILLIS_TO_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(running) {
                String response = ejecutarRefresh();

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String success = jsonResponse.getString("success");
                    if(success.equals("true")) {
                        User user = User.getInstance();
                        user.setToken(jsonResponse.getString("token"));
                        user.setTokenRefresh(jsonResponse.getString("token_refresh"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            synchronized (mPauseLock) {
                while (mPaused) {
                    try {
                        mPauseLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public void doStop() {
        synchronized (mPauseLock) {
            this.running = false;
            mPaused = true;
        }
    }

    public void doResume() {
        synchronized (mPauseLock) {
            mPaused = false;
            this.running = true;
            mPauseLock.notifyAll();
        }
    }

    private void setRunning(boolean b) {
        this.running = b;
    }

    public boolean isRunning() {
        return this.running;
    }

    public boolean getmPaused() {
        return this.mPaused;
    }

    private String ejecutarRefresh() {
        HttpURLConnection urlConnection;
        User user = User.getInstance();
        String result = "";

        try {
            URL mUrl = new URL(Constantes.URL_TOKEN);

            urlConnection = (HttpURLConnection) mUrl.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod(Constantes.METODO_PUT);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization", "Bearer " + user.getTokenRefresh());

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(result.getBytes(Constantes.UTF));
            wr.flush();

            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if((responseCode == HttpURLConnection.HTTP_OK) || (responseCode == HttpURLConnection.HTTP_CREATED)) {
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream());
                result = convertInputStreamToString(inputStream).toString();
            } else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getErrorStream());
                result = convertInputStreamToString(inputStream).toString();
            } else {
                result = Constantes.REQUEST_ERROR;
            }

            Log.i(Constantes.LOG_TYPE, result);

            wr.close();
            urlConnection.disconnect();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private StringBuilder convertInputStreamToString(InputStreamReader inputStream) throws IOException {
        BufferedReader br = new BufferedReader(inputStream);
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null){
            result.append(line).append("\n");
        }
        br.close();
        return result;
    }
}
