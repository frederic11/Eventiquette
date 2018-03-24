package com.frederictech.eventiquette;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.login.Login;
import com.facebook.places.Places;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddEventActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    int PLACE_PICKER_REQUEST = 200;

    MapView eventLocationMap;
    GoogleMap map;
    Marker mNDUFacultyOfScienceMarker;
    Marker mUserLocationMarker;
    ProgressDialog nDialog;

    Button btnSelectLocation;
    EditText editTextEventLocationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Event");

        // Gets the MapView from the XML layout and creates it
        eventLocationMap = (MapView) findViewById(R.id.map_event_location);
        eventLocationMap.onCreate(savedInstanceState);
        eventLocationMap.setClickable(false);
        eventLocationMap.getMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSelectLocation = (Button) findViewById(R.id.map_select_location);
        editTextEventLocationName = (EditText) findViewById(R.id.edit_event_location);

        nDialog = new ProgressDialog(AddEventActivity.this);
        nDialog.setMessage("Loading...");
        nDialog.setIndeterminate(true);
        nDialog.setCancelable(false);

        btnSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    btnSelectLocation.setEnabled(false);
                    nDialog.show();
                    startActivityForResult(builder.build(AddEventActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    btnSelectLocation.setEnabled(true);
                    nDialog.cancel();
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    btnSelectLocation.setEnabled(true);
                    nDialog.cancel();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_submit) {
            return true;
        }
        if (id == R.id.action_cancel) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng mNDUFacultyOfScienceLatLng = new LatLng(33.949394, 35.611142);
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLng(mNDUFacultyOfScienceLatLng));
        map.moveCamera(CameraUpdateFactory.zoomTo(17.0f));
        mNDUFacultyOfScienceMarker = map.addMarker(new MarkerOptions().position(mNDUFacultyOfScienceLatLng));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                mNDUFacultyOfScienceMarker.remove();
                if(mUserLocationMarker != null)
                    mUserLocationMarker.remove();
                mUserLocationMarker = map.addMarker(new MarkerOptions().position(place.getLatLng()));
                map.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                editTextEventLocationName.setText(place.getName());
                btnSelectLocation.setEnabled(true);
                nDialog.cancel();
            }
            if (resultCode == RESULT_CANCELED){
                btnSelectLocation.setEnabled(true);
                nDialog.cancel();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        eventLocationMap.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        eventLocationMap.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventLocationMap.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        eventLocationMap.onLowMemory();
    }

}
