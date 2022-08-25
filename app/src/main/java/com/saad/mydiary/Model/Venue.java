package com.saad.mydiary.Model;

public class Venue {
    int Venue_ID;
    String Venue_Name;

    public Venue() {
    }

    public Venue(int venue_ID, String venue_Name) {
        Venue_ID = venue_ID;
        Venue_Name = venue_Name;
    }

    public int getVenue_ID() {
        return Venue_ID;
    }

    public void setVenue_ID(int venue_ID) {
        Venue_ID = venue_ID;
    }

    public String getVenue_Name() {
        return Venue_Name;
    }

    public void setVenue_Name(String venue_Name) {
        Venue_Name = venue_Name;
    }
}
