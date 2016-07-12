package com.example.crossfire.myimageviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by CrossFire on 2016/7/11.
 */
public class GroupAdapter extends BaseAdapter {

    private List<ImageBean> list;
    private Point point = new Point(0,0);
    private GridView gridView;
    protected LayoutInflater inflater;

    public GroupAdapter(Context context,List<ImageBean> list,GridView gridView){
        this.list = list;
        this.gridView = gridView;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ImageBean imageBean = list.get(position);

        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.image_folder_grid_item,parent,false);
            viewHolder.myImageView = (MyImageView)convertView.findViewById(R.id.group_image);
            viewHolder.textViewCounts = (TextView)convertView.findViewById(R.id.group_count);
            viewHolder.textViewTitle = (TextView)convertView.findViewById(R.id.group_title);

            viewHolder.myImageView.setOnMeasureListener(new MyImageView.OnMeasureListener(){

                @Override
                public void onMeasureSize(int width,int height){
                    point.set(width,height);
                }
            });

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewTitle.setText(imageBean.getFolderName());
        viewHolder.textViewCounts.setText(imageBean.getImageCounts()+"");
        viewHolder.myImageView.setTag(imageBean.getImagePath());

        int reqWidth = point.x>0?point.x : 400;

        ImageLoader.getInstance().loadImage(imageBean.getImagePath(), reqWidth, new ImageLoader.NativeImageCallBack() {
            @Override
            public void onImageLoad(Bitmap bitmap, String path) {
                ImageView imageView = (ImageView)gridView.findViewWithTag(path);
                if(bitmap!=null&&imageView!=null){
                    imageView.setImageBitmap(bitmap);
                }
            }
        });

        return convertView;
    }

    public static class ViewHolder{
        public MyImageView myImageView;
        public TextView textViewTitle;
        public TextView textViewCounts;
    }
}
