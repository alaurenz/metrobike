package com.HuskySoft.metrobike.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.HuskySoft.metrobike.R;

public class HistoryAdapter extends ArrayAdapter<HistoryItem>{

    Context context; 
    int layoutResourceId;    
    HistoryItem data[] = null;
    
    public HistoryAdapter(Context context, int layoutResourceId, HistoryItem[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HistoryItemHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new HistoryItemHolder();
            holder.textViewIndex = (TextView)row.findViewById(R.id.textViewIndex);
            holder.textViewFrom = (TextView)row.findViewById(R.id.textViewFrom);
            holder.textViewTo = (TextView)row.findViewById(R.id.textViewTo);
            row.setTag(holder);
        }
        else
        {
            holder = (HistoryItemHolder)row.getTag();
        }
        
        HistoryItem HistoryItem = data[position];
        holder.textViewIndex.setText(""+HistoryItem.index);
        holder.textViewFrom.setText(HistoryItem.from);
        holder.textViewTo.setText(HistoryItem.to);
        
        return row;
    }
    
    static class HistoryItemHolder
    {
        TextView textViewIndex;
        TextView textViewFrom;
        TextView textViewTo;
    }
}
