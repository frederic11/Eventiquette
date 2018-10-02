package com.frederictech.eventiquette;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Event> mDataset;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTemplateTitle, mTemplateDescriptiom, mTemplateCreatedDate;

        public MyViewHolder(View view) {
            super(view);
            mTemplateTitle = (TextView) view.findViewById(R.id.text_view_template_title);
            mTemplateDescriptiom = (TextView) view.findViewById(R.id.text_view_template_short_description);
            mTemplateCreatedDate = (TextView) view.findViewById(R.id.text_view_template_created_date);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Event> myDataset) {
        mDataset = myDataset;
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTemplateTitle.setText(mDataset.get(position).getTitle());
        holder.mTemplateDescriptiom.setText(mDataset.get(position).getShortDescription());
        holder.mTemplateCreatedDate.setText(mDataset.get(position).getCreatedDate().toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
