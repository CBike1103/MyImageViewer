package com.example.crossfire.myimageviewer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by CrossFire on 2016/7/11.
 */
public class MyImageLoader {

    private static LruCache<String, Bitmap> memoryCache;
    private static ExecutorService imageThreadPool = Executors.newFixedThreadPool(4);

    private static MyImageLoader myImageLoader;

    private MyImageLoader() {
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory/4;
        // 设置图片缓存大小为程序最大可用内存的1/4
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }


    public static MyImageLoader getInstance() {
        if (myImageLoader == null) {
            myImageLoader = new MyImageLoader();
        }
        return myImageLoader;
    }


    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public void loadImage(final String path, final int reqWidth, final NativeImageCallBack niCallBack){
        Bitmap bitmap= getBitmapFromMemoryCache(path);

        final Handler handler = new Handler(){

            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                niCallBack.onImageLoad((Bitmap)msg.obj,path);
            }
        };


        if(bitmap!=null) {
            Message msg = handler.obtainMessage();
            msg.obj = bitmap;
            handler.sendMessage(msg);
        }else {

            imageThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = decodeSampledBitmapFromResource(path, reqWidth);
                    if(bitmap==null){//in case have problems when decoding the file
                        bitmap = BitmapFactory.decodeResource(Resources.getSystem(),android.R.drawable.alert_light_frame);
                    }
                    Message msg = handler.obtainMessage();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);

                    addBitmapToMemoryCache(path, bitmap);
                }
            });
        }

    }

    private Bitmap getBitmapFromMemoryCache(String key) {
        return memoryCache.get(key);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth) {
        // 源图片的宽度
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (width > reqWidth) {
            // 计算出实际宽度和目标宽度的比率
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = widthRatio;
        }
        return inSampleSize;
    }

    private static Bitmap decodeSampledBitmapFromResource(String pathName,
                                                         int reqWidth) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    public interface NativeImageCallBack{
        void onImageLoad(Bitmap bitmap, String path);
    }

}