package com.frederictech.eventiquette;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RecurrenceTypeAdapter extends ArrayAdapter<RecurrenceTypeOptions> {

    public RecurrenceTypeAdapter(Context context, List<RecurrenceTypeOptions> donationOptions) {
        super(context, 0, donationOptions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_recurrence_type_option, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();

        RecurrenceTypeOptions option = getItem(position);

        vh.description.setText(option.description);
        vh.amount.setText(option.amount);

        return convertView;
    }

    private static final class ViewHolder {
        TextView description;
        TextView amount;

        public ViewHolder(View v) {
            description = (TextView) v.findViewById(R.id.description123);
            amount = (TextView) v.findViewById(R.id.amount123);
        }
    }
}
