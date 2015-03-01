package com.test.scrublooper.activities;

import android.app.Fragment;

import com.test.scrublooper.activities.base.BaseActivity;
import com.test.scrublooper.fragments.MainFragment;


public class MainActivity extends BaseActivity
{
    @Override
    protected void onStart()
    {
        super.onStart();
    }


    @Override
    protected Fragment createFragment()
    {
        return new MainFragment();
    }

    public void showTime(Long time)
    {
        setTitle(Long.toString(time));
    }
}
