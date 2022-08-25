package com.saad.mydiary.Model;

public class EventType {
    int EventType_ID;
    String EventType_Name;

    public EventType() {
    }

    public EventType(int eventType_ID, String eventType_Name) {
        EventType_ID = eventType_ID;
        EventType_Name = eventType_Name;
    }

    public int getEventType_ID() {
        return EventType_ID;
    }

    public void setEventType_ID(int eventType_ID) {
        EventType_ID = eventType_ID;
    }

    public String getEventType_Name() {
        return EventType_Name;
    }

    public void setEventType_Name(String eventType_Name) {
        EventType_Name = eventType_Name;
    }
}
