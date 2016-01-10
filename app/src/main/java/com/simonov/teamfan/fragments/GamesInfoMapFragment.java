package com.simonov.teamfan.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.simonov.teamfan.R;
import com.simonov.teamfan.objects.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 10-Jan-16.
 */
public class GamesInfoMapFragment extends Fragment {
    Event mEvent;

    public GamesInfoMapFragment(Event mEvent) {
        this.mEvent = mEvent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game_info_map, container, false);

        GoogleMap mMap = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        if (Geocoder.isPresent()) {
            try {
                Geocoder gc = new Geocoder(getContext());
                Log.d("mytag:eventLocationName:",mEvent.eventLocationName.split(",")[0]);

                List<Address> addresses = gc.getFromLocationName(mEvent.eventLocationName.split(",")[0], 5); // get the found Address Objects

                List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                for (Address a : addresses) {
                    Log.d("mytag: location:", a.toString());
                    if (a.hasLatitude() && a.hasLongitude()) {
                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    }
                }
                if (ll.size() > 0){
                    LatLng latLng = new LatLng(ll.get(0).latitude, ll.get(0).longitude);
                    Log.d("mytag:LatLng:", latLng.toString());
                    CameraPosition target = CameraPosition.builder().target(latLng).zoom(16).build();
                    mMap.moveCamera((CameraUpdateFactory.newCameraPosition(target)));
                }
            } catch (IOException e) {
                // handle the exception
                e.printStackTrace();
            }
        }

        return root;
    }
}
