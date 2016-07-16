package com.example.crossfire.myimageviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.huewu.pla.lib.MultiColumnListView;


/**
 * Created by CrossFire on 2016/7/12.
 */
public class showImageActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_scroll_view);

        MyScrollViewAdapter myScrollViewAdapter = new MyScrollViewAdapter(showImageActivity.this,getIntent().getStringArrayListExtra("data"));
        MultiColumnListView multiColumnListView = (MultiColumnListView)findViewById(R.id.my_scroll_image_view);

        multiColumnListView.setDividerHeight(5);
        multiColumnListView.setAdapter(myScrollViewAdapter);

    }
}
