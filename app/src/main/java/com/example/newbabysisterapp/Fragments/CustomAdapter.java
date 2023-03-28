package com.example.newbabysisterapp.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newbabysisterapp.R;
import com.example.newbabysisterapp.model.User;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<User> {
    private ArrayList<User> dataSet;
    private Context mContext;

    public CustomAdapter(ArrayList<User> data, Context context) {
        super(context, android.R.layout.simple_list_item_1, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        User dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        MyViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_row_item_layout,parent,false);
            viewHolder = new MyViewHolder(convertView);
            viewHolder.fullName = (TextView) convertView.findViewById(R.id.fullNameText);
            viewHolder.aboutMe = (TextView) convertView.findViewById(R.id.aboutMeText);
            viewHolder.phoneNum = (TextView) convertView.findViewById(R.id.phoneNumText);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        viewHolder.fullName.setText(dataModel.getPrivateName() + " " + dataModel.getFamilyName());
        viewHolder.aboutMe.setText(dataModel.getDescription());
        viewHolder.phoneNum.setText(dataModel.getPhone());
        // Return the completed view to render on screen
        return convertView;
    }

    //the data for the each ingredient row
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView fullName;
        TextView aboutMe;

        TextView phoneNum;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullNameText);
            aboutMe = itemView.findViewById(R.id.aboutMeText);
            phoneNum = itemView.findViewById(R.id.phoneNumText);
        }
    }
}
