package com.sleepaiden.alphebetsong.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.sleepaiden.alphebetsong.R;
import com.sleepaiden.alphebetsong.models.AlphebetPage;
import com.sleepaiden.alphebetsong.settings.PreferenceConstants;
import com.sleepaiden.alphebetsong.settings.SettingsActivity;
import com.sleepaiden.alphebetsong.views.CustomViewPager;
import com.sleepaiden.androidcommonutils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private PreferenceUtils preferenceUtils;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.viewPager) CustomViewPager mViewPager;

    @BindArray(R.array.alphebet_words) String[] words;

    private int[] images = {
            R.drawable.apple,
            R.drawable.baby,
            R.drawable.cookie,
    };

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //TODO Start a new learning
            }
        });
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        prepareAdapterData();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(false, new ZoomOutSlideTransformer());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferenceUtils == null) {
            preferenceUtils = new PreferenceUtils(this);
        }
        //Set the visibility of floating button
        String lMode = getLearningMode();
        if (PreferenceConstants.LEARNING_MODE_MANUAL.equals(lMode)) {
            fab.setVisibility(View.INVISIBLE);
            mViewPager.setPagingEnabled(true);
        } else {
            fab.setVisibility(View.VISIBLE);
            mViewPager.setPagingEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getLearningMode() {
        return preferenceUtils.getString(
                PreferenceConstants.PREF_KEY_LEARNING_MODE,
                PreferenceConstants.LEARNING_MODE_MANUAL);
    }

    private void prepareAdapterData() {
        List<AlphebetPage> data = new ArrayList<>(words.length);

        for (int i = 0; i < words.length; i++) {
            AlphebetPage ap = new AlphebetPage(images[i], words[i], null, null);
            data.add(ap);
        }

        mSectionsPagerAdapter.setData(data);
    }
//    static {
//        System.loadLibrary("native-lib");
//    }
}
