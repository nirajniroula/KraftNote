package com.example.kraftnote.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import com.google.android.gms.maps.model.LatLng;

@Entity(tableName = "LocationReminders", foreignKeys = {
        @ForeignKey(entity = Note.class, parentColumns = "id",
                childColumns = "note_id", onDelete = ForeignKey.CASCADE)
})
public class LocationReminder extends BaseEntity {

    @ColumnInfo(name = "place_id")
    private String placeId;
    private String name;
    private String address;
    private double lat;
    private double lng;

    @ColumnInfo(name = "note_id")
    private int noteId;

    @Ignore
    public LocationReminder() {
    }

    public LocationReminder(String placeId,
                            String name,
                            String address,
                            double lat,
                            double lng,
                            int noteId) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.noteId = noteId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    @Ignore
    public String getFullAddress() {
        return getName() + ", " + getAddress();
    }

    @Ignore
    public LatLng getLatLng() {
        return new LatLng(getLat(), getLng());
    }
}
