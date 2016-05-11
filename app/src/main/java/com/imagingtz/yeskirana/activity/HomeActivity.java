package com.imagingtz.yeskirana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sked.core.adapter.CategoryAdapter;
import com.sked.core.common.Utils;
import com.sked.core.model.Category;
import com.sked.core.model.SubCategory;
import com.imagingtz.yeskirana.R;
import com.imagingtz.yeskirana.fragment.MainFragment;
import com.imagingtz.yeskirana.fragment.ProductListFragment;

import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView userName;
    private List<Category> categories;
    private boolean homeFlag;
    private ImageButton navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        userName = (TextView) header.findViewById(R.id.userName);
        userName.setText(com.imagingtz.yeskirana.java.Utils.getUserName(this));
        navView = (ImageButton) header.findViewById(R.id.edit);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });
        final ExpandableListView listView = (ExpandableListView) navigationView.findViewById(R.id.listView);
        categories = Utils.getCategoryList(this, "category.json");
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories);
        listView.setAdapter(categoryAdapter);

        groupClick(0);
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //TODO:Add action if required on the expansion of the group item.
            }
        });

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //TODO: Add actions here for the group item click.
                groupClick(groupPosition);
                return false;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //TODO:Start required activity or do required action on this block.
                homeFlag = false;
                Category category = categories.get(groupPosition);
                SubCategory subCategory = category.getSubCategories().get(childPosition);
                int subCategoryId = subCategory.getId();
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction().replace(R.id.rlContainer,
                        ProductListFragment.newInstance(subCategoryId)).commit();

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void groupClick(int groupPosition) {
        if (categories.get(groupPosition).getSubCategories().size() == 0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (groupPosition == 0) {
                homeFlag = true;
                fragmentManager.beginTransaction().replace(R.id.rlContainer,
                        MainFragment.newInstance(groupPosition)).commit();
            } else if (groupPosition == 1) {
                homeFlag = false;
                startActivity(new Intent(com.imagingtz.yeskirana.activity.HomeActivity.this, MyBasketActivity.class));
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!homeFlag) {
            groupClick(0);
        } else {
            finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
