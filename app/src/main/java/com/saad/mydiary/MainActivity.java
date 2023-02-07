 package com.saad.mydiary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saad.mydiary.Adapter.RecyclerAddDateButtonAdapter;
import com.saad.mydiary.Adapter.RecyclerViewAllEventsAdapter;
import com.saad.mydiary.Adapter.RecyclerviewAllChainEventsAdapter;
import com.saad.mydiary.Adapter.RecyclerviewEventOptionsAdapter;
import com.saad.mydiary.Interface.OnItemClick;
import com.saad.mydiary.Interface.OnOptionClick;
import com.saad.mydiary.Model.ChainEventFile;
import com.saad.mydiary.Model.EventType;
import com.saad.mydiary.Model.Events;
import com.saad.mydiary.Model.Venue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

 public class MainActivity extends AppCompatActivity {
     public static int EventOptions = 1;
     public static int DateOptions = 1;
     public static String URL = "http://192.168.1.106/DiaryAPI/api/Diary/";
     Button Button_Previous_Date, ButtonNext_Date, Button_Add_Events;
     TextView TextView_Date;
     RecyclerView RecyclerView_Events_Options, RecyclerView_All_Events, RecyclerView_Add_Button;
     EditText SearchBox;
     ArrayList<Venue> venueArrayList = new ArrayList<>();
     ArrayList<EventType> eventTypeArrayList = new ArrayList<>();
     ArrayList<Events> eventsArrayList = new ArrayList<>();
     ArrayList<Integer> integerArrayList = new ArrayList<>();
     ArrayList<ChainEventFile> chainEventFileArrayList = new ArrayList<>();
     final Calendar myCalendar = Calendar.getInstance();
     Button Button_ShowEvent, Button_ShowChainEvent;
     RecyclerView RecyclerView_All_ChainEvents;


     //c1


     String TaskEventOption = "";
     String TaskDate = "";
     String TaskName;
     Button TaskButton,TaskButton1;// = findViewById(R.id.Button_Show_Task);


     //c1 complete
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         TaskButton = findViewById(R.id.Button_Show_Task);
         TaskButton1 = findViewById(R.id.Button_Show_Task1);

         Button_Previous_Date = findViewById(R.id.Button_Previous_Date);
         ButtonNext_Date = findViewById(R.id.Button_Next_Date);
         Button_Add_Events = findViewById(R.id.Button_View_Calender);
         TextView_Date = findViewById(R.id.TextView_Date);
         RecyclerView_All_Events = findViewById(R.id.RecyclerView_All_Events);
         RecyclerView_Events_Options = findViewById(R.id.RecyclerView_Events_Option);
         RecyclerView_Add_Button = findViewById(R.id.Linearlayout_Date_Button);
         SearchBox = findViewById(R.id.EditText_Seatch);
         Button_ShowEvent = findViewById(R.id.Button_Show_Event);
         Button_ShowChainEvent = findViewById(R.id.Button_Show_ChainEvent);
         RecyclerView_All_ChainEvents = findViewById(R.id.RecyclerView_All_ChainEvents);


         RecyclerView_Events_Options.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
         RecyclerView_Events_Options.setNestedScrollingEnabled(false);
         RecyclerView_All_Events.setLayoutManager(new LinearLayoutManager(MainActivity.this));
         RecyclerView_Add_Button.setLayoutManager(new LinearLayoutManager(MainActivity.this));
         RecyclerView_All_ChainEvents.setLayoutManager(new LinearLayoutManager(MainActivity.this));

         TextView_Date.setText(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date()));
         try {
             String CurrentDate = TextView_Date.getText().toString();
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
             Calendar c = Calendar.getInstance();
             c.setTime(sdf.parse(CurrentDate));
             int Days = c.getActualMaximum(Calendar.DATE);
             integerArrayList.clear();
             for (int i = 1; i <= Days; i++) {
                 integerArrayList.add(i);
             }
             RecyclerView_Add_Button.setAdapter(new RecyclerAddDateButtonAdapter(MainActivity.this, integerArrayList, onOptionClick));
         } catch (ParseException e) {
             e.printStackTrace();
         }

         DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
             @Override
             public void onDateSet(DatePicker view, int year, int month, int day) {
                 myCalendar.set(Calendar.YEAR, year);
                 myCalendar.set(Calendar.MONTH, month);
                 myCalendar.set(Calendar.DAY_OF_MONTH, day);
                 updateLabel();
             }
         };


         String GetAllEventOptionUrl = URL + "getEventOptions";
         String GetAllEventUrl = URL + "getEvent";
         String GetAllVenueUrl = URL + "getVenue";


         StringRequest stringRequestEventOption = new StringRequest(Request.Method.GET, GetAllEventOptionUrl, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 try {
                     JSONArray jsonArray = new JSONArray(response);
                     for (int i = 0; i < jsonArray.length(); i++) {
                         JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(i).toString());
                         EventType eventType = new EventType();
                         eventType.setEventType_ID(jsonObject.getInt("EventType_ID"));
                         eventType.setEventType_Name(jsonObject.getString("EventType_Name"));
                         eventTypeArrayList.add(eventType);
                     }
                     RecyclerView_Events_Options.setAdapter(new RecyclerviewEventOptionsAdapter(MainActivity.this, eventTypeArrayList, onItemClick));
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Toast.makeText(MainActivity.this, "Error While Getting Event Options Information", Toast.LENGTH_SHORT).show();
             }
         });
         StringRequest stringRequestVenue = new StringRequest(Request.Method.GET, GetAllVenueUrl, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 try {
                     JSONArray jsonArray = new JSONArray(response);
                     for (int i = 0; i < jsonArray.length(); i++) {
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
                 Toast.makeText(MainActivity.this, "Error While Getting Venue Information", Toast.LENGTH_SHORT).show();
             }
         });
         StringRequest stringRequestAllEvent = new StringRequest(Request.Method.GET, GetAllEventUrl, new Response.Listener<String>() {
             @RequiresApi(api = Build.VERSION_CODES.M)
             @Override
             public void onResponse(String response) {
                 try {
                     JSONArray jsonArray = new JSONArray(response);
                     for (int i = 0; i < jsonArray.length(); i++) {
                         JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(i).toString());
                         Events events = new Events();
                         events.setEvent_ID(jsonObject.getInt("Event_ID"));
                         events.setEvent_Title(jsonObject.getString("Event_Title"));
                         events.setEvent_Date(jsonObject.getString("Event_Date"));
                         events.setVenue_ID(jsonObject.getInt("Venue_ID"));
                         events.setEvent_Description(jsonObject.getString("Event_Description"));
                         events.setEventType_ID(jsonObject.getInt("EventType_ID"));
                         events.setReminder(jsonObject.getBoolean("Reminder"));
                         events.setFavourite(jsonObject.getBoolean("Favourite"));
                         events.setTime(jsonObject.getString("Time"));
                         SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                         String CurrentDate = sdf.format(myCalendar.getTime());
                         if (CurrentDate.equals(events.getEvent_Date())) {
                             String TimePattern = "HH:mm";
                             SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TimePattern);
                             String CurrentTime = simpleDateFormat.format(myCalendar.getTime());
                             SimpleDateFormat s = new SimpleDateFormat("hh:mm");
                             Date inTime = s.parse(CurrentTime);
                             Date outTime = s.parse(events.getTime());
                             if (inTime.before(outTime)) {
                                 Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                 PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_ONE_SHOT);
                                 String CHANNEL_ID = "channel_name";// The id of the channel.
                                 NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                         .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                                         .setContentTitle(events.getEvent_Title())
                                         .setContentText(events.getEvent_Description())
                                         .setAutoCancel(true)
                                         .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                         .setContentIntent(pendingIntent);
                                 NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
                                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                     CharSequence name = "Channel Name";// The user-visible name of the channel.
                                     int importance = NotificationManager.IMPORTANCE_HIGH;
                                     NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                                     notificationManager.createNotificationChannel(mChannel);
                                 }
                                 notificationManager.notify(1, notificationBuilder.build());
                             }
                         }
                         eventsArrayList.add(events);
                     }
                     RecyclerView_All_Events.setAdapter(new RecyclerViewAllEventsAdapter(MainActivity.this, eventsArrayList, venueArrayList, eventTypeArrayList));
                 } catch (JSONException e) {
                     e.printStackTrace();
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Toast.makeText(MainActivity.this, "Error While Getting Events Information", Toast.LENGTH_SHORT).show();
             }
         });


         RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
         stringRequestVenue.setRetryPolicy(new DefaultRetryPolicy(0,
                 DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         stringRequestAllEvent.setRetryPolicy(new DefaultRetryPolicy(0,
                 DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         stringRequestEventOption.setRetryPolicy(new DefaultRetryPolicy(0,
                 DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

         requestQueue.add(stringRequestEventOption);
         requestQueue.add(stringRequestVenue);
         requestQueue.add(stringRequestAllEvent);
     //}
 //});



        Button_Add_Events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEventOptionActivity.class);
                startActivity(intent);
            }
        });

        ButtonNext_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String CurrentDate = TextView_Date.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(CurrentDate));
                    c.add(Calendar.MONTH, 1);  // number of days to add
                    TextView_Date.setText(sdf.format(c.getTime()));

                    String EventOnRequiredMonth = URL + "getRequiredMonthEvents?Month="+TextView_Date.getText().toString();
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, EventOnRequiredMonth, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                eventsArrayList.clear();
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(i).toString());
                                    Events events = new Events();
                                    events.setEvent_ID(jsonObject.getInt("Event_ID"));
                                    events.setEvent_Title(jsonObject.getString("Event_Title"));
                                    events.setEvent_Date(jsonObject.getString("Event_Date"));
                                    events.setVenue_ID(jsonObject.getInt("Venue_ID"));
                                    events.setEvent_Description(jsonObject.getString("Event_Description"));
                                    events.setEventType_ID(jsonObject.getInt("EventType_ID"));
                                    events.setReminder(jsonObject.getBoolean("Reminder"));
                                    events.setFavourite(jsonObject.getBoolean("Favourite"));
                                    events.setTime(jsonObject.getString("Time"));
                                    eventsArrayList.add(events);
                                }
                                RecyclerView_All_Events.setAdapter(new RecyclerViewAllEventsAdapter(MainActivity.this, eventsArrayList, venueArrayList, eventTypeArrayList));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(stringRequest);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        Button_Previous_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String CurrentDate = TextView_Date.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(CurrentDate));
                    c.add(Calendar.MONTH, -1);  // number of days to add
                    TextView_Date.setText(sdf.format(c.getTime()));

                    String EventOnRequiredMonth = URL + "getRequiredMonthEvents?Month="+TextView_Date.getText().toString();
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, EventOnRequiredMonth, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                eventsArrayList.clear();
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(i).toString());
                                    Events events = new Events();
                                    events.setEvent_ID(jsonObject.getInt("Event_ID"));
                                    events.setEvent_Title(jsonObject.getString("Event_Title"));
                                    events.setEvent_Date(jsonObject.getString("Event_Date"));
                                    events.setVenue_ID(jsonObject.getInt("Venue_ID"));
                                    events.setEvent_Description(jsonObject.getString("Event_Description"));
                                    events.setEventType_ID(jsonObject.getInt("EventType_ID"));
                                    events.setReminder(jsonObject.getBoolean("Reminder"));
                                    events.setFavourite(jsonObject.getBoolean("Favourite"));
                                    events.setTime(jsonObject.getString("Time"));
                                    eventsArrayList.add(events);
                                }
                                RecyclerView_All_Events.setAdapter(new RecyclerViewAllEventsAdapter(MainActivity.this, eventsArrayList, venueArrayList, eventTypeArrayList));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(stringRequest);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        TextView_Date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    String CurrentDate = TextView_Date.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(CurrentDate));
                    int Days = c.getActualMaximum(Calendar.DATE);
                    integerArrayList.clear();
                    for(int i=1; i<= Days; i++)
                    {
                        integerArrayList.add(i);
                    }
                    RecyclerView_Add_Button.setAdapter(new RecyclerAddDateButtonAdapter(MainActivity.this, integerArrayList, onOptionClick));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        TextView_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        SearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(SearchBox.getText().toString().length()>0)
                {
                    String GetEventFromSearch = URL + "getSearchEvents?Text=" + SearchBox.getText().toString();
                    StringRequest stringRequestSearchBox = new StringRequest(Request.Method.GET, GetEventFromSearch, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                eventsArrayList.clear();
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(i).toString());
                                    Events events = new Events();
                                    events.setEvent_ID(jsonObject.getInt("Event_ID"));
                                    events.setEvent_Title(jsonObject.getString("Event_Title"));
                                    events.setEvent_Date(jsonObject.getString("Event_Date"));
                                    events.setVenue_ID(jsonObject.getInt("Venue_ID"));
                                    events.setEvent_Description(jsonObject.getString("Event_Description"));
                                    events.setEventType_ID(jsonObject.getInt("EventType_ID"));
                                    events.setReminder(jsonObject.getBoolean("Reminder"));
                                    events.setFavourite(jsonObject.getBoolean("Favourite"));
                                    events.setTime(jsonObject.getString("Time"));
                                    eventsArrayList.add(events);
                                }
                                RecyclerView_All_Events.setAdapter(new RecyclerViewAllEventsAdapter(MainActivity.this, eventsArrayList, venueArrayList, eventTypeArrayList));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    RequestQueue requestQueueSearchBox = Volley.newRequestQueue(MainActivity.this);
                    requestQueueSearchBox.add(stringRequestSearchBox);
                }
                else
                {
                    String GetEventFromSearch = URL +"getEvent";;
                    StringRequest stringRequestSearchBox = new StringRequest(Request.Method.GET, GetEventFromSearch, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                eventsArrayList.clear();
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(i).toString());
                                    Events events = new Events();
                                    events.setEvent_ID(jsonObject.getInt("Event_ID"));
                                    events.setEvent_Title(jsonObject.getString("Event_Title"));
                                    events.setEvent_Date(jsonObject.getString("Event_Date"));
                                    events.setVenue_ID(jsonObject.getInt("Venue_ID"));
                                    events.setEvent_Description(jsonObject.getString("Event_Description"));
                                    events.setEventType_ID(jsonObject.getInt("EventType_ID"));
                                    events.setReminder(jsonObject.getBoolean("Reminder"));
                                    events.setFavourite(jsonObject.getBoolean("Favourite"));
                                    events.setTime(jsonObject.getString("Time"));
                                    eventsArrayList.add(events);
                                }
                                RecyclerView_All_Events.setAdapter(new RecyclerViewAllEventsAdapter(MainActivity.this, eventsArrayList, venueArrayList, eventTypeArrayList));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    RequestQueue requestQueueSearchBox = Volley.newRequestQueue(MainActivity.this);
                    requestQueueSearchBox.add(stringRequestSearchBox);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        TaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequestAllEventAgain = new StringRequest(Request.Method.GET, GetAllEventUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            eventsArrayList.clear();
                            for(int i=0; i<jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(i).toString());
                                Events events = new Events();
                                events.setEvent_ID(jsonObject.getInt("Event_ID"));
                                events.setEvent_Title(jsonObject.getString("Event_Title"));
                                events.setEvent_Date(jsonObject.getString("Event_Date"));
                                events.setVenue_ID(jsonObject.getInt("Venue_ID"));
                                events.setEvent_Description(jsonObject.getString("Event_Description"));
                                events.setEventType_ID(jsonObject.getInt("EventType_ID"));
                                events.setReminder(jsonObject.getBoolean("Reminder"));
                                events.setFavourite(jsonObject.getBoolean("Favourite"));
                                events.setTime(jsonObject.getString("Time"));
                                eventsArrayList.add(events);
                            }
                            RecyclerView_All_Events.setAdapter(new RecyclerViewAllEventsAdapter(MainActivity.this, eventsArrayList, venueArrayList, eventTypeArrayList));
                            RecyclerView_All_Events.setVisibility(View.VISIBLE);
                            RecyclerView_All_ChainEvents.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error While Getting Events Information", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(stringRequestAllEventAgain);

            }
        });

        Button_ShowChainEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String GetAllChainEventUrl = URL + "getChainEvent";
                StringRequest stringRequestAllChainEvent = new StringRequest(Request.Method.GET, GetAllChainEventUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            chainEventFileArrayList.clear();
                            for (int i=0; i<jsonArray.length() ;i++)
                            {
                                JSONObject jsonObject = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));
                                ChainEventFile chainEventFile = new ChainEventFile();
                                chainEventFile.setChainID(jsonObject.getInt("ChainID"));
                                chainEventFile.setTitlel(jsonObject.getString("Event_Title"));
                                chainEventFileArrayList.add(chainEventFile);
                            }
                            RecyclerView_All_ChainEvents.setAdapter(new RecyclerviewAllChainEventsAdapter(MainActivity.this, chainEventFileArrayList, venueArrayList, eventTypeArrayList));
                            RecyclerView_All_ChainEvents.setVisibility(View.VISIBLE);
                            RecyclerView_All_Events.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                stringRequestAllChainEvent.setRetryPolicy(new DefaultRetryPolicy(0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequestAllChainEvent);
            }
        });

        TaskButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 TaskName = SearchBox.getText().toString();
                 if(!TaskEventOption.equals("") )
                 {
                     if(!TaskName.equals(""))
                     {
                         String TaskURL = URL + "getEventTask?Option="+TaskEventOption.toString()+ "&Name="+TaskName;
                         StringRequest request = new StringRequest(Request.Method.GET, TaskURL, new Response.Listener<String>() {
                             @Override
                             public void onResponse(String response) {
                                 try {
                                     JSONArray jsonArray = new JSONArray(response);
                                     eventsArrayList.clear();
                                     for(int i=0; i<jsonArray.length(); i++) {
                                         JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(i).toString());
                                         Events events = new Events();
                                         events.setEvent_ID(jsonObject.getInt("Event_ID"));
                                         events.setEvent_Title(jsonObject.getString("Event_Title"));
                                         events.setEvent_Date(jsonObject.getString("Event_Date"));
                                         events.setVenue_ID(jsonObject.getInt("Venue_ID"));
                                         events.setEvent_Description(jsonObject.getString("Event_Description"));
                                         events.setEventType_ID(jsonObject.getInt("EventType_ID"));
                                         events.setReminder(jsonObject.getBoolean("Reminder"));
                                         events.setFavourite(jsonObject.getBoolean("Favourite"));
                                         events.setTime(jsonObject.getString("Time"));
                                         String FirstFourCharacterOfYears = TextView_Date.getText().toString().substring(0,4);
                                         String FirstFourCharacterOfYearsDate = events.getEvent_Date().toString().substring(0,4);
                                         if(FirstFourCharacterOfYears.equals(FirstFourCharacterOfYearsDate)) {
                                             eventsArrayList.add(events);
                                         }
                                     }
                                     RecyclerView_All_Events.setAdapter(new RecyclerViewAllEventsAdapter(MainActivity.this, eventsArrayList, venueArrayList, eventTypeArrayList));
                                     RecyclerView_All_Events.setVisibility(View.VISIBLE);
                                     RecyclerView_All_ChainEvents.setVisibility(View.GONE);
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }
                         }, new Response.ErrorListener() {
                             @Override
                             public void onErrorResponse(VolleyError error) {

                             }
                         });
                         RequestQueue requestQueue1 = Volley.newRequestQueue(MainActivity.this);
                         requestQueue1.add(request);
                     }
                     else
                     {
                         Toast.makeText(MainActivity.this, "Please Enter Name to Seaech in SearchBox", Toast.LENGTH_SHORT).show();
                     }
                 }
                 else
                 {
                     Toast.makeText(MainActivity.this, "Please Select Event", Toast.LENGTH_SHORT).show();
                 }
             }
         });

    }

     private void updateLabel(){
         String myFormat="yyyy-MM";
         SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
         TextView_Date.setText(dateFormat.format(myCalendar.getTime()));
     }

     OnItemClick onItemClick = new OnItemClick() {
         @Override
         public void onClick(int value) {
             for(int i=0; i< eventTypeArrayList.size(); i++)
             {
                 if(eventTypeArrayList.get(i).getEventType_ID() == value)
                 {
                     TaskEventOption = eventTypeArrayList.get(i).getEventType_Name();
                 }
             }
             RecyclerView_All_Events.setAdapter(null);
             String CHainOption = URL + "getSpecificChainEventNew?EventTypeId="+value;
             StringRequest request = new StringRequest(Request.Method.GET, CHainOption, new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {
                     try {
                         JSONArray jsonArray = new JSONArray(response);
                         chainEventFileArrayList.clear();
                         for (int i=0; i<jsonArray.length() ;i++)
                         {
                             JSONObject jsonObject = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));
                             ChainEventFile chainEventFile = new ChainEventFile();
                             chainEventFile.setChainID(jsonObject.getInt("ChainID"));
                             chainEventFile.setTitlel(jsonObject.getString("Event_Title"));
                             chainEventFileArrayList.add(chainEventFile);
                         }
                         RecyclerView_All_ChainEvents.setAdapter(new RecyclerviewAllChainEventsAdapter(MainActivity.this, chainEventFileArrayList, venueArrayList, eventTypeArrayList));
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }
             }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {

                 }
             });
             String GetSpecificEventUrl = URL + "getSpecificEvent?EventTypeId=";
             StringRequest stringRequestSpecificEvent = new StringRequest(Request.Method.GET, GetSpecificEventUrl + value, new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {
                     try {
                         JSONArray jsonArray = new JSONArray(response);
                         eventsArrayList.clear();
                         for(int i=0; i<jsonArray.length(); i++) {
                             JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(i).toString());
                             Events events = new Events();
                             events.setEvent_ID(jsonObject.getInt("Event_ID"));
                             events.setEvent_Title(jsonObject.getString("Event_Title"));
                             events.setEvent_Date(jsonObject.getString("Event_Date"));
                             events.setVenue_ID(jsonObject.getInt("Venue_ID"));
                             events.setEvent_Description(jsonObject.getString("Event_Description"));
                             events.setEventType_ID(jsonObject.getInt("EventType_ID"));
                             events.setReminder(jsonObject.getBoolean("Reminder"));
                             events.setFavourite(jsonObject.getBoolean("Favourite"));
                             events.setTime(jsonObject.getString("Time"));
                             eventsArrayList.add(events);
                         }
                         RecyclerView_All_Events.setAdapter(new RecyclerViewAllEventsAdapter(MainActivity.this, eventsArrayList, venueArrayList, eventTypeArrayList));
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }
             }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {
                     Toast.makeText(MainActivity.this, "Error While Getting Events Information", Toast.LENGTH_SHORT).show();
                 }
             });
             RequestQueue requestQueueSpecificEvent = Volley.newRequestQueue(getApplicationContext());
             requestQueueSpecificEvent.add(request);
             requestQueueSpecificEvent.add(stringRequestSpecificEvent);
         }
     };

    OnOptionClick onOptionClick = new OnOptionClick() {
        @Override
        public void onClick(int value) {
            RecyclerView_All_Events.setAdapter(null);
            String GetSpecificEventUrl = URL + "getSpecificEventOnDate?EventTypeId=";
            String Date = "";
            if(value<10)
            {
                Date = TextView_Date.getText().toString().replace(" ", "/") + "-0" + value;
                Date = Date.replace("-","/");
            }
            else
            {
                Date = TextView_Date.getText().toString().replace(" ", "/") + "-" + value;
                Date = Date.replace("-","/");
            }
            GetSpecificEventUrl += EventOptions + "&Date=" + Date;
            StringRequest stringRequestSpecificEvent = new StringRequest(Request.Method.GET, GetSpecificEventUrl , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        eventsArrayList.clear();
                        for(int i=0; i<jsonArray.length(); i++) {
                            JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(i).toString());
                            Events events = new Events();
                            events.setEvent_ID(jsonObject.getInt("Event_ID"));
                            events.setEvent_Title(jsonObject.getString("Event_Title"));
                            events.setEvent_Date(jsonObject.getString("Event_Date"));
                            events.setVenue_ID(jsonObject.getInt("Venue_ID"));
                            events.setEvent_Description(jsonObject.getString("Event_Description"));
                            events.setEventType_ID(jsonObject.getInt("EventType_ID"));
                            events.setReminder(jsonObject.getBoolean("Reminder"));
                            events.setFavourite(jsonObject.getBoolean("Favourite"));
                            events.setTime(jsonObject.getString("Time"));
                            eventsArrayList.add(events);
                        }
                        RecyclerView_All_Events.setAdapter(new RecyclerViewAllEventsAdapter(MainActivity.this, eventsArrayList, venueArrayList, eventTypeArrayList));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Error While Getting Events Information", Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueueSpecificEvent = Volley.newRequestQueue(getApplicationContext());
            requestQueueSpecificEvent.add(stringRequestSpecificEvent);
        }
    };


 }