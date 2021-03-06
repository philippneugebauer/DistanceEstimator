package it.unibz.inf.ait.distanceestimatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import java.util.List;

import it.unibz.inf.ait.distanceestimatorapp.database.DatabaseManager;
import it.unibz.inf.ait.distanceestimatorapp.map.RouteOverlay;
import it.unibz.inf.ait.distanceestimatorapp.model.MyLocation;

/**
 * Copyright 2014 Philipp Neugebauer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class TrainingMapActivity extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_map);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        MapController mapController = mapView.getController();
        mapController.setZoom(15);

        Intent intent = getIntent();
        int id = Integer.valueOf(intent
                .getStringExtra(MainActivity.EXTRA_MESSAGE));
        DatabaseManager db = new DatabaseManager(this);
        List<MyLocation> locationList = db.getAllLocationsByTraining(id);
        RouteOverlay overlay = new RouteOverlay();

        for (int i = 0; i < locationList.size(); i++) {
            MyLocation location = locationList.get(i);
            int lat = (int) (location.getLatitude() * 1E6);
            int lng = (int) (location.getLongitude() * 1E6);
            GeoPoint geoPoint = new GeoPoint(lat, lng);
            if (i == 0) {
                mapController.animateTo(geoPoint);
            }
            overlay.addGeoPoint(geoPoint);
        }

        mapView.getOverlays().add(overlay);

        if (locationList.size() < 1) {
            showToast("There is no data available to show!");
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_training_map, menu);
        return true;
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
