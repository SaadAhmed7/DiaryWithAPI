package com.saad.mydiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saad.mydiary.Adapter.RecyclerviewEventOptionsAdapter;
import com.saad.mydiary.Adapter.RecyclerviewShowChainEvents;
import com.saad.mydiary.Model.ChainEvent;
import com.saad.mydiary.Model.EventType;
import com.saad.mydiary.Model.Venue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.saad.mydiary.MainActivity.URL;

public class ChainEventActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ChainEvent> chainEventList = new ArrayList<>();
    ArrayList<Venue> venueArrayList = new ArrayList<>();
    ArrayList<EventType> eventTypeArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chain_event);

        recyclerView = findViewById(R.id.RecyclerView_ChainEvent);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChainEventActivity.this));

        Intent intent = getIntent();
        int ChainID = intent.getIntExtra("ChainID", 0);

        String GetAllEventOptionUrl = URL +"getEventOptions";
        String GetAllVenueUrl = URL +"getVenue";
        String GetSpecificChainEventURL = URL  + "getSpecificChainEvent?ChainID="+ChainID;

        StringRequest stringRequestEventOption = new StringRequest(Request.Method.GET, GetAllEventOptionUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(i).toString());
                        EventType eventType = new EventType();
                        eventType.setEventType_ID(jsonObject.getInt("EventType_ID"));
                        eventType.setEventType_Name(jsonObject.getString("EventType_Name"));
                        eventTypeArrayList.add(eventType);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChainEventActivity.this, "Error While Getting Event Options Information", Toast.LENGTH_SHORT).show();
            }
        });
        StringRequest stringRequestVenue = new StringRequest(Request.Method.GET, GetAllVenueUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(i).toString());
                        Venue venue = new Venue();
                        venue.setVenue_ID(jsonObject.getInt("Venue_ID"));
                        venue.setVenue_Name(jsonObject.getString("Venue_Name"));
                        venueArrayList.add(venue);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChainEventActivity.this, "Error While Getting Venue Information", Toast.LENGTH_SHORT).show();
            }
        });
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GetSpecificChainEventURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i< jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));
                        ChainEvent chainEvent = new ChainEvent();
                        chainEvent.setEvent_ID(jsonObject.getInt("Event_ID"));
                        chainEvent.setEvent_Title(jsonObject.getString("Event_Title"));
                        chainEvent.setEvent_Date(jsonObject.getString("Event_Date"));
                        chainEvent.setVenue_ID(jsonObject.getInt("Venue_ID"));
                        chainEvent.setEvent_Description(jsonObject.getString("Event_Description"));
                        chainEvent.setEventType_ID(jsonObject.getInt("EventType_ID"));
                        chainEvent.setReminder(jsonObject.getBoolean("Reminder"));
                        chainEvent.setFavourite(jsonObject.getBoolean("Favourite"));
                        chainEvent.setTime(jsonObject.getString("Time"));
                        chainEvent.setChainID(jsonObject.getInt("ChainID"));
                        chainEventList.add(chainEvent);
                    }
                    recyclerView.setAdapter(new RecyclerviewShowChainEvents(ChainEventActivity.this, chainEventList, venueArrayList, eventTypeArrayList));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequestVenue.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequestEventOption.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ChainEventActivity.this);
        requestQueue.add(stringRequestEventOption);
        requestQueue.add(stringRequestVenue);
        requestQueue.add(stringRequest);

    }
}