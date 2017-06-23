package com.example.goviewer;


import android.widget.ImageView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.uzmap.pkg.uzcore.UZResourcesIDFinder;


public class MyBitmapUtils {

    private MyBitmapUtils() {
        //不能被实例化
    }
    private static DisplayImageOptions imageOptions;
    public static DisplayImageOptions getOptions(){
        if(imageOptions==null){
            synchronized(DisplayImageOptions.class){
                if(imageOptions==null){
                    imageOptions=new DisplayImageOptions.Builder().cacheOnDisk(true)
                            .cacheInMemory(false).showImageOnFail(ApiUtils.getResDrawableID(R.drawable.stub,"stub"))//R.drawable.stub)//
                            .build();
                }
            }
        }
        return imageOptions;
    }
    public static void display(final ImageView imageview, String url) {

		ImageLoader imageLoader=ImageLoader.getInstance();

//		imageLoader.displayImage(url, imageview, imageOptions);
        if(imageview==null){
            return;
        }
        if(url.startsWith("drawable://")){
            url=url.substring(11);
//            display(imageview,Integer.parseInt(url));
            imageview.setImageBitmap(Utils.readBitMap(imageview.getContext().getApplicationContext(),Integer.parseInt(url)));
        }else{
            imageLoader.displayImage(url, imageview, getOptions());
//            Picasso.with(imageview.getContext()).load(url).into(imageview);
//            Glide.with(imageview.getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview);
        }

    }

    public static void display(final ImageView imageview, int id) {
//        Picasso.with(imageview.getContext()).load(id).into(imageview);
//        Glide.with(imageview.getContext()).load(id).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview);
        imageview.setImageBitmap(Utils.readBitMap(imageview.getContext().getApplicationContext(),id));
    }

}

