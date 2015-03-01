package com.test.scrublooper.activities.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.test.scrublooper.R;


/**
 * Created by sauravrp on 2/25/15.
 */
public abstract class BaseActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(fragment == null)
        {
            fragment = createFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        }
    }

    abstract protected Fragment createFragment();
}
