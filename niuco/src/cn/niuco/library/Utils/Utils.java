package cn.niuco.library.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.niuco.library.NiucoAppliaction;
import cn.niuco.library.bean.DetailtypeBean;

/**
 * Created by 1973 on 2015/4/30.
 */
public class Utils {
    public static ArrayList<String> getlist(String introduction) {
        ArrayList<String> urls = new ArrayList();
        String[] str = introduction.split("<img");
        for (int j = 0; j < str.length; j++) {
            String s=new String(str[j]);
            if(s.contains("src=")){


                    s = s.replaceAll("-src=", "333");
                    int ta = s.indexOf("src=");
                    s = s.substring(ta + 5);
                    int r = s.indexOf("\"");
                    s = s.substring(0, r);
                    // s=s.replaceAll("\"","");
                    if (s != null && s.length() != 0) {
                        Log.e("srccc", s);
                        s=s.trim();
                        urls.add(s);
                    }
            }
        }
        return urls;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        final float roundPx = width > height ? height / 2 : width / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static SimpleImageLoadingListener listener;

    public static <T extends ImageView> void displayImage2Circle(T container, String url) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        if (listener == null) {
            listener = new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    ((ImageView) view).setImageBitmap(getRoundedCornerBitmap(loadedImage));
                }
            };
        }
        ImageLoader.getInstance().displayImage(url, container, options, listener);
    }

    public static void setUsingState(Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("base_userinfo",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("first_time", "false");
        editor.commit();
    }

    public static String getUsingState(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("base_userinfo",
                Activity.MODE_PRIVATE);
        String value = sharedPreferences.getString("first_time", "true"); //0为未登陆 ，1为yijing登陆
        return value;
    }


}
