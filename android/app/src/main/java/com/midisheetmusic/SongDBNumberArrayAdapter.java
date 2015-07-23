package com.midisheetmusic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 6/4/2015.
 */
public class SongDBNumberArrayAdapter<T> extends ArrayAdapter<T> {
    public ArrayList<SongDBEntry> items;
    public ArrayList<SongDBEntry> filtered;
    private LayoutInflater inflater;
    private Context context;
    private Filter filter;

    public SongDBNumberArrayAdapter(Context context, int resourceId, ArrayList<SongDBEntry> objects) {
        super(context, resourceId, (List<T>)objects);
        this.filtered = objects;
        this.items = (ArrayList<SongDBEntry>)filtered.clone();
        this.context = context;
        this.filter = new SongDBFilter();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.choose_songdb_item, null);
        }

        SongDBEntry dbEntry = (SongDBEntry) this.getItem(position);
        int songID = dbEntry.getID();
        String songTitle = dbEntry.getTitle();
        FileUri file = dbEntry.getMidiFilename();

        TextView text = (TextView)convertView.findViewById(R.id.choose_songdb_name);
        text.setHighlightColor(Color.WHITE);
        text.setTextSize(convertFromDp(30));

        text.setText(createIndentedText("[" + songID + "] " + songTitle, 0, 18));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new SongDBFilter();
        return filter;
    }

    private class SongDBFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<SongDBEntry> filt = new ArrayList<SongDBEntry>();
                ArrayList<SongDBEntry> lItems = new ArrayList<SongDBEntry>();
                synchronized (this) {
                    lItems.addAll(items);
                }
                for (int i = 0, l = lItems.size(); i < l; i++) {
                    SongDBEntry m = lItems.get(i);
                    if (Integer.toString(m.getID()).contains(constraint))
                        filt.add(m);
                }
                result.count = filt.size();
                result.values = filt;
            } else {
                synchronized(this) {
                    result.values = items;
                    result.count = items.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered = (ArrayList<SongDBEntry>)results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                add((T)filtered.get(i));
            notifyDataSetInvalidated();
        }
    }

    static SpannableString createIndentedText(String text, int marginFirstLine, int marginNextLines) {
        SpannableString result=new SpannableString(text);
        result.setSpan(new LeadingMarginSpan.Standard(marginFirstLine, marginNextLines), 0, text.length(), 0);
        return result;
    }

    public float convertFromDp(int input) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return ((input - 0.5f) / scale);
    }

    public float convertFromPx(int input) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (((float)input/scale) + 0.5f);
    }
}
