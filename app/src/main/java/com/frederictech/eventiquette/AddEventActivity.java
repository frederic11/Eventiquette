package com.frederictech.eventiquette;

import android.app.Activity;
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
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


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
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup;
import com.nex3z.togglebuttongroup.button.OnCheckedChangeListener;
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
    TextView textViewUntilDatePicker;
    Button btnSelectPhoto;
    ImageView imageViewEventPhoto;
    Switch switchEventIsRecurrent;

    LinearLayout linearLayoutEndDate;
    LinearLayout linearLayoutUntilDate;
    LinearLayout linearLayoutRecurrentEvery;
    MultiSelectToggleGroup muliselectGroupWeekdays;

    com.nex3z.togglebuttongroup.button.CircularToggle toggleButtonDay;
    com.nex3z.togglebuttongroup.button.CircularToggle toggleButtonWeek;


    Date eventStartDate;
    Date eventEndDate;
    Date eventUntilDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Event");

        linearLayoutEndDate = (LinearLayout) findViewById(R.id.linear_layout_end_date);
        linearLayoutUntilDate = (LinearLayout) findViewById(R.id.linear_layout_until_date);
        linearLayoutRecurrentEvery = (LinearLayout) findViewById(R.id.linear_layout_recurrent_every);
        muliselectGroupWeekdays = (MultiSelectToggleGroup) findViewById(R.id.muliselect_group_weekdays);
        toggleButtonDay = (com.nex3z.togglebuttongroup.button.CircularToggle) findViewById(R.id.toggle_button_every_day);
        toggleButtonWeek = (com.nex3z.togglebuttongroup.button.CircularToggle) findViewById(R.id.toggle_button_every_week);
        HideRecurrentEventSection();

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
        textViewUntilDatePicker = (TextView) findViewById(R.id.text_event_until_date_picker);
        btnSelectPhoto = (Button) findViewById(R.id.image_view_event_image_select);
        imageViewEventPhoto = (ImageView) findViewById(R.id.image_view_event_image);
        switchEventIsRecurrent = (Switch) findViewById(R.id.switch_event_is_recurrent);


        nDialog = new ProgressDialog(AddEventActivity.this);
        nDialog.setMessage("Loading...");
        nDialog.setIndeterminate(true);
        nDialog.setCancelable(false);

        switchEventIsRecurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    toggleButtonDay.setChecked(true);
                    ShowRecurrentEventSection();
                }
                else{
                    HideRecurrentEventSection();
                }
            }
        });

        toggleButtonDay.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public <T extends View & Checkable> void onCheckedChanged(T view, boolean isChecked) {
                if(isChecked){
                    toggleButtonWeek.setChecked(false);
                    muliselectGroupWeekdays.setVisibility(View.GONE);
                } else {
                    toggleButtonWeek.setChecked(true);
                    muliselectGroupWeekdays.setVisibility(View.VISIBLE);
                }
            }
        });

        toggleButtonWeek.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public <T extends View & Checkable> void onCheckedChanged(T view, boolean isChecked) {
                if(isChecked){
                    toggleButtonDay.setChecked(false);
                    muliselectGroupWeekdays.setVisibility(View.VISIBLE);
                } else {
                    toggleButtonDay.setChecked(true);
                    muliselectGroupWeekdays.setVisibility(View.GONE);
                }
            }
        });

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
                textViewEndDatePicker.setEnabled(false);
                nDialog.show();
                showEndDateTimePicker();
            }
        });

        textViewUntilDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewUntilDatePicker.setEnabled(false);
                nDialog.show();
                showUntilDateTimePicker();
            }
        });
    }

    private void ShowRecurrentEventSection() {
        linearLayoutEndDate.setVisibility(View.GONE);
        linearLayoutRecurrentEvery.setVisibility(View.VISIBLE);
        linearLayoutUntilDate.setVisibility(View.VISIBLE);
    }

    private void HideRecurrentEventSection() {
        linearLayoutEndDate.setVisibility(View.VISIBLE);
        linearLayoutRecurrentEvery.setVisibility(View.GONE);
        muliselectGroupWeekdays.setVisibility(View.GONE);
        linearLayoutUntilDate.setVisibility(View.GONE);
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
                eventStartDate = date;
                eventEndDate = null;
                eventUntilDate = null;
                textViewEndDatePicker.setEnabled(true);
                textViewEndDatePicker.setText("");
                textViewUntilDatePicker.setEnabled(true);
                textViewUntilDatePicker.setText("");
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

        if (eventStartDate == null) {
            eventStartDate = new Date();
        }

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(eventStartDate); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour

        Calendar cal2 = Calendar.getInstance(); // creates calendar
        cal2.setTime(eventStartDate); // sets calendar time/date
        cal2.add(Calendar.MINUTE, 15); // adds 15 minutes

        final SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", java.util.Locale.getDefault());
        // Assign values
        dateTimeDialogFragment.startAtTimeView();
        dateTimeDialogFragment.set24HoursMode(false);
        dateTimeDialogFragment.setMinimumDateTime(cal2.getTime());//eventStartDate + 15 minutes
        dateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2050, Calendar.DECEMBER, 31).getTime());
        dateTimeDialogFragment.setDefaultDateTime(cal.getTime()); //eventStartDate + One hour

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
                if (date.before(eventStartDate)) {
                    eventEndDate = null;
                    showSnackBar(AddEventActivity.this,"Event End Date can't be before it's Start Date.");
                } else {
                    eventEndDate = date;
                    textViewEndDatePicker.setText(myDateFormat.format(date));
                }
                textViewEndDatePicker.setEnabled(true);
                nDialog.cancel();
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

    private void showUntilDateTimePicker() {// Initialize DateTimePicker Start
        SwitchDateTimeDialogFragment dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "Event Until Date",
                "OK",
                "Cancel"
        );

        if (eventStartDate == null) {
            eventStartDate = new Date();
        }

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(eventStartDate); // sets calendar time/date
        cal.add(Calendar.DAY_OF_MONTH, 3); // adds 3 days

        Calendar cal2 = Calendar.getInstance(); // creates calendar
        cal2.setTime(eventStartDate); // sets calendar time/date
        cal2.add(Calendar.DAY_OF_MONTH, 1); // adds 1 days

        final SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", java.util.Locale.getDefault());
        // Assign values
        dateTimeDialogFragment.startAtCalendarView();
        dateTimeDialogFragment.set24HoursMode(false);
        dateTimeDialogFragment.setMinimumDateTime(cal2.getTime());//eventStartDate + 15 minutes
        dateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2050, Calendar.DECEMBER, 31).getTime());
        dateTimeDialogFragment.setDefaultDateTime(cal.getTime()); //eventStartDate + One hour

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
                if (date.before(eventStartDate)) {
                    eventUntilDate = null;
                    showSnackBar(AddEventActivity.this,"Event End Date can't be before it's Start Date.");
                } else {
                    eventUntilDate = date;
                    textViewUntilDatePicker.setText(myDateFormat.format(date));
                }
                textViewUntilDatePicker.setEnabled(true);
                nDialog.cancel();
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Date is get on negative button click
                textViewUntilDatePicker.setEnabled(true);
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

    public void showSnackBar(Activity activity, String message){
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
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
