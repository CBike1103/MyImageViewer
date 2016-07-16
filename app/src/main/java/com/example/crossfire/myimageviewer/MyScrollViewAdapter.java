package com.example.crossfire.myimageviewer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by CrossFire on 2016/7/13.
 */
public class MyScrollViewAdapter extends BaseAdapter {

    private List<String> path;
    private ImageLoader imageloader;
    protected LayoutInflater inflater;
    private DisplayImageOptions options;

    public MyScrollViewAdapter(Context context,List<String> pathName){
        path = pathName;
        imageloader = UniversalImageLoader.getImageLoader(context);
        inflater = LayoutInflater.from(context);
        options = UniversalImageLoader.getDisplayImageOptions();
    }

    @Override
    public int getCount() {
        return path.size();
    }

    @Override
    public Object getItem(int position) {
        return path.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        String pathName = path.get(position);
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.my_scroll_view_item,parent,false);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.my_scroll_view_item);
            convertView.setTag(viewHolder);

            viewHolder.imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent it = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse("file://" + view.getTag());
                    it.setDataAndType(uri, "image/*");
                    view.getContext().startActivity(it);
                }
            });

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setTag(pathName);

        imageloader.displayImage("file://"+pathName,viewHolder.imageView,options);

        return convertView;

    }

    private class ViewHolder{
        ImageView imageView;
    }
}
