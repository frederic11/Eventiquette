package com.frederictech.eventiquette;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddEventActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    int PLACE_PICKER_REQUEST = 200;
    int READ_REQUEST_CODE = 300;

    FirebaseFirestore db;
    static final String EVENTS_COLLECTION_FULL_DETAILS = "eventsCollectionFullDetails";
    static final String EVENTS_COLLECTION_TEMPLATES = "eventsCollectionTemplates";

    Context context;

    MapView eventLocationMap;
    GoogleMap map;
    Marker mNDUFacultyOfScienceMarker;
    Marker mUserLocationMarker;
    ProgressDialog nDialog;

    LatLng userSelectedPlaceLatLng = null;

    TextView textViewMapsError;
    TextView textViewPickDayError;
    EditText editTextEventTitle;
    EditText editTextEventShortDescription;
    EditText editTextEventDescription;
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

    EditText editTextEventLocationName;
    com.rengwuxian.materialedittext.MaterialEditText textViewStartDatePicker;
    com.rengwuxian.materialedittext.MaterialEditText textViewEndDatePicker;
    com.rengwuxian.materialedittext.MaterialEditText textViewRecurrenceType;
    com.rengwuxian.materialedittext.MaterialEditText textViewEventType;
    com.rengwuxian.materialedittext.MaterialEditText textViewEventPrivacy;
    ImageView imageViewEventPhoto;
    EditText editTextEventReservationNumber;
    MultiSelectToggleGroup muliselectGroupWeekdays;


    Date eventStartDate;
    Date eventEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Event");

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        context = this;

        // Gets the MapView from the XML layout and creates it
        eventLocationMap = (MapView) findViewById(R.id.map_event_location);
        eventLocationMap.onCreate(savedInstanceState);
        eventLocationMap.setClickable(true);
        eventLocationMap.getMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewMapsError = (TextView) findViewById(R.id.text_view_map_error);
        textViewPickDayError = (TextView) findViewById(R.id.text_view_pick_day_error);
        editTextEventLocationName = (EditText) findViewById(R.id.edit_event_location);
        textViewStartDatePicker = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.text_event_start_date_picker);
        textViewEndDatePicker = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.text_event_end_date_picker);
        textViewRecurrenceType = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.text_event_recurrence_type);
        textViewEventType = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.text_event_type);
        textViewEventPrivacy = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.text_event_privacy);
        imageViewEventPhoto = (ImageView) findViewById(R.id.image_view_event_image);
        editTextEventReservationNumber = (EditText) findViewById(R.id.edit_event_reservation_number);
        editTextEventTitle = (EditText) findViewById(R.id.edit_event_title);
        editTextEventShortDescription = (EditText) findViewById(R.id.edit_event_short_description);
        editTextEventDescription = (EditText) findViewById(R.id.edit_event_description);
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
        muliselectGroupWeekdays = (MultiSelectToggleGroup) findViewById(R.id.multi_select_group_weekdays);

        HideRecurrentEventSection();

        nDialog = new ProgressDialog(AddEventActivity.this);
        nDialog.setMessage("Loading...");
        nDialog.setIndeterminate(true);
        nDialog.setCancelable(false);

        editTextEventReservationNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher("LB"));

        imageViewEventPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        textViewRecurrenceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewRecurrenceType.setEnabled(false);
                ArrayAdapter<RecurrenceTypeOptions> adapter = new RecurrenceTypeAdapter(context, loadRecurrenceOptions());
                new LovelyChoiceDialog(context)
                        .setTopColorRes(R.color.colorAccent)
                        .setIcon(R.drawable.baseline_autorenew_black_36dp)
                        .setCancelable(false)
                        .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<RecurrenceTypeOptions>() {
                            @Override
                            public void onItemSelected(int position, RecurrenceTypeOptions item) {
                                textViewRecurrenceType.setText(item.recurrenceType);
                                if (Objects.equals(item.recurrenceType, "Once")) {
                                    textViewPickDayError.setVisibility(View.GONE);
                                    HideRecurrentEventSection();
                                    textViewStartDatePicker.setText("");
                                    textViewEndDatePicker.setEnabled(false);
                                    textViewEndDatePicker.setHint("End Date and Time");
                                    textViewEndDatePicker.setText("");
                                    textViewRecurrenceType.setEnabled(true);
                                } else if (Objects.equals(item.recurrenceType, "Weekly")) {
                                    ShowRecurrentEventSection();
                                    textViewRecurrenceType.setEnabled(true);
                                    textViewStartDatePicker.setText("");
                                    textViewEndDatePicker.setEnabled(false);
                                    textViewEndDatePicker.setHint("Until");
                                    textViewEndDatePicker.setText("");
                                }
                            }
                        })
                        .show();
            }
        });

        textViewEventType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewEventType.setEnabled(false);
                ArrayAdapter<RecurrenceTypeOptions> adapter = new RecurrenceTypeAdapter(context, loadEventTypeOptions());
                new LovelyChoiceDialog(context)
                        .setTopColorRes(R.color.colorAccent)
                        .setIcon(R.drawable.baseline_local_offer_black_36dp)
                        .setTitle("Event Type")
                        .setCancelable(false)
                        .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<RecurrenceTypeOptions>() {
                            @Override
                            public void onItemSelected(int position, RecurrenceTypeOptions item) {
                                textViewEventType.setText(item.recurrenceType);
                                textViewEventType.setEnabled(true);
                            }
                        })
                        .show();
            }
        });

        textViewEventPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewEventPrivacy.setEnabled(false);
                ArrayAdapter<RecurrenceTypeOptions> adapter = new RecurrenceTypeAdapter(context, loadEventPrivacyOptions());
                new LovelyChoiceDialog(context)
                        .setTopColorRes(R.color.colorAccent)
                        .setIcon(R.drawable.baseline_lock_black_36dp)
                        .setCancelable(false)
                        .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<RecurrenceTypeOptions>() {
                            @Override
                            public void onItemSelected(int position, RecurrenceTypeOptions item) {
                                textViewEventPrivacy.setText(item.recurrenceType);
                                textViewEventPrivacy.setEnabled(true);
                            }
                        })
                        .show();
            }
        });

        Picasso.get().load(R.drawable.click_to_add_image).fit().centerCrop().into(imageViewEventPhoto);



        final String eventIdFromTemplateActivity;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                eventIdFromTemplateActivity = null;
            } else {
                eventIdFromTemplateActivity = extras.getString("TEMPLATE_EVENT_ID");
            }
        } else {
            eventIdFromTemplateActivity= (String) savedInstanceState.getSerializable("TEMPLATE_EVENT_ID");
        }

        if (eventIdFromTemplateActivity != null){
           nDialog.show();
            db.collection(EVENTS_COLLECTION_TEMPLATES)
                    .document(eventIdFromTemplateActivity)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d("Get Event", "DocumentSnapshot data: " + documentSnapshot.getId());
                            Event myEvent = documentSnapshot.toObject(Event.class);
                            showSnackBar(AddEventActivity.this, eventIdFromTemplateActivity);
                            editTextEventTitle.setText(myEvent.getTitle());
                            nDialog.cancel();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showSnackBar(AddEventActivity.this, "Oops! Something Went Wrong");
                            nDialog.cancel();
                        }
                    });
        }
    }

    private List<RecurrenceTypeOptions> loadRecurrenceOptions() {
        List<RecurrenceTypeOptions> result = new ArrayList<>();
        String[] raw = getResources().getStringArray(R.array.recurrence_type_populate);
        for (String op : raw) {
            result.add(new RecurrenceTypeOptions(op));
        }
        return result;
    }

    private List<RecurrenceTypeOptions> loadEventTypeOptions() {
        List<RecurrenceTypeOptions> result = new ArrayList<>();
        String[] raw = getResources().getStringArray(R.array.spinner_event_type_populate);
        for (String op : raw) {
            result.add(new RecurrenceTypeOptions(op));
        }
        return result;
    }

    private List<RecurrenceTypeOptions> loadEventPrivacyOptions() {
        List<RecurrenceTypeOptions> result = new ArrayList<>();
        String[] raw = getResources().getStringArray(R.array.event_privacy_populate);
        for (String op : raw) {
            result.add(new RecurrenceTypeOptions(op));
        }
        return result;
    }

    private void ShowRecurrentEventSection() {
        muliselectGroupWeekdays.setVisibility(View.VISIBLE);
    }

    private void HideRecurrentEventSection() {
        muliselectGroupWeekdays.setVisibility(View.GONE);
    }

    private void PerformFileSearch() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(16, 9)
                .setFixAspectRatio(true)
                .setBackgroundColor(R.color.colorPrimary)
                .setBorderCornerColor(R.color.colorAccent)
                .start(this);
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
                textViewEndDatePicker.setEnabled(true);
                textViewEndDatePicker.setText("");
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
                    showSnackBar(AddEventActivity.this, "Event End Date can't be before it's Start Date.");
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save_template) {
            nDialog.show();
            SaveEventAsTemplate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SubmitEventForApproval() {
        boolean isValid = true;
        boolean isTemplate = false;
        Event userEvent = new Event();

        if (userSelectedPlaceLatLng != null) {
            textViewMapsError.setVisibility(View.GONE);
            userEvent.setLocationLatitude(userSelectedPlaceLatLng.latitude);
            userEvent.setLocationLongitude(userSelectedPlaceLatLng.longitude);
        } else {
            textViewMapsError.setTextColor(Color.RED);
            textViewMapsError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (editTextEventTitle != null && !editTextEventTitle.getText().toString().trim().equals("")) {
            userEvent.setTitle(editTextEventTitle.getText().toString().trim());
            editTextEventTitle.setError(null);
        } else {
            isValid = false;
            editTextEventTitle.setError("This field can't be Empty");
        }

        if (editTextEventLocationName != null && !editTextEventLocationName.getText().toString().trim().equals("")) {
            userEvent.setLocationName(editTextEventLocationName.getText().toString().trim());
            editTextEventLocationName.setError(null);
        } else {
            isValid = false;
            editTextEventLocationName.setError("This field can't be Empty");
        }

        if (editTextEventReservationNumber != null && !editTextEventReservationNumber.getText().toString().trim().equals("")) {
            userEvent.setReservationNumber(editTextEventReservationNumber.getText().toString().trim());
            editTextEventReservationNumber.setError(null);
        } else {
            isValid = false;
            editTextEventReservationNumber.setError("This field can't be Empty");
        }

        if (textViewEventType != null && !textViewEventType.getText().toString().trim().equals("")) {
            userEvent.setType(textViewEventType.getText().toString().trim());
            textViewEventType.setError(null);
        } else {
            isValid = false;
            textViewEventType.setError("This field can't be Empty");
        }

        if (editTextEventShortDescription != null && !editTextEventShortDescription.getText().toString().trim().equals("")) {
            userEvent.setShortDescription(editTextEventShortDescription.getText().toString().trim());
            editTextEventShortDescription.setError(null);
        } else {
            isValid = false;
            editTextEventShortDescription.setError("This field can't be Empty");
        }

        if (editTextEventDescription != null && !editTextEventDescription.getText().toString().trim().equals("")) {
            userEvent.setDescription(editTextEventDescription.getText().toString().trim());
            editTextEventDescription.setError(null);
        } else {
            isValid = false;
            editTextEventDescription.setError("This field can't be Empty");
        }

        if (textViewRecurrenceType != null && !textViewRecurrenceType.getText().toString().trim().equals("")) {
            userEvent.setRecurrent(Objects.equals(textViewRecurrenceType.getText().toString().trim(), "Weekly"));
            textViewRecurrenceType.setError(null);
        } else {
            isValid = false;
            textViewRecurrenceType.setError("This field can't be Empty");
        }

        userEvent.setOnMonday(toggleIsMonday.isChecked());
        userEvent.setOnTuesday(toggleIsTuesday.isChecked());
        userEvent.setOnWednesday(toggleIsWednesday.isChecked());
        userEvent.setOnThursday(toggleIsThursday.isChecked());
        userEvent.setOnFriday(toggleIsFriday.isChecked());
        userEvent.setOnSaturday(toggleIsSaturday.isChecked());
        userEvent.setOnSunday(toggleIsSunday.isChecked());

        if (textViewRecurrenceType == null ||
                !userEvent.isRecurrent() ||
                (userEvent.isRecurrent()
                        && (userEvent.isOnMonday() ||
                        userEvent.isOnTuesday() ||
                        userEvent.isOnWednesday() ||
                        userEvent.isOnThursday() ||
                        userEvent.isOnFriday() ||
                        userEvent.isOnSaturday() ||
                        userEvent.isOnSunday()))) {
            textViewPickDayError.setVisibility(View.GONE);
        } else {
            isValid = false;
            textViewPickDayError.setTextColor(Color.RED);
            textViewPickDayError.setVisibility(View.VISIBLE);
        }

        if (eventStartDate != null) {
            userEvent.setStartDateTime(eventStartDate);
            textViewStartDatePicker.setError(null);
        } else {
            textViewStartDatePicker.setError("Please Select a Date");
            isValid = false;
        }

        if (eventEndDate != null) {
            userEvent.setEndDatetime(eventEndDate);
            textViewEndDatePicker.setError(null);
        } else {
            textViewEndDatePicker.setError("Please Select a Date");
            isValid = false;
        }

        if (editTextEventWebsite != null && !editTextEventWebsite.getText().toString().trim().equals("")) {
            userEvent.setUrl(editTextEventWebsite.getText().toString().trim());
        }

        if (editTextAgeLimit != null && !editTextAgeLimit.getText().toString().trim().equals("")) {
            try {
                userEvent.setAgeLimit(Integer.parseInt(editTextAgeLimit.getText().toString().trim()));
            } catch (Exception ex) {
                userEvent.setAgeLimit(0);
            }
        } else {
            userEvent.setAgeLimit(0);
        }

        if (editTextTicketingUrl != null && !editTextTicketingUrl.getText().toString().trim().equals("")) {
            userEvent.setTicketUrl(editTextTicketingUrl.getText().toString().trim());
        }

        if (textViewEventPrivacy != null && !textViewEventPrivacy.getText().toString().trim().equals("")) {
            userEvent.setPrivate(Objects.equals(textViewEventPrivacy.getText().toString().trim(), "Private"));
            textViewEventPrivacy.setError(null);
        } else {
            isValid = false;
            textViewEventPrivacy.setError("This field can't be Empty");
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userEvent.setCreatedBy(auth.getCurrentUser().getUid());
        }else {
            isValid = false;
        }

        userEvent.setCreatedDate(new Date());
        userEvent.setSource(getPackageName());

        //ToDo: Remove auto-approve
        userEvent.setApproved(true);

        findViewById(R.id.action_submit).setEnabled(true);
        nDialog.cancel();

        if (isValid) {
            WriteFullEventDetailsToDatabase(userEvent, isTemplate);
            ShowConfirmationDialogue();
        }
    }

    private void SaveEventAsTemplate() {
        Event userEvent = new Event();
        boolean isValid = true;
        boolean isTemplate = true;

        if (userSelectedPlaceLatLng != null) {
            textViewMapsError.setVisibility(View.GONE);
            userEvent.setLocationLatitude(userSelectedPlaceLatLng.latitude);
            userEvent.setLocationLongitude(userSelectedPlaceLatLng.longitude);
        }

        if (editTextEventTitle != null && !editTextEventTitle.getText().toString().trim().equals("")) {
            userEvent.setTitle(editTextEventTitle.getText().toString().trim());
            editTextEventTitle.setError(null);
        } else {
            isValid = false;
            editTextEventTitle.setError("This field can't be Empty");
        }

        if (editTextEventLocationName != null && !editTextEventLocationName.getText().toString().trim().equals("")) {
            userEvent.setLocationName(editTextEventLocationName.getText().toString().trim());
            editTextEventLocationName.setError(null);
        }

        if (editTextEventReservationNumber != null && !editTextEventReservationNumber.getText().toString().trim().equals("")) {
            userEvent.setReservationNumber(editTextEventReservationNumber.getText().toString().trim());
            editTextEventReservationNumber.setError(null);
        }

        if (textViewEventType != null && !textViewEventType.getText().toString().trim().equals("")) {
            userEvent.setType(textViewEventType.getText().toString().trim());
            textViewEventType.setError(null);
        }

        if (editTextEventShortDescription != null && !editTextEventShortDescription.getText().toString().trim().equals("")) {
            userEvent.setShortDescription(editTextEventShortDescription.getText().toString().trim());
            editTextEventShortDescription.setError(null);
        }

        if (editTextEventDescription != null && !editTextEventDescription.getText().toString().trim().equals("")) {
            userEvent.setDescription(editTextEventDescription.getText().toString().trim());
            editTextEventDescription.setError(null);
        }

        if (textViewRecurrenceType != null && !textViewRecurrenceType.getText().toString().trim().equals("")) {
            userEvent.setRecurrent(Objects.equals(textViewRecurrenceType.getText().toString().trim(), "Weekly"));
            textViewRecurrenceType.setError(null);
        }

        userEvent.setOnMonday(toggleIsMonday.isChecked());
        userEvent.setOnTuesday(toggleIsTuesday.isChecked());
        userEvent.setOnWednesday(toggleIsWednesday.isChecked());
        userEvent.setOnThursday(toggleIsThursday.isChecked());
        userEvent.setOnFriday(toggleIsFriday.isChecked());
        userEvent.setOnSaturday(toggleIsSaturday.isChecked());
        userEvent.setOnSunday(toggleIsSunday.isChecked());

        if (textViewRecurrenceType == null ||
                !userEvent.isRecurrent() ||
                (userEvent.isRecurrent()
                        && (userEvent.isOnMonday() ||
                        userEvent.isOnTuesday() ||
                        userEvent.isOnWednesday() ||
                        userEvent.isOnThursday() ||
                        userEvent.isOnFriday() ||
                        userEvent.isOnSaturday() ||
                        userEvent.isOnSunday()))) {
            textViewPickDayError.setVisibility(View.GONE);
        }

        if (eventStartDate != null) {
            userEvent.setStartDateTime(eventStartDate);
            textViewStartDatePicker.setError(null);
        }

        if (eventEndDate != null) {
            userEvent.setEndDatetime(eventEndDate);
            textViewEndDatePicker.setError(null);
        }

        if (editTextEventWebsite != null && !editTextEventWebsite.getText().toString().trim().equals("")) {
            userEvent.setUrl(editTextEventWebsite.getText().toString().trim());
        }

        if (editTextAgeLimit != null && !editTextAgeLimit.getText().toString().trim().equals("")) {
            try {
                userEvent.setAgeLimit(Integer.parseInt(editTextAgeLimit.getText().toString().trim()));
            } catch (Exception ex) {
                userEvent.setAgeLimit(0);
            }
        } else {
            userEvent.setAgeLimit(0);
        }

        if (editTextTicketingUrl != null && !editTextTicketingUrl.getText().toString().trim().equals("")) {
            userEvent.setTicketUrl(editTextTicketingUrl.getText().toString().trim());
        }

        if (textViewEventPrivacy != null && !textViewEventPrivacy.getText().toString().trim().equals("")) {
            userEvent.setPrivate(Objects.equals(textViewEventPrivacy.getText().toString().trim(), "Private"));
            textViewEventPrivacy.setError(null);
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userEvent.setCreatedBy(auth.getCurrentUser().getUid());
        }

        userEvent.setCreatedDate(new Date());
        userEvent.setSource(getPackageName());

        //findViewById(R.id.action_save_template).setEnabled(true);
        nDialog.cancel();


        if (isValid) {
            WriteFullEventDetailsToDatabase(userEvent, isTemplate);
            ShowTemplateSaveConfirmationDialogue();
        }
    }

    private void WriteFullEventDetailsToDatabase(final Event userEvent, boolean isTemplate) {
        db.collection(isTemplate ? EVENTS_COLLECTION_TEMPLATES : EVENTS_COLLECTION_FULL_DETAILS )
                .add(userEvent)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("DB Write", "DocumentSnapshot successfully written with ID:"
                                + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DB Write", "Error writing document", e);
                    }
                });
    }

    private void ShowConfirmationDialogue() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Event Submitted for Approval")
                .setCancelable(false)
                .setMessage("Thank you for creating an Event. Your Event has been submitted for approval.\n\n" +
                        "You will receive a notification when this Event gets Accepted or Rejected.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    private void ShowTemplateSaveConfirmationDialogue() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Event Submitted for Approval")
                .setCancelable(false)
                .setMessage("Your Template has been successfully Saved.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    private void ShowEventFailedToWriteDialogue() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Event Submission Failed")
                .setCancelable(false)
                .setMessage("Oops! Something went wrong. Please try again later.")
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
                textViewMapsError.setVisibility(View.GONE);
                Place place = PlacePicker.getPlace(this, data);
                userSelectedPlaceLatLng = place.getLatLng();
                mNDUFacultyOfScienceMarker.remove();
                if (mUserLocationMarker != null)
                    mUserLocationMarker.remove();
                mUserLocationMarker = map.addMarker(new MarkerOptions().position(place.getLatLng()));
                map.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                editTextEventLocationName.setText(place.getName());
                eventLocationMap.setClickable(true);
                nDialog.cancel();
            }
            if (resultCode == RESULT_CANCELED) {
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
            }
            if (resultCode == RESULT_CANCELED) {
                nDialog.cancel();
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.get().load(resultUri).fit().centerCrop().into(imageViewEventPhoto);
                nDialog.cancel();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                showSnackBar(this, "Couldn't set Image.");
                nDialog.cancel();
            } else {
                nDialog.cancel();
            }
        }
    }

    public void showSnackBar(Activity activity, String message) {
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
