package com.ravijain.sankalp.support;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpCategory;
import com.ravijain.sankalp.data.SpCategoryItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ravijain on 4/6/2017.
 */
public abstract class SpExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;

    public List<Object> getGroups() {
        return _groups;
    }

    public HashMap<Integer, List<Object>> getChildren() {
        return _children;
    }

    public void setGroups(List<Object> _groups) {
        this._groups = _groups;
    }

    public void setChildren(HashMap<Integer, List<Object>> _children) {
        this._children = _children;
    }

    private List<Object> _groups; // header titles
    // child data in format of header title, child title

    private HashMap<Integer, List<Object>> _children;

    public SpExpandableListAdapter(Context c, List<Object> groups, HashMap<Integer, List<Object>> children) {
        _context = c;
        _groups = groups;
        _children = children;

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._children.get(groupPosition).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this._children.get(groupPosition) != null) {
            return this._children.get(groupPosition).size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(_context);

        View view = null;
        if (convertView == null) {
            view = inflater.inflate(getGroupLayout(), null);
        } else {
            view = convertView;
        }

        populateGroupView(groupPosition, isExpanded, view, parent);


        ImageView iconExpand = (ImageView) view.findViewById(R.id.icon_expand);
        ImageView iconCollapse = (ImageView) view
                .findViewById(R.id.icon_collapse);

        if (isExpanded) {
            iconExpand.setVisibility(View.GONE);
            iconCollapse.setVisibility(View.VISIBLE);
        } else {
            iconExpand.setVisibility(View.VISIBLE);
            iconCollapse.setVisibility(View.GONE);
        }

        if (getChildrenCount(groupPosition) == 0) {
            iconExpand.setVisibility(View.GONE);
            iconCollapse.setVisibility(View.GONE);
        }

        return view;
    }

    public abstract int getGroupLayout();
    public abstract void populateGroupView(int groupPosition, boolean isExpanded,
                                           View view, ViewGroup parent);

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



}
