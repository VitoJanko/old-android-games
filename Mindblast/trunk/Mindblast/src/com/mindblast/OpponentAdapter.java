package com.mindblast;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OpponentAdapter extends ArrayAdapter<Opponent>{

    Context context; 
    int layoutResourceId;    
//    Opponent data[] = null;
    ArrayList<Opponent> data = null;//new ArrayList<Opponent>();
    
    public OpponentAdapter(Context context, int layoutResourceId, ArrayList<Opponent> data) {
    	super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        OpponentHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new OpponentHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            
            row.setTag(holder);
        }
        else
        {
            holder = (OpponentHolder)row.getTag();
        }
        
        Opponent opponent = data.get(position);
        holder.txtTitle.setText(opponent.name);
        holder.imgIcon.setImageResource(opponent.icon);
        
        return row;
    }
    
    static class OpponentHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}