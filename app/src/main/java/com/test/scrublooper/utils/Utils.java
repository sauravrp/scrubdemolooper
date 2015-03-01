package com.test.scrublooper.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sauravrp on 2/26/15.
 */
public class Utils
{
    private static Bitmap sBitmap;
    private static BitmapFactory.Options sBitmapOptions;

    static
    {
        sBitmapOptions = new BitmapFactory.Options();
    }

    public static Bitmap getBitmap()
    {
        return sBitmap;
    }
    private static boolean sLogEnabled = false;

    public static void enableLogging(boolean enable)
    {
        sLogEnabled = enable;
    }

    public static void LogDebug(String tag, String log)
    {
       if(sLogEnabled)
       {
           Log.d(tag, log);
       }
    }

    public static void LogError(String tag, String log)
    {
        if(sLogEnabled)
        {
            Log.e(tag, log);
        }
    }

    public static int calculateInSampleSize(
        BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth)
            {
                inSampleSize = inSampleSize * 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromAsset(Resources resources, final String fileName,
                                                         int reqWidth, int reqHeight) throws IOException
    {
        if(sBitmap == null)
        {
            InputStream is1 = resources.getAssets().open(fileName);

            // First decode with inJustDecodeBounds=true to check dimensions

            sBitmapOptions.inJustDecodeBounds = true;
            sBitmapOptions.inMutable = true;
            //Rect outpadding = new Rect();
            sBitmap = BitmapFactory.decodeStream(is1, null, sBitmapOptions);

            // Calculate inSampleSize
            sBitmapOptions.inSampleSize = calculateInSampleSize(sBitmapOptions, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            sBitmapOptions.inJustDecodeBounds = false;
        }
        else
        {
            sBitmapOptions.inBitmap = sBitmap;
        }

        InputStream is2 = resources.getAssets().open(fileName);

        sBitmap = BitmapFactory.decodeStream(is2, null, sBitmapOptions);

        return sBitmap;
    }



}
