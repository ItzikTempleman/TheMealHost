package com.example.therecipehost.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.therecipehost.Constants.GlobalConstants;
import com.example.therecipehost.Models.IResponse;
import com.example.therecipehost.Utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MealAsyncTask extends AsyncTask<Void, Void, String> {

    private String searchTitle;
    private IResponse iResponse;

    public MealAsyncTask(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        iResponse.onLoading(true);
    }

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder data = new StringBuilder();
        try {
            URL url = new URL(Utils.getURL(searchTitle));
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // set connection time out for requests
            //httpURLConnection.setConnectTimeout(10);
            // adding Headers
            httpURLConnection.addRequestProperty(GlobalConstants.RAPID_API_KEY, GlobalConstants.RAPID_API_VALUE);
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            iResponse.onError(e.getMessage());
            iResponse.onLoading(false);
        }
        return data.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        iResponse.onSuccess(s);
        iResponse.onLoading(false);
    }

    public void setIResponse(IResponse iResponse) {
        this.iResponse = iResponse;
    }
}
