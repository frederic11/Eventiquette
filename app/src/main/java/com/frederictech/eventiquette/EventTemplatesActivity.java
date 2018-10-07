package com.frederictech.eventiquette;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Set;

public class EventTemplatesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Context context = this;

    FirebaseFirestore db;
    static final String EVENTS_COLLECTION_TEMPLATES = "eventsCollectionTemplates";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_templates);

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.event_templates_recycler_view);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final ArrayList myEventsTemplates = new ArrayList();

        db.collection(EVENTS_COLLECTION_TEMPLATES)
                .orderBy("createdDate")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DB GET", document.getId() + " => " + document.getData());
                                Event myEvent = document.toObject(Event.class);
                                myEvent.setEventId(document.getId());
                                myEventsTemplates.add(myEvent);
                            }

                            // specify an adapter
                            mAdapter = new MyAdapter(myEventsTemplates, context);
                            mRecyclerView.setAdapter(mAdapter);

                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(SetUpItemTouchHelper(myEventsTemplates, mAdapter));
                            itemTouchHelper.attachToRecyclerView(mRecyclerView);
                        } else {
                            Log.d("DB GET", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private ItemTouchHelper.SimpleCallback SetUpItemTouchHelper(final ArrayList<Event> eventsList, final RecyclerView.Adapter mAdapter){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;
            Event deletedEvent;
            int deletedEventPosition;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(EventTemplatesActivity.this, R.drawable.ic_delete_white_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = 54;
                initiated = true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                View view = EventTemplatesActivity.this.getWindow().getDecorView().findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar
                        .make(view, "Template Deleted Successfully", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                db.collection(EVENTS_COLLECTION_TEMPLATES).document(deletedEvent.getEventId()).set(deletedEvent);
                                eventsList.add(deletedEventPosition, deletedEvent);
                                mAdapter.notifyDataSetChanged();
                                Snackbar snackbar1 = Snackbar.make(view, "Template restored", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            }
                        });

                snackbar.show();
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                db.collection(EVENTS_COLLECTION_TEMPLATES).document(eventsList.get(position).getEventId()).delete();
                deletedEvent = eventsList.get(position);
                deletedEventPosition = position;
                eventsList.remove(position);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        };

        return simpleItemTouchCallback;
    }
}
