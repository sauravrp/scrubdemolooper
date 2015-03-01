package com.test.scrublooper.threads;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.test.scrublooper.utils.Utils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sauravrp on 3/1/15.
 */
public class ImageDownloader extends HandlerThread
{
    private final static String TAG = "ImageDownloader";
    private final static int MESSAGE_DOWNLOAD = 0;
    public interface Listener
    {
        public void onBitmapReady(ImageView ImageView, Bitmap bitmap);
        public void addBitmapToMemoryCache(String key, Bitmap bitmap);
    }

    Handler mHandler;

    private WeakReference<Resources> mResources;

    Map<ImageView, String> mRequestMap = Collections.synchronizedMap(new HashMap<ImageView, String>());
    Handler mResponseHandler;

    private Listener mListener;

    public void setListener(Listener listener)
    {
        this.mListener = listener;
    }

    public ImageDownloader(Handler responseHandler, Resources resources)
    {
        super(TAG);
        mResponseHandler = responseHandler;
        mResources = new WeakReference<Resources>(resources);
    }

    @Override
    protected void onLooperPrepared()
    {
        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if(msg.what == MESSAGE_DOWNLOAD)
                {
                    ImageView imageView = (ImageView) msg.obj;

                    handleRequest(imageView);
                }
            }
        };
    }

    private void  handleRequest(final ImageView imageView)
    {
        final String fileName = mRequestMap.get(imageView);
        if(TextUtils.isEmpty(fileName))
            return;
        try
        {
            Log.d(TAG, "decoding file = " + fileName);
            final Bitmap bitmap = Utils.decodeSampledBitmapFromAsset(
                mResources.get(),
                fileName,
                imageView.getWidth(),
                imageView.getHeight());

            if(mListener != null)
            {
                mListener.addBitmapToMemoryCache(fileName, bitmap);
            }

            mResponseHandler.post(new Runnable() {
                @Override
                public void run()
                {
                    if(mRequestMap.get(imageView) != fileName)
                    {
                        Log.d(TAG, "discarding " + fileName);
                        return;
                    }

                    mRequestMap.remove(imageView);

                    if(mListener != null)
                    {
                        mListener.onBitmapReady(imageView, bitmap);
                    }
                }
            });

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void queueProcessFile(ImageView imageView, final String fileName)
    {
        Log.d(TAG, "queueProcessFile for fileName = " + fileName);
        mRequestMap.put(imageView, fileName);

        mHandler.obtainMessage(MESSAGE_DOWNLOAD, imageView).sendToTarget();
    }

    public void clearQueue()
    {
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        mRequestMap.clear();
    }
}
