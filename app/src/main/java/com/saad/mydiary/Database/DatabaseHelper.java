package com.saad.mydiary.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.saad.mydiary.Model.Events;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Events.db";
    private static final String TABLE_NAME = "Events";
    private static final String COLUMN_EVENT_ID = "Event_ID";
    private static final String COLUMN_EVENT_TITLE = "Event_Title";
    private static final String COLUMN_EVENT_DATE = "Event_Date";
    private static final String COLUMN_VENUE_ID = "Venue_ID";
    private static final String COLUMN_EVENT_DESCRIPTION = "Event_Description";
    private static final String COLUMN_EVENTTYPE_ID = "EventType_ID";
    private static final String COLUMN_REMINDER = "Reminder";
    private static final String COLUMN_FAVOURITE = "Favourite";
    private static final String COLUMN_TIME = "Time";
    private static final String COLUMN_DAILY = "Daily";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_EVENT_TITLE + " TEXT,"
            + COLUMN_EVENT_DATE + " TEXT,"
            + COLUMN_VENUE_ID + " INTEGER,"
            + COLUMN_EVENT_DESCRIPTION + " TEXT,"
            + COLUMN_EVENTTYPE_ID + " INTEGER,"
            + COLUMN_REMINDER + " INTEGER,"
            + COLUMN_FAVOURITE + " INTEGER,"
            + COLUMN_TIME + " TEXT,"
            + COLUMN_DAILY + " INTEGER" + ")";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public long addEvent(Events events) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_TITLE, events.getEvent_Title());
        values.put(COLUMN_EVENT_DATE, events.getEvent_Date());
        values.put(COLUMN_VENUE_ID, events.getVenue_ID());
        values.put(COLUMN_EVENT_DESCRIPTION, events.getEvent_Description());
        values.put(COLUMN_EVENTTYPE_ID, events.getEventType_ID());
        values.put(COLUMN_REMINDER, events.getReminder());
        values.put(COLUMN_FAVOURITE, events.getFavourite());
        values.put(COLUMN_TIME, events.getTime());
        values.put(COLUMN_DAILY, events.getDaily());

        long event_id = db.insert(TABLE_NAME, null, values);
        return event_id;
    }

    public List<Events> getAllEvents()  {
        List<Events> eventsList = new ArrayList<Events>();

        String selectQuery = "SELECT * FROM Events";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Events events = new Events();
                events.setEvent_ID(cursor.getInt(0));
                events.setEvent_Title(cursor.getString(1));
                events.setEvent_Date(cursor.getString(2));
                events.setVenue_ID(cursor.getInt(3));
                events.setEvent_Description(cursor.getString(4));
                events.setEventType_ID(cursor.getInt(5));
                events.setReminder(cursor.getInt(6) > 0);
                events.setFavourite(cursor.getInt(7) > 0);
                events.setTime(cursor.getString(8));
                events.setDaily(cursor.getInt(9) > 0);

                eventsList.add(events);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return eventsList;
    }

    public boolean updateEvent(Events event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_TITLE, event.getEvent_Title());
        values.put(COLUMN_EVENT_DATE, event.getEvent_Date());
        values.put(COLUMN_VENUE_ID, event.getVenue_ID());
        values.put(COLUMN_EVENT_DESCRIPTION, event.getEvent_Description());
        values.put(COLUMN_EVENTTYPE_ID, event.getEventType_ID());
        values.put(COLUMN_REMINDER, event.getReminder());
        values.put(COLUMN_FAVOURITE, event.getFavourite());
        values.put(COLUMN_TIME, event.getTime());
        values.put(COLUMN_DAILY, event.getDaily());

        int result = db.update(TABLE_NAME, values, COLUMN_EVENT_ID + " = ?",
                new String[]{String.valueOf(event.getEvent_ID())});
        db.close();
        return result > 0;
    }

    public boolean updateEventById(int id, Events event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_TITLE, event.getEvent_Title());
        values.put(COLUMN_EVENT_DATE, event.getEvent_Date());
        values.put(COLUMN_VENUE_ID, event.getVenue_ID());
        values.put(COLUMN_EVENT_DESCRIPTION, event.getEvent_Description());
        values.put(COLUMN_EVENTTYPE_ID, event.getEventType_ID());
        values.put(COLUMN_REMINDER, event.getReminder());
        values.put(COLUMN_FAVOURITE, event.getFavourite());
        values.put(COLUMN_TIME, event.getTime());
        values.put(COLUMN_DAILY, event.getDaily());

        int result = db.update(TABLE_NAME, values, COLUMN_EVENT_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public boolean deleteEventById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_EVENT_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }
}

