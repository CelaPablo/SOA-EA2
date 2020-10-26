package com.example.soa_ea2.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.soa_ea2.Constantes;

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

public class ServiceHTTP extends IntentService {

    public ServiceHTTP() { super("Service POST"); }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            assert intent != null;
            String uri = intent.getExtras().getString("uri");
            String token = intent.getExtras().getString(("token"));
            String dataJson = intent.getExtras().getString(("dataJson"));
            String operation = intent.getExtras().getString(("operation"));
            String typeRequest = intent.getExtras().getString(("typeRequest"));
            executeRequest(uri, dataJson, operation, typeRequest, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void executeRequest(String uri, String dataJson, String operation, String typeRequest, String token){
        String result = Request(uri,dataJson, typeRequest, token);

        if(result == null || result.equals(Constantes.REQUEST_ERROR)) return;

        Intent i = new Intent(operation);
        i.putExtra("dataJson",result);
        sendBroadcast(i);
    }

    private String Request(String uri, String dataJson, String typeRequest, String token) {
        HttpURLConnection urlConnection;
        String result = null;

        try {
            URL mUrl = new URL(uri);

            urlConnection = (HttpURLConnection) mUrl.openConnection();

            if(token.length() > 0) {
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            } else {
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            }
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod(typeRequest);

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(dataJson.getBytes(Constantes.UTF));

            wr.flush();
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if((responseCode == HttpURLConnection.HTTP_OK) || (responseCode == HttpURLConnection.HTTP_CREATED)) {
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream());
                result = convertInputStreamToString(inputStream).toString();
            } else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getErrorStream());
                result = convertInputStreamToString(inputStream).toString();
                JSONObject resultObject = new JSONObject(result);
                Toast.makeText(getApplicationContext(), resultObject.getString("msg"), Toast.LENGTH_LONG).show();
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
        } catch (JSONException e) {
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
