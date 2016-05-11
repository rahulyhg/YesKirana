package com.sked.core.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sked.core.R;
import com.sked.core.model.Category;

import java.util.List;

/**
 * ERC, All rights Reserved
 * Created by Sanjeet on 02-Jan-16.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categories.get(groupPosition).getSubCategories().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categories.get(groupPosition).getSubCategories().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView  =layoutInflater.inflate(R.layout.group_item, null);
        TextView view = (TextView) itemView.findViewById(android.R.id.text1);
        ImageView indicator = (ImageView)itemView.findViewById(R.id.indicator);
        view.setText(categories.get(groupPosition).getName());
        if (categories.get(groupPosition).getSubCategories().size()>0){
             indicator.setVisibility(View.VISIBLE);
        }else {
            indicator.setVisibility(View.GONE);
        }
        return itemView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) layoutInflater.inflate(R.layout.child_item, null);
        view.setText(categories.get(groupPosition).getSubCategories().get(childPosition).getName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
