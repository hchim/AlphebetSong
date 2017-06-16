package com.sleepaiden.alphebetsong.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.sleepaiden.alphebetsong.R;
import com.sleepaiden.alphebetsong.models.AlphebetPage;
import com.sleepaiden.alphebetsong.settings.PreferenceConstants;
import com.sleepaiden.alphebetsong.settings.SettingsActivity;
import com.sleepaiden.alphebetsong.views.CustomViewPager;
import com.sleepaiden.androidcommonutils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements VoiceFragmentDialog.VoiceUpdateInterface {
    private static final String TAG = "MainActivity";
    private PreferenceUtils priPreferenceUtils;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewPager) CustomViewPager mViewPager;

    @BindArray(R.array.alphebet_words) String[] words;

    private int[] images = {
            R.drawable.apple,
            R.drawable.baby,
            R.drawable.cookie,
            R.drawable.doggy,
            R.drawable.egg,
            R.drawable.flower,
            R.drawable.guitar,
            R.drawable.honey,
            R.drawable.igloo,
            R.drawable.jelly,
            R.drawable.kitten,
            R.drawable.lion,
            R.drawable.monkey,
            R.drawable.noodle,
            R.drawable.oyster,
            R.drawable.panda,
            R.drawable.quarter,
            R.drawable.rabbit,
            R.drawable.salmon,
            R.drawable.tiger,
            R.drawable.umbrella,
            R.drawable.vacuum,
            R.drawable.whale,
            R.drawable.xray,
            R.drawable.yogurt,
            R.drawable.zebra
    };

    private int[] sounds = {
            R.raw.alphebet_apple,
            R.raw.alphebet_baby,
            R.raw.alphebet_cookie,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple,
            R.raw.alphebet_apple
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
    private int currentPage = 0;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        priPreferenceUtils = new PreferenceUtils(this, PreferenceConstants.PRIVATE_PREF);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        prepareAdapterData();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(false, new ZoomOutSlideTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position != currentPage) {
                    ((FragmentLifecycle) mSectionsPagerAdapter.instantiateItem(mViewPager, currentPage)).onPauseFragment();
                    ((FragmentLifecycle) mSectionsPagerAdapter.instantiateItem(mViewPager, position)).onResumeFragment();
                    currentPage = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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

    private void prepareAdapterData() {
        List<AlphebetPage> data = new ArrayList<>(words.length);

        for (int i = 0; i < words.length; i++) {
            AlphebetPage ap = new AlphebetPage(images[i], words[i], sounds[i], priPreferenceUtils.getString(words[i], null));
            data.add(ap);
        }

        mSectionsPagerAdapter.setData(data);
    }

    @Override
    public void onCustomVoiceSaved(String word, String filePath) {
        for (AlphebetPage ap : mSectionsPagerAdapter.getData()) {
            if (word.equals(ap.getWord())) {
                ap.setCustomSoundSource(filePath);
                break;
            }
        }
    }

    //    static {
//        System.loadLibrary("native-lib");
//    }
}
