package com.example.crossfire.myimageviewer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by CrossFire on 2016/7/11.
 */
public class folderActivity extends AppCompatActivity {
    private HashMap<String,List<String>> mGroupMap = new HashMap<>();
    private List<ImageBean> imageBeanList  = new ArrayList<>();
    private final static int SCAN_OK = 1;
    private ProgressDialog mProgressDialog;
    private GroupAdapter gAdapter;
    private GridView mGroupGridView;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what== SCAN_OK){
                mProgressDialog.dismiss();

                gAdapter = new GroupAdapter(folderActivity.this, imageBeanList = subGroupOfImage(mGroupMap),mGroupGridView);
                mGroupGridView.setAdapter(gAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_folder);

        mGroupGridView = (GridView)findViewById(R.id.image_folder_grid);

        getImages();

        mGroupGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> childList = (ArrayList<String>) mGroupMap.get(imageBeanList.get(position).getFolderName());


                Intent intent = new Intent(folderActivity.this, showImageActivity.class);
                intent.putStringArrayListExtra("data",childList);
                startActivity(intent);
            }
        });
    }

    @Override
    public void  onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode>0){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                getImages();
            }else{
                Toast.makeText(this,"没有权限什么都干不了，呜呜呜~~",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getImages() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            //progressBar
            mProgressDialog = ProgressDialog.show(this, "嗷嗷嗷", "少女祈祷中…");


            new Thread(new Runnable() {
                @Override
                public void run() {
                    Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    ContentResolver contentResolver = folderActivity.this.getContentResolver();
                    Cursor cursor = contentResolver.query(imageUri, null, null, null, MediaStore.Images.Media.DATE_MODIFIED);

                    while (cursor.moveToNext()) {
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//get path of image

                        String parentName = new File(path).getParentFile().getName();//get parent path

                        if (!mGroupMap.containsKey(parentName)) {
                            List<String> childList = new ArrayList<>();
                            childList.add(path);
                            mGroupMap.put(parentName, childList);
                        } else {
                            mGroupMap.get(parentName).add(path);
                        }

                    }

                    mHandler.sendEmptyMessage(SCAN_OK);
                    cursor.close();
                }
            }).start();
        }
    }

    private List<ImageBean> subGroupOfImage(HashMap<String,List<String>> groupMap){
        if(groupMap.size()==0) return new ArrayList<>();

        List<ImageBean> list = new ArrayList<>();

        Iterator<Map.Entry<String,List<String>>> iterator = groupMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,List<String>> entry = iterator.next();
            ImageBean mImageBean = new ImageBean();
            String key = entry.getKey();
            List<String> value = entry.getValue();

            mImageBean.setFolderName(key);
            mImageBean.setImageCounts(value.size());
            mImageBean.setImagePath(value.get(0));

            list.add(mImageBean);
        }

        return list;
    }
}
