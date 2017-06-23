package com.example.goviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    //幼儿二级菜单集合
    public static List<Map<String, Object>> childList;
    //课程信息传递到三级菜单
    public static Map<String, Object> childKcMap;
    //判断课程是否购买
    public static boolean kcStat;

    public static void setKcStat(boolean kcStat) {
        Utils.kcStat = kcStat;
    }

    public static boolean getKcStat(){
        return kcStat;
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * @param context
     * @param resId
     * @return
     */
    public static Map<Integer,SoftReference<Bitmap>> bitmaps=new HashMap<Integer,SoftReference<Bitmap>>();
    public static Bitmap readBitMap(Context context, int resId) {
        if(bitmaps.get(new Integer(resId))!=null&&bitmaps.get(new Integer(resId)).get()!=null&&!bitmaps.get(new Integer(resId)).get().isRecycled()){
            return bitmaps.get(new Integer(resId)).get();
        }
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        Bitmap bitmap=BitmapFactory.decodeStream(is, null, opt);
        try {
            is.close();
            is=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        SoftReference<Bitmap> softRef = new SoftReference<Bitmap>(bitmap);
        bitmaps.put(new Integer(resId),softRef);
        bitmap=null;
        return softRef.get();
    }
    public static boolean canloading=true;
    public static File downLoadFile(String httpUrl) {
        // TODO Auto-generated method stub
        final String fileName = "cqtxt"+UserInfo.getVersion()+".apk";
        File tmpFile = new File("//sdcard");
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }
        final File file = new File("//sdcard//" + fileName);
        try {
            URL url = new URL(httpUrl);
            try {
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[256];
                conn.connect();
                double count = 0;
                if (conn.getResponseCode() >= 400) {
//	                                Toast.makeText(DownFile.this, "连接超时", Toast.LENGTH_SHORT)
//	                                                .show();
                    Log.i("time", "time exceed");
                } else {
                    while (count <= 100&&canloading) {
                        if (is != null) {
                            int numRead = is.read(buf);
                            if (numRead <= 0) {
                                break;
                            } else {
                                fos.write(buf, 0, numRead);
                            }
                        } else {
                            break;
                        }
                    }
                }
                conn.disconnect();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(canloading){
            return file;
        }else{
            return  null;
        }

    }
}