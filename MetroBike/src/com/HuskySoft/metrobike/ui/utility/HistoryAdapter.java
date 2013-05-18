package com.HuskySoft.metrobike.ui.utility;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.HuskySoft.metrobike.R;

/**
 * This class holds the History Item objects and connect to the text view in the
 * search activity.
 * 
 * @author CoolCapri, sw11
 */
public class HistoryAdapter extends ArrayAdapter<String> {
    /**
     * The context of this current activity.
     */
    private Context context;
    /**
     * The id of this current activity.
     */
    private int layoutResourceId;
    /**
     * The array of history item object.
     */
    private History data;

    /**
     * Constructor to initialize the field.
     * 
     * @param contextView
     *            this current activity.
     * @param layoutId
     *            this current activity id.
     * @param historyData
     *            the array of history items.
     */
    public HistoryAdapter(final Context contextView, final int layoutId,
            final History historyData) {
        super(contextView, layoutId, historyData.getHistory());
        this.layoutResourceId = layoutId;
        this.context = contextView;
        this.data = historyData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.ArrayAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    @Override
    public final View getView(final int position, final View convertView,
            final ViewGroup parent) {
        View row = convertView;
        HistoryItemHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new HistoryItemHolder();
           // holder.textViewIndex = (TextView) row
            //        .findViewById(R.id.textViewIndex);
            holder.textViewFrom = (TextView) row
                    .findViewById(R.id.textViewFrom);
            //holder.textViewTo = (TextView) row.findViewById(R.id.textViewTo);
            row.setTag(holder);
        } else {
            holder = (HistoryItemHolder) row.getTag();
        }

        String historyItem = data.getAddress(position);
        //holder.textViewIndex.setText("" + historyItem.getIndex());
        //holder.textViewFrom.setText(historyItem.getFrom());
        //holder.textViewTo.setText(historyItem.getTo());
        //holder.textViewIndex.setText("" + position);
        holder.textViewFrom.setText(historyItem);

        return row;
    }

    /**
     * This inner class holds the textView objects.
     */
    static class HistoryItemHolder {
        /**
         * The position of text view.
         */
        private TextView textViewIndex;
        /**
         * The from address text view.
         */
        private TextView textViewFrom;
        /**
         * The to address text view.
         */
        private TextView textViewTo;
    }
}
