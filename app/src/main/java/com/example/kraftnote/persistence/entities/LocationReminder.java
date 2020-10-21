package com.example.kraftnote.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.kraftnote.persistence.converters.CreatedAtConverter;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

@Entity(tableName = "LocationReminders", foreignKeys =
        {@ForeignKey(entity = Note.class, parentColumns = "note_id", childColumns = "location_reminder_note_id", onDelete = ForeignKey.CASCADE)}
)
public class LocationReminder {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "location_reminder_id")
    protected Integer id;

    @ColumnInfo(name = "location_reminder_place_id", index = true)
    private String placeId;

    @ColumnInfo(name = "location_reminder_name")
    private String name;

    @ColumnInfo(name = "location_reminder_address")
    private String address;

    @ColumnInfo(name = "location_reminder_lat")
    private double lat;

    @ColumnInfo(name = "location_reminder_lng")
    private double lng;

    @ColumnInfo(name = "location_reminder_note_id", index = true)
    private int noteId;

    @TypeConverters(CreatedAtConverter.class)
    @ColumnInfo(name = "location_reminder_created_at", defaultValue = "CURRENT_TIMESTAMP")
    protected Date createdAt;

    @Ignore
    public LocationReminder() {
    }

    public LocationReminder(Integer id, String placeId, String name, String address, double lat, double lng, int noteId, Date createdAt) {
        this.id = id;
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.noteId = noteId;
        setCreatedAt(createdAt);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = (createdAt == null) ? new Date() : createdAt;
    }

    @Override
    public String toString() {
        return "LocationReminder{" +
                "id=" + id +
                ", placeId='" + placeId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", noteId=" + noteId +
                ", createdAt=" + createdAt +
                '}';
    }
}
