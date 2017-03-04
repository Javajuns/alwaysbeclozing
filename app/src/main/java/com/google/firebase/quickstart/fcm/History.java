package com.google.firebase.quickstart.fcm;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {


    private ArrayList<HistoyElementAdapter.HistoyElement> displayedHistoryelements;
    private HistoyElementAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        displayedHistoryelements = new ArrayList<>();
        mAdapter = new HistoyElementAdapter(displayedHistoryelements, History.this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(200);
        animator.setChangeDuration(200);
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(mAdapter);
        new JsonSending().execute();
    }

    private class JsonSending extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String resp = "";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("method", "alexa.history");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HttpURLConnection httpcon;
//        String url = null;
            String data = jsonObject.toString();
            String result = null;
            try {
                //Connect
                httpcon = (HttpURLConnection) ((new URL(Consts.HOST).openConnection()));
                httpcon.setDoOutput(true);
                httpcon.setRequestProperty("Content-Type", "application/json");
                httpcon.setRequestProperty("Accept", "application/json");
                httpcon.setRequestMethod("POST");
                httpcon.connect();

                //Write
                OutputStream os = httpcon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.close();
                os.close();

                //Read
                BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

                String line = null;
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                resp = sb.toString();
                Log.d(getBaseContext().getPackageName(), "Http response:" + resp);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resp;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.d(getBaseContext().getPackageName(), s);
            compareAndvisualize(getList(s));
        }
    }

    List<HistoyElementAdapter.HistoyElement> getList(String json) {
        JSONObject obj = null;
        List<HistoyElementAdapter.HistoyElement> result = new ArrayList<>();
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray arr = null;
        try {
            arr = obj.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < arr.length(); i++) {
            try {
                int post_id = arr.getJSONObject(i).getInt("id");
                String sended = arr.getJSONObject(i).getString("request");
                String received = arr.getJSONObject(i).getString("answer");
                String date =   arr.getJSONObject(i).getString("date");
                result.add(new HistoyElementAdapter.HistoyElement(post_id, sended,received, date));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    void compareAndvisualize(List<HistoyElementAdapter.HistoyElement> received) {

        if (displayedHistoryelements.size() == 0) {
            for (HistoyElementAdapter.HistoyElement evt : received) {
                displayedHistoryelements.add(evt);
                mAdapter.notifyItemInserted(displayedHistoryelements.size() - 1);
            }
        } else {
            for (HistoyElementAdapter.HistoyElement displayedEvent : displayedHistoryelements) {

                if (received.size() != 0) {

                    boolean ispresent = false;
                    for (HistoyElementAdapter.HistoyElement receivedEvent : received) {
                        if (displayedEvent.getId() == receivedEvent.getId()) {
                            ispresent = true;
                            break;
                        }
                    }
                    if (!ispresent) {
                        int index = displayedHistoryelements.indexOf(displayedEvent);
                        displayedHistoryelements.remove(displayedEvent);
                        mAdapter.notifyItemRemoved(index);
                    }
                } else {
                    displayedHistoryelements.clear();
                    mAdapter.notifyAll();
                }
            }
            for (HistoyElementAdapter.HistoyElement receivedEvent : received) {
                boolean ispresent = false;
                for (HistoyElementAdapter.HistoyElement displayedEvent : displayedHistoryelements) {
                    if (displayedEvent.getId() == receivedEvent.getId()) {
                        ispresent = true;
                    }
                }
                if (!ispresent) {
                    displayedHistoryelements.add(receivedEvent);
                    mAdapter.notifyItemInserted(displayedHistoryelements.size() - 1);
                }

            }

        }

    }
}
