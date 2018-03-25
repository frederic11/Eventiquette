package com.frederictech.eventiquette;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    int PLACE_PICKER_REQUEST = 200;
    int READ_REQUEST_CODE = 300;

    MapView eventLocationMap;
    GoogleMap map;
    Marker mNDUFacultyOfScienceMarker;
    Marker mUserLocationMarker;
    ProgressDialog nDialog;

    Button btnSelectLocation;
    EditText editTextEventLocationName;
    TextView textViewStartDatePicker;
    TextView textViewEndDatePicker;
    Button btnSelectPhoto;
    ImageView imageViewEventPhoto;

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
        textViewStartDatePicker = (TextView) findViewById(R.id.text_event_start_date_picker);
        textViewEndDatePicker = (TextView) findViewById(R.id.text_event_end_date_picker);
        btnSelectPhoto = (Button) findViewById(R.id.image_view_event_image_select);
        imageViewEventPhoto = (ImageView) findViewById(R.id.image_view_event_image);


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

        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSelectPhoto.setEnabled(false);
                nDialog.show();
                PerformFileSearch();
            }
        });

        textViewStartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewStartDatePicker.setEnabled(false);
                nDialog.show();
                showStartDateTimePicker();
            }
        });

        textViewEndDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewStartDatePicker.setEnabled(false);
                nDialog.show();
                showEndDateTimePicker();
            }
        });
    }

    private void PerformFileSearch() {
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    private void showStartDateTimePicker() {
        // Initialize DateTimePicker Start
        SwitchDateTimeDialogFragment dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "Event Start Date",
                "OK",
                "Cancel"
        );

        final SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", java.util.Locale.getDefault());
        // Assign values
        dateTimeDialogFragment.startAtCalendarView();
        dateTimeDialogFragment.set24HoursMode(false);
        dateTimeDialogFragment.setMinimumDateTime(new Date());
        dateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2050, Calendar.DECEMBER, 31).getTime());
        dateTimeDialogFragment.setDefaultDateTime(new Date());

        // Define new day and month format
        try {
            dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MM yyyy", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e("dateTimeDialogFragment", e.getMessage());
        }

        // Set listener
        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                // Date is get on positive button click
                textViewStartDatePicker.setEnabled(true);
                nDialog.cancel();
                textViewStartDatePicker.setText(myDateFormat.format(date));
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Date is get on negative button click
                textViewStartDatePicker.setEnabled(true);
                nDialog.cancel();
            }
        });


        dateTimeDialogFragment.show(getSupportFragmentManager(), "dialog_time");
    }

    private void showEndDateTimePicker() {
        // Initialize DateTimePicker Start
        SwitchDateTimeDialogFragment dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "Event End Date",
                "OK",
                "Cancel"
        );

        final SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", java.util.Locale.getDefault());
        // Assign values
        dateTimeDialogFragment.startAtCalendarView();
        dateTimeDialogFragment.set24HoursMode(false);
        dateTimeDialogFragment.setMinimumDateTime(new Date());
        dateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2050, Calendar.DECEMBER, 31).getTime());
        dateTimeDialogFragment.setDefaultDateTime(new Date());

        // Define new day and month format
        try {
            dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MM yyyy", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e("dateTimeDialogFragment", e.getMessage());
        }

        // Set listener
        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                // Date is get on positive button click
                textViewStartDatePicker.setEnabled(true);
                nDialog.cancel();
                textViewEndDatePicker.setText(myDateFormat.format(date));
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Date is get on negative button click
                textViewEndDatePicker.setEnabled(true);
                nDialog.cancel();
            }
        });


        dateTimeDialogFragment.show(getSupportFragmentManager(), "dialog_time");
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

        if (requestCode == READ_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // The document selected by the user won't be returned in the intent.
                // Instead, a URI to that document will be contained in the return intent
                // provided to this method as a parameter.
                // Pull that URI using resultData.getData().

                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                    Picasso.get().load(uri).fit().centerCrop().into(imageViewEventPhoto);
                }
                nDialog.cancel();
                btnSelectPhoto.setEnabled(true);
            }
            if (resultCode == RESULT_CANCELED) {
                nDialog.cancel();
                btnSelectPhoto.setEnabled(true);
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
