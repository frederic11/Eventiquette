package com.frederictech.eventiquette;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.auth.data.model.Resource;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup;
import com.nex3z.togglebuttongroup.button.OnCheckedChangeListener;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AddEventActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    int PLACE_PICKER_REQUEST = 200;
    int READ_REQUEST_CODE = 300;
    int PIX_REQUEST_CODE = 400;
    int NUMBER_OF_PIX_TO_SELECT = 4;

    Context context;

    MapView eventLocationMap;
    GoogleMap map;
    Marker mNDUFacultyOfScienceMarker;
    Marker mUserLocationMarker;
    Drawable initialBtnSelectLocationBackgroundDrawable;
    ProgressDialog nDialog;

    LatLng userSelectedPlaceLatLng = null;

    EditText editTextEventTitle;
    EditText editTextEventShortDescription;
    EditText editTextEventDescription;
    Spinner spinnerEventType;
    EditText editTextEventWebsite;
    com.nex3z.togglebuttongroup.button.CircularToggle toggleIsMonday;
    com.nex3z.togglebuttongroup.button.CircularToggle toggleIsTuesday;
    com.nex3z.togglebuttongroup.button.CircularToggle toggleIsWednesday;
    com.nex3z.togglebuttongroup.button.CircularToggle toggleIsThursday;
    com.nex3z.togglebuttongroup.button.CircularToggle toggleIsFriday;
    com.nex3z.togglebuttongroup.button.CircularToggle toggleIsSaturday;
    com.nex3z.togglebuttongroup.button.CircularToggle toggleIsSunday;
    EditText editTextAgeLimit;
    EditText editTextTicketingUrl;
    Switch switchIsPrivate;

    EditText editTextEventLocationName;
    com.rengwuxian.materialedittext.MaterialEditText textViewStartDatePicker;
    com.rengwuxian.materialedittext.MaterialEditText textViewEndDatePicker;
    com.rengwuxian.materialedittext.MaterialEditText textViewReccuranceType;
    TextView textViewUntilDatePicker;
    Button btnSelectPhoto;
    ImageView imageViewEventPhoto;
    Switch switchEventIsRecurrent;
    EditText editTextEventReservationNumber;

    LinearLayout linearLayoutEndDate;
    LinearLayout linearLayoutUntilDate;
    MultiSelectToggleGroup muliselectGroupWeekdays;

    ImageView imgViewImage0;
    ImageView imgViewImage1;
    ImageView imgViewImage2;
    ImageView imgViewImage3;


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

        context = this;

        linearLayoutEndDate = (LinearLayout) findViewById(R.id.linear_layout_end_date);
        linearLayoutUntilDate = (LinearLayout) findViewById(R.id.linear_layout_until_date);
        muliselectGroupWeekdays = (MultiSelectToggleGroup) findViewById(R.id.multiselect_group_weekdays);


        // Gets the MapView from the XML layout and creates it
        eventLocationMap = (MapView) findViewById(R.id.map_event_location);
        eventLocationMap.onCreate(savedInstanceState);
        eventLocationMap.setClickable(true);
        eventLocationMap.getMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextEventLocationName = (EditText) findViewById(R.id.edit_event_location);
        textViewStartDatePicker = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.text_event_start_date_picker);
        textViewEndDatePicker = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.text_event_end_date_picker);
        textViewReccuranceType = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.text_event_recurrence_type);
        textViewUntilDatePicker = (TextView) findViewById(R.id.text_event_until_date_picker);
        btnSelectPhoto = (Button) findViewById(R.id.image_view_event_image_select);
        imageViewEventPhoto = (ImageView) findViewById(R.id.image_view_event_image);
        switchEventIsRecurrent = (Switch) findViewById(R.id.switch_event_is_recurrent);
        editTextEventReservationNumber = (EditText) findViewById(R.id.edit_event_reservation_number);
        editTextEventTitle = (EditText) findViewById(R.id.edit_event_title);
        editTextEventShortDescription = (EditText) findViewById(R.id.edit_event_short_description);
        editTextEventDescription = (EditText) findViewById(R.id.edit_event_description);
        spinnerEventType = (Spinner) findViewById(R.id.spinner_event_type);
        editTextEventWebsite = (EditText) findViewById(R.id.edit_event_website);
        toggleIsMonday = (com.nex3z.togglebuttongroup.button.CircularToggle) findViewById(R.id.toggle_button_monday);
        toggleIsTuesday = (com.nex3z.togglebuttongroup.button.CircularToggle) findViewById(R.id.toggle_button_tuesday);
        toggleIsWednesday = (com.nex3z.togglebuttongroup.button.CircularToggle) findViewById(R.id.toggle_button_wednesday);
        toggleIsThursday = (com.nex3z.togglebuttongroup.button.CircularToggle) findViewById(R.id.toggle_button_thurday);
        toggleIsFriday = (com.nex3z.togglebuttongroup.button.CircularToggle) findViewById(R.id.toggle_button_friday);
        toggleIsSaturday = (com.nex3z.togglebuttongroup.button.CircularToggle) findViewById(R.id.toggle_button_saturday);
        toggleIsSunday = (com.nex3z.togglebuttongroup.button.CircularToggle) findViewById(R.id.toggle_button_sunday);
        editTextAgeLimit = (EditText) findViewById(R.id.edit_event_age_limit);
        editTextTicketingUrl = (EditText) findViewById(R.id.edit_event_ticket_url);
        switchIsPrivate = (Switch) findViewById(R.id.switch_event_is_private);

        imgViewImage0 = (ImageView) findViewById(R.id.image_view_event_image_0);
        imgViewImage1 = (ImageView) findViewById(R.id.image_view_event_image_1);
        imgViewImage2 = (ImageView) findViewById(R.id.image_view_event_image_2);
        imgViewImage3 = (ImageView) findViewById(R.id.image_view_event_image_3);

        HideRecurrentEventSection();

        nDialog = new ProgressDialog(AddEventActivity.this);
        nDialog.setMessage("Loading...");
        nDialog.setIndeterminate(true);
        nDialog.setCancelable(false);

        editTextEventReservationNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher("LB"));

        switchEventIsRecurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ShowRecurrentEventSection();
                }
                else{
                    HideRecurrentEventSection();
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

        View.OnClickListener eventImageListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nDialog.show();
                Pix.start(AddEventActivity.this, PIX_REQUEST_CODE, NUMBER_OF_PIX_TO_SELECT);
            }
        };

        imgViewImage0.setOnClickListener(eventImageListener);
        imgViewImage1.setOnClickListener(eventImageListener);
        imgViewImage2.setOnClickListener(eventImageListener);
        imgViewImage3.setOnClickListener(eventImageListener);

        textViewReccuranceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<RecurrenceTypeOptions> adapter = new RecurrenceTypeAdapter(context, loadDonationOptions());
                new LovelyChoiceDialog(context)
                        .setTopColorRes(R.color.colorAccent)
                        .setIcon(R.drawable.baseline_autorenew_black_36dp)
                        .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<RecurrenceTypeOptions>() {
                            @Override
                            public void onItemSelected(int position, RecurrenceTypeOptions item) {
                                Toast.makeText(context, item.amount,Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }

    private List<RecurrenceTypeOptions> loadDonationOptions() {
        List<RecurrenceTypeOptions> result = new ArrayList<>();
        String[] raw = getResources().getStringArray(R.array.donations);
        for (String op : raw) {
            String[] info = op.split("%");
            result.add(new RecurrenceTypeOptions(info[1], info[0]));
        }
        return result;
    }

    private void ShowRecurrentEventSection() {
        textViewEndDatePicker.setVisibility(View.GONE);
        linearLayoutUntilDate.setVisibility(View.VISIBLE);
    }

    private void HideRecurrentEventSection() {
        textViewEndDatePicker.setVisibility(View.VISIBLE);
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
            findViewById(id).setEnabled(false);
            nDialog.show();
            SubmitEventForApproval();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SubmitEventForApproval() {
        boolean isValid = true;
        Event userEvent = new Event();

        if (userSelectedPlaceLatLng != null){
            userEvent.setLocationLatitude(userSelectedPlaceLatLng.latitude);
            userEvent.setLocationLongitude(userSelectedPlaceLatLng.longitude);
        } else {
            isValid = false;
        }

        if (editTextEventTitle != null && !editTextEventTitle.getText().toString().trim().equals("") ) {
            userEvent.setTitle(editTextEventTitle.getText().toString().trim());
            editTextEventTitle.setError(null);
        } else {
            isValid = false;
            editTextEventTitle.setError("This field can't be Empty");
        }

        if (editTextEventLocationName != null && !editTextEventLocationName.getText().toString().trim().equals("") ) {
            userEvent.setLocationName(editTextEventLocationName.getText().toString().trim());
            editTextEventLocationName.setError(null);
        } else {
            isValid = false;
            editTextEventLocationName.setError("This field can't be Empty");
        }

        if (editTextEventShortDescription != null && !editTextEventShortDescription.getText().toString().trim().equals("") ) {
            userEvent.setShortDescription(editTextEventShortDescription.getText().toString().trim());
            editTextEventShortDescription.setError(null);
        } else {
            isValid = false;
            editTextEventShortDescription.setError("This field can't be Empty");
        }

        if (editTextEventDescription != null && !editTextEventDescription.getText().toString().trim().equals("") ) {
            userEvent.setDescription(editTextEventDescription.getText().toString().trim());
            editTextEventDescription.setError(null);
        } else {
            isValid = false;
            editTextEventDescription.setError("This field can't be Empty");
        }

        if (spinnerEventType != null && !spinnerEventType.getSelectedItem().toString().trim().equals("") ) {
            userEvent.setType(spinnerEventType.getSelectedItem().toString().trim());
        } else {
            isValid = false;
        }

        if (editTextEventWebsite != null && !editTextEventWebsite.getText().toString().trim().equals("") ) {
            userEvent.setUrl(editTextEventWebsite.getText().toString().trim());
        }

        if (switchEventIsRecurrent != null && switchEventIsRecurrent.isChecked()){
            userEvent.setRecurrent(true);
        } else if (switchEventIsRecurrent != null && !switchEventIsRecurrent.isChecked()) {
            userEvent.setRecurrent(false);
        }

        if (eventStartDate != null) {
            userEvent.setStartDateTime(eventStartDate.getTime());
            textViewStartDatePicker.setError(null);
        } else {
            textViewStartDatePicker.setError("Please Select a Date");
            isValid = false;
        }

        if (eventEndDate != null && !switchEventIsRecurrent.isChecked()) {
            userEvent.setEndDatetime(eventEndDate.getTime());
            textViewEndDatePicker.setError(null);
        } else if (eventEndDate == null && !switchEventIsRecurrent.isChecked()) {
            textViewEndDatePicker.setError("Please Select a Date");
            isValid = false;
        }

        if (eventUntilDate != null && switchEventIsRecurrent.isChecked()) {
            userEvent.setUntilDateTime(eventUntilDate.getTime());
            textViewUntilDatePicker.setError(null);
        } else if (eventUntilDate == null && switchEventIsRecurrent.isChecked()) {
            textViewUntilDatePicker.setError("Please Select a Date");
            isValid = false;
        }

        userEvent.setOnMonday(toggleIsMonday.isChecked());
        userEvent.setOnTuesday(toggleIsTuesday.isChecked());
        userEvent.setOnWednesday(toggleIsWednesday.isChecked());
        userEvent.setOnThursday(toggleIsThursday.isChecked());
        userEvent.setOnFriday(toggleIsFriday.isChecked());
        userEvent.setOnSaturday(toggleIsSaturday.isChecked());
        userEvent.setOnSunday(toggleIsSunday.isChecked());

        if (editTextAgeLimit != null && !editTextAgeLimit.getText().toString().trim().equals("") ) {
            try{
                userEvent.setAgeLimit(Integer.parseInt(editTextAgeLimit.getText().toString().trim()));
            } catch (Exception ex){
                userEvent.setAgeLimit(0);
            }
        } else {
            userEvent.setAgeLimit(0);
        }

        if (editTextEventReservationNumber != null && !editTextEventReservationNumber.getText().toString().trim().equals("") ) {
            userEvent.setReservationNumber(editTextEventReservationNumber.getText().toString().trim());
            editTextEventReservationNumber.setError(null);
        } else {
            isValid = false;
            editTextEventReservationNumber.setError("This field can't be Empty");
        }

        if (editTextTicketingUrl != null && !editTextTicketingUrl.getText().toString().trim().equals("") ) {
            userEvent.setTicketUrl(editTextTicketingUrl.getText().toString().trim());
        }

        if(switchIsPrivate != null)
            userEvent.setPrivate(switchIsPrivate.isChecked());

        findViewById(R.id.action_submit).setEnabled(true);
        nDialog.cancel();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            userEvent.setCreatedBy(auth.getCurrentUser().getUid());
        }

        userEvent.setCreatedDate(new Date().getTime());

        if(isValid){
            ShowConfirmationDialogue();
        }
    }

    private void ShowConfirmationDialogue() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Event Submitted for Approval")
                .setMessage("Thank you for creating an Event. Your Event has been submitted for approval.\n\n" +
                        "You will receive a notification when this Event gets Accepted or Rejected.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng mNDUFacultyOfScienceLatLng = new LatLng(33.949394, 35.611142);
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLng(mNDUFacultyOfScienceLatLng));
        map.moveCamera(CameraUpdateFactory.zoomTo(17.0f));
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    eventLocationMap.setClickable(false);
                    nDialog.show();
                    startActivityForResult(builder.build(AddEventActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    eventLocationMap.setClickable(true);
                    nDialog.cancel();
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    eventLocationMap.setClickable(true);
                    nDialog.cancel();
                    e.printStackTrace();
                }
            }
        });
        mNDUFacultyOfScienceMarker = map.addMarker(new MarkerOptions().position(mNDUFacultyOfScienceLatLng));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                userSelectedPlaceLatLng = place.getLatLng();
                mNDUFacultyOfScienceMarker.remove();
                if(mUserLocationMarker != null)
                    mUserLocationMarker.remove();
                mUserLocationMarker = map.addMarker(new MarkerOptions().position(place.getLatLng()));
                map.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                editTextEventLocationName.setText(place.getName());
                eventLocationMap.setClickable(true);
                nDialog.cancel();
            }
            if (resultCode == RESULT_CANCELED){
                eventLocationMap.setClickable(true);
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

        if (requestCode == PIX_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                if(returnValue != null){
                    if(returnValue.size() >= 1)
                    if(returnValue.get(0) != null){
                        Picasso.get().load("file://" + returnValue.get(0)).into(imgViewImage0);
                    }
                    if(returnValue.size() >= 2)
                    if(returnValue.get(1) != null){
                        Picasso.get().load("file://" + returnValue.get(1)).into(imgViewImage1);
                    }
                    if(returnValue.size() >= 3)
                    if(returnValue.get(2) != null){
                        Picasso.get().load("file://" + returnValue.get(2)).into(imgViewImage2);
                    }
                    if(returnValue.size() >= 4)
                    if(returnValue.get(3) != null){
                        Picasso.get().load("file://" + returnValue.get(3)).into(imgViewImage3);
                    }
                }
                nDialog.cancel();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(AddEventActivity.this, PIX_REQUEST_CODE, NUMBER_OF_PIX_TO_SELECT);
                } else {
                    Toast.makeText(AddEventActivity.this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

}
