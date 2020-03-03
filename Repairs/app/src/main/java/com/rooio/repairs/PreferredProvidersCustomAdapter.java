package com.rooio.repairs;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class PreferredProvidersCustomAdapter implements ListAdapter {
    private ArrayList<ServiceProviderData> arrayList;
    private Context context;

    public PreferredProvidersCustomAdapter(Context context, ArrayList<ServiceProviderData> preferredProviders) {
        this.arrayList = preferredProviders;
        this.context = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ServiceProviderData data = arrayList.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.provider_list_row, null);
            TextView tittle = convertView.findViewById(R.id.title);
            ImageView imag = convertView.findViewById(R.id.list_image);
            String name = data.name;
            tittle.setText(name);
            if(!data.image.isEmpty()) {
                Picasso.with(context)
                        .load(data.image)
                        .into(imag);
            }
            else {
                ConstraintLayout imageConstraint = convertView.findViewById(R.id.preferredProviderImageConstraint);
                imageConstraint.setVisibility(View.GONE);
            }

            if(context instanceof PreferredProvidersSettings){
                tittle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.details_arrow, 0);
            }
        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }
}