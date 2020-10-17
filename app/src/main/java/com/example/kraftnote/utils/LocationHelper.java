package com.example.kraftnote.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import java.util.Arrays;
import java.util.List;

public class LocationHelper {
    public static final LatLng AUSTRALIA = new LatLng(25.2744, 133.7751);
    public static final LatLng SYDNEY = new LatLng(33.8688, 151.2093);

    public static final List<Place.Field> PLACES_FIELDS = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

    public static final RectangularBounds DEFAULT_LOCATION_BIAS = RectangularBounds.newInstance(
            LatLngBounds.builder()
                    .include(SYDNEY)
                    .include(AUSTRALIA)
                    .build()
    );

    public static void forFragment(final AutocompleteSupportFragment fragment) {
        fragment.setPlaceFields(LocationHelper.PLACES_FIELDS);
        fragment.setLocationBias(LocationHelper.DEFAULT_LOCATION_BIAS);
    }
}
