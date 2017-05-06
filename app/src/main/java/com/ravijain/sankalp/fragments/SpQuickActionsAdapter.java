package com.ravijain.sankalp.fragments;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.support.SpDateUtils;
import com.ravijain.sankalp.support.SpUtils;

import java.util.Calendar;

/**
 * Created by ravijain on 1/16/2017.
 */
public class SpQuickActionsAdapter extends BaseAdapter {

    private final Context _context;

    public SpQuickActionsAdapter(Context context) {
        _context = context;
    }

    @Override
    public int getCount() {
        return _images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
                convertView = LayoutInflater.from(_context).inflate(R.layout.quick_actions_item_view, parent, false);

        }

        ImageView iv = (ImageView) convertView.findViewById(R.id.qa_icon);
        if (position == 2 || position == 3) {
            Calendar c = Calendar.getInstance();
            if (position == 3) {
                c.setTime(SpDateUtils.getTomorrow());
            }
            iv.setImageDrawable(SpUtils.getDateDrawable(c, _context));
        }
        else {

            iv.setColorFilter(SpUtils.getPrimaryColor(_context));
            iv.setImageResource(_images[position]);
        }

        TextView label = (TextView) convertView.findViewById(R.id.qa_label);
        label.setText(_labels[position]);

        return convertView;
    }

    private Integer[] _images = {
            R.drawable.ic_date_range_black_24dp,
            R.drawable.ic_note_add_black_24dp,
            R.drawable.ic_description_black_24dp,
            R.drawable.ic_description_black_24dp,
            R.drawable.ic_list_black_24dp
//            R.drawable.ic_description_black_24dp,
    };

    private Integer[] _labels = {
            R.string.calendar,
            R.string.surprise,
            R.string.thisDay,
            R.string.tomorrow,
            R.string.current
    };
}
