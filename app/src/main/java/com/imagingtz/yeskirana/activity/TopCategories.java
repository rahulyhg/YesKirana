package com.imagingtz.yeskirana.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.imagingtz.yeskirana.R;
import com.imagingtz.yeskirana.fragment.ProductListFragment;

public class TopCategories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_categories);
        int categoryId = getIntent().getIntExtra("categoryId",0);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, ProductListFragment.newInstance(categoryId))
                .commit();
    }
}
