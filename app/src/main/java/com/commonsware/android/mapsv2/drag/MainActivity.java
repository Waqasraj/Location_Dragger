/***
  Copyright (c) 2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  Covered in detail in the book _The Busy Coder's Guide to Android Development_
    https://commonsware.com/Android
 */

package com.commonsware.android.mapsv2.drag;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AbstractMapActivity implements
    OnMapReadyCallback, OnInfoWindowClickListener,
    OnMarkerDragListener {
  private boolean needsInit=false;
  private  GoogleMap map = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (readyToGo()) {
      setContentView(R.layout.activity_main);

      MapFragment mapFrag=
          (MapFragment)getFragmentManager().findFragmentById(R.id.map);

      if (savedInstanceState == null) {
        needsInit=true;
      }

      mapFrag.setRetainInstance(true);
      mapFrag.getMapAsync(this);
    }
  }

  @Override
  public void onMapReady(GoogleMap map) {
    if (needsInit) {
      CameraUpdate center=
          CameraUpdateFactory.newLatLng(new LatLng(12.9773,
                  77.5712));
      CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

      map.moveCamera(center);
      map.animateCamera(zoom);

      /*addMarker(map, 12.9615, 77.6157,
                R.string.rc, R.string.race_course);*/


      addMarker(map, 12.9773, 77.5712,
                R.string.majestic,
                R.string.majestic_snippet);
      /*addMarker(map, 12.9706255, 77.5776951,
                R.string.chickpet, R.string.chickpet_street);
      addMarker(map, 12.9775, 77.5685,
                R.string.bangalore_railway_station, R.string.bangalore_railway_station);*/
    }

    map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
    map.setOnInfoWindowClickListener(this);
    map.setOnMarkerDragListener(this);
  }

  @Override
  public void onInfoWindowClick(Marker marker) {
    Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
  }

  @Override
  public void onMarkerDragStart(Marker marker) {
    LatLng position=marker.getPosition();

    Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f",
                                                    position.latitude,
                                                    position.longitude));

    Toast.makeText(MainActivity.this,String.format("Drag from %f:%f",
            position.latitude,
            position.longitude),Toast.LENGTH_LONG).show();
  }

  @Override
  public void onMarkerDrag(Marker marker) {
    LatLng position=marker.getPosition();

    Log.d(getClass().getSimpleName(),
          String.format("Dragging to %f:%f", position.latitude,
                        position.longitude));

    /*Toast.makeText(MainActivity.this,String.format("Dragging to %f:%f",
            position.latitude,
            position.longitude),Toast.LENGTH_LONG).show();*/



  }

  @Override
  public void onMarkerDragEnd(Marker marker) {
    LatLng position=marker.getPosition();

    Log.d(getClass().getSimpleName(), String.format("Dragged to %f:%f",
        position.latitude,
        position.longitude));


    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());


    List<Address> addresses;
    try {
      addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1);
      String address = addresses.get(0).getAddressLine(0);
      String city = addresses.get(0).getLocality();

      Toast.makeText(MainActivity.this,"Address: "+
              address + " " +city,Toast.LENGTH_LONG).show();

      //addMarker();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void addMarker(GoogleMap map, double lat, double lon,
                         int title, int snippet) {
    map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                                     .title(getString(title))
                                     .snippet(getString(snippet))
                                     .draggable(true));


  }
}
