package com.rooio.repairs;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlaceAutoCompleteAdapter extends ArrayAdapter implements Filterable {

    private ArrayList<String> results;

    private PlaceAPI placeApi=new PlaceAPI();

    public PlaceAutoCompleteAdapter(Context context,int resId){
        super(context,resId);

    }

    @Override
    public int getCount(){
        return results.size();
    }

    @Override
    public String getItem(int pos){
        return results.get(pos);
    }

    @NotNull
    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                if(constraint!=null){
                    results=placeApi.autoComplete(constraint.toString());

                    filterResults.values=results;
                    filterResults.count=results.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results1) {
                if(results1 !=null && results1.count>0){
                    notifyDataSetChanged();
                }
                else{
                    notifyDataSetInvalidated();
                }

            }
        };
    }

}