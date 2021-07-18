package com.example.android.maptrial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.ConversationAction;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SymptomAdapter extends ArrayAdapter<Symptom> {

    private Context mContext;
    int mResource;

    public SymptomAdapter(@NonNull Context context, int resource, @NonNull List<Symptom> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        int deg = getItem(position).getDegree();

        Symptom symp = new Symptom(name, deg);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvname = (TextView) convertView.findViewById(R.id.nameview);
        TextView tvdeg = (TextView) convertView.findViewById(R.id.degview);

        tvname.setText(name);
        tvdeg.setText(String.valueOf(deg));

        return convertView;
    }
}
