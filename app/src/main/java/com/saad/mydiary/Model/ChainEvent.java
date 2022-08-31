package com.saad.mydiary.Model;

public class ChainEvent {
    int Event_ID;
    String Event_Title;
    String Event_Date;
    int Venue_ID;
    String Event_Description;
    int EventType_ID;
    Boolean Reminder;
    Boolean Favourite;
    String Time;
    Boolean Daily;
    int ChainID;

    public ChainEvent() {
    }

    public ChainEvent(int event_ID, String event_Title, String event_Date, int venue_ID, String event_Description, int eventType_ID, Boolean reminder, Boolean favourite, String time, Boolean daily, int chainID) {
        Event_ID = event_ID;
        Event_Title = event_Title;
        Event_Date = event_Date;
        Venue_ID = venue_ID;
        Event_Description = event_Description;
        EventType_ID = eventType_ID;
        Reminder = reminder;
        Favourite = favourite;
        Time = time;
        Daily = daily;
        ChainID = chainID;
    }

    public int getEvent_ID() {
        return Event_ID;
    }

    public void setEvent_ID(int event_ID) {
        Event_ID = event_ID;
    }

    public String getEvent_Title() {
        return Event_Title;
    }

    public void setEvent_Title(String event_Title) {
        Event_Title = event_Title;
    }

    public String getEvent_Date() {
        return Event_Date;
    }

    public void setEvent_Date(String event_Date) {
        Event_Date = event_Date;
    }

    public int getVenue_ID() {
        return Venue_ID;
    }

    public void setVenue_ID(int venue_ID) {
        Venue_ID = venue_ID;
    }

    public String getEvent_Description() {
        return Event_Description;
    }

    public void setEvent_Description(String event_Description) {
        Event_Description = event_Description;
    }

    public int getEventType_ID() {
        return EventType_ID;
    }

    public void setEventType_ID(int eventType_ID) {
        EventType_ID = eventType_ID;
    }

    public Boolean getReminder() {
        return Reminder;
    }

    public void setReminder(Boolean reminder) {
        Reminder = reminder;
    }

    public Boolean getFavourite() {
        return Favourite;
    }

    public void setFavourite(Boolean favourite) {
        Favourite = favourite;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public Boolean getDaily() {
        return Daily;
    }

    public void setDaily(Boolean daily) {
        Daily = daily;
    }

    public int getChainID() {
        return ChainID;
    }

    public void setChainID(int chainID) {
        ChainID = chainID;
    }
}
