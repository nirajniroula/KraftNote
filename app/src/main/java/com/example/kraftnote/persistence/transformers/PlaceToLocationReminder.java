package com.example.kraftnote.persistence.transformers;

import com.example.kraftnote.persistence.entities.LocationReminder;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

public class PlaceToLocationReminder {
    public static LocationReminder make(Place place, int noteId) {
        LatLng latLng = place.getLatLng();
        LocationReminder lr = new LocationReminder();

        lr.setNoteId(noteId);
        lr.setPlaceId(place.getId());
        lr.setName(place.getName());
        lr.setAddress(place.getAddress());
        lr.setLat(latLng != null ? latLng.latitude : 0);
        lr.setLng(latLng != null ? latLng.longitude : 0);

        return lr;
    }
}
