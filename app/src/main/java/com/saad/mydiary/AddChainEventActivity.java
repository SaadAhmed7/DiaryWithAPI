package com.saad.mydiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saad.mydiary.Model.EventType;
import com.saad.mydiary.Model.Events;
import com.saad.mydiary.Model.Venue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.saad.mydiary.MainActivity.URL;

public class AddChainEventActivity extends AppCompatActivity {
    Button Button_Save, Button_ChainEvent;
    EditText EditText_Title, EditText_Date, EditText_Time, EditText_Description;
    Spinner Spinner_Venue, Spinner_Event;
    EditText AddVenue, AddEventOption;
    ArrayList<com.saad.mydiary.Model.Venue> venueArrayList = new ArrayList<>();
    List<String> Venue = new ArrayList<>();
    ArrayList<EventType> eventTypeArrayList = new ArrayList<>();
    List<String> Event = new ArrayList<>();
    ArrayList<Events> eventsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chain_event);

        Button_Save = findViewById(R.id.Button_Save);
        EditText_Title = findViewById(R.id.EditText_Title);
        EditText_Date = findViewById(R.id.EditText_Date);
        EditText_Time = findViewById(R.id.EditText_Time);
        EditText_Description = findViewById(R.id.EditText_Description);
        Spinner_Event = findViewById(R.id.Spinner_Event);
        Spinner_Venue = findViewById(R.id.Spinner_Venue);
        AddVenue = findViewById(R.id.EditText_Add_Venue);
        AddEventOption = findViewById(R.id.EditText_Add_Event);
        Button_ChainEvent = findViewById(R.id.Button_AddChain);

        Intent intent = getIntent();
        int EventID = intent.getIntExtra("EventID", 0);

        String GetAllEventOptionUrl = URL +"getEventOptions";
        String GetAllVenueUrl = URL +"getVenue";
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
                        Venue.add(venue.getVenue_Name());

                    }
                    Venue.add("Other");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddChainEventActivity.this, android.R.layout.simple_list_item_1, Venue);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner_Venue.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddChainEventActivity.this, "Error While Getting Venue Information", Toast.LENGTH_SHORT).show();
            }
        });
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
                        Event.add(eventType.getEventType_Name());
                    }
                    Event.remove("All");
                    Event.add("Other");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddChainEventActivity.this, android.R.layout.simple_list_item_1, Event);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner_Event.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddChainEventActivity.this, "Error While Getting Event Options Information", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequestVenue.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequestEventOption.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequestEventOption);
        requestQueue.add(stringRequestVenue);

        Spinner_Venue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(Spinner_Venue.getSelectedItem() == "Other")
                {
                    AddVenue.setVisibility(View.VISIBLE);
                }
                else
                {
                    AddVenue.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner_Event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(Spinner_Venue.getSelectedItem() == "Other")
                {
                    AddEventOption.setVisibility(View.VISIBLE);
                }
                else
                {
                    AddEventOption.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button_ChainEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EditText_Title.length()>0 && EditText_Date.length()>0 && EditText_Description.length()>0 && EditText_Time.length()>0)
                {
                    Events events = new Events();
                    events.setEvent_Title(EditText_Title.getText().toString());
                    events.setTime(EditText_Time.getText().toString());
                    events.setEvent_Date(EditText_Date.getText().toString());
                    events.setEvent_Description(EditText_Description.getText().toString());
                    events.setFavourite(false);
                    events.setReminder(false);
                    if(Spinner_Event.getSelectedItem() != "Other" && Spinner_Venue.getSelectedItem() != "Other")
                    {
                        events.setEventType_ID(Spinner_Event.getSelectedItemPosition());
                        events.setVenue_ID(Spinner_Venue.getSelectedItemPosition());
                        eventsArrayList.add(events);
                        EditText_Title.setEnabled(false);
                        Spinner_Event.setEnabled(false);
                        EditText_Time.setText("");
                        EditText_Date.setText("");
                        EditText_Description.setText("");
                    }
                    else
                    {
                        if(Spinner_Event.getSelectedItem() != "Other" && Spinner_Event.getSelectedItem() == "Other")
                        {
                            String AddEvent = URL + "setEvent?EventName=" + AddEventOption;
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, AddEvent, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        events.setEventType_ID(jsonObject.getInt("Event_ID"));
                                        events.setVenue_ID(Spinner_Venue.getSelectedItemPosition());
                                        eventsArrayList.add(events);
                                        EditText_Title.setEnabled(false);
                                        Spinner_Event.setEnabled(false);
                                        EditText_Time.setText("");
                                        EditText_Date.setText("");
                                        EditText_Description.setText("");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            RequestQueue requestQueue1 = Volley.newRequestQueue(AddChainEventActivity.this);
                            requestQueue1.add(stringRequest);
                        }
                        else  if(Spinner_Event.getSelectedItem() == "Other" && Spinner_Event.getSelectedItem() != "Other")
                        {
                            String AddEvent = URL + "setVenue?VenueName=" + AddVenue;StringRequest stringRequest = new StringRequest(Request.Method.POST, AddEvent, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    events.setEventType_ID(Spinner_Event.getSelectedItemPosition());
                                    events.setVenue_ID(jsonObject.getInt("Venue_ID"));
                                    eventsArrayList.add(events);
                                    EditText_Title.setEnabled(false);
                                    Spinner_Event.setEnabled(false);
                                    EditText_Time.setText("");
                                    EditText_Date.setText("");
                                    EditText_Description.setText("");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                            RequestQueue requestQueue1 = Volley.newRequestQueue(AddChainEventActivity.this);
                            requestQueue1.add(stringRequest);
                        }
                        else
                        {
                            String AddEvent = URL + "setEventAndVenue?EventName=" + AddEventOption + "&VenueName=" + AddVenue;
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, AddEvent, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    events.setEventType_ID(jsonObject.getInt("Event_ID"));
                                    events.setVenue_ID(jsonObject.getInt("Venue_ID"));
                                    eventsArrayList.add(events);
                                    EditText_Title.setEnabled(false);
                                    Spinner_Event.setEnabled(false);
                                    EditText_Time.setText("");
                                    EditText_Date.setText("");
                                    EditText_Description.setText("");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                            RequestQueue requestQueue1 = Volley.newRequestQueue(AddChainEventActivity.this);
                            requestQueue1.add(stringRequest);
                        }
                    }
                }
                else
                {
                    Toast.makeText(AddChainEventActivity.this, "Please Fill the Required Field", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eventsArrayList.size()>0)
                {
                    try {
                        JSONArray jsonArray = new JSONArray();
                        for(int i=0; i<eventsArrayList.size(); i++)
                        {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("Event_Title", eventsArrayList.get(i).getEvent_Title());
                            jsonObject.put("Time", eventsArrayList.get(i).getTime());
                            jsonObject.put("Event_Date", eventsArrayList.get(i).getEvent_Date());
                            jsonObject.put("Event_Description", eventsArrayList.get(i).getEvent_Description());
                            jsonObject.put("Favourite", false);
                            jsonObject.put("Reminder", false);
                            jsonObject.put("Venue_ID", eventsArrayList.get(i).getVenue_ID());
                            jsonObject.put("EventType_ID", eventsArrayList.get(i).getEventType_ID());
                            jsonArray.put(jsonObject);
                        }
                        String AddChainEvent = URL + "AddChainEvent";
                        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST, AddChainEvent, jsonArray, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Toast.makeText(AddChainEventActivity.this, "Chain Added", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        RequestQueue requestQueue1 = Volley.newRequestQueue(AddChainEventActivity.this);
                        requestQueue1.add(jsonObjectRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(AddChainEventActivity.this, "Add Event", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}