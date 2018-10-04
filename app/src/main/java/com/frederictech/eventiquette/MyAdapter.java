package com.frederictech.eventiquette;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.security.PublicKey;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Event> mDataset;

    Context myContext;
    boolean isClickable = true;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public  LinearLayout mLinearLayout;
        public TextView mTemplateTitle, mTemplateEventId, mTemplateCreatedDate;

        public MyViewHolder(View view) {
            super(view);
            mLinearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_template_item_root);
            mTemplateTitle = (TextView) view.findViewById(R.id.text_view_template_title);
            mTemplateEventId = (TextView) view.findViewById(R.id.text_view_template_event_id);
            mTemplateCreatedDate = (TextView) view.findViewById(R.id.text_view_template_created_date);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Event> myDataset, Context context) {
        mDataset = myDataset;
        myContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_templates, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTemplateTitle.setText(mDataset.get(position).getTitle());
        holder.mTemplateEventId.setText(mDataset.get(position).getEventId());
        holder.mTemplateCreatedDate.setText(mDataset.get(position).getCreatedDate().toString());

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClickable) {
                    Intent addEventIntent = new Intent(myContext, AddEventActivity.class);
                    addEventIntent.putExtra("TEMPLATE_EVENT_ID", holder.mTemplateEventId.getText());
                    myContext.startActivity(addEventIntent);
                }
                isClickable = false;
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
