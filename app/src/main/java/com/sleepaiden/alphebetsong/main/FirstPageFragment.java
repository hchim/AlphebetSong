package com.sleepaiden.alphebetsong.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sleepaiden.alphebetsong.R;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huiche on 6/14/17.
 */

public class FirstPageFragment extends Fragment implements FragmentLifecycle {

    @BindView(R.id.tipsListView) ListView tipsListView;

    @BindArray(R.array.tips_array) String[] tips;

    public static FirstPageFragment newInstance() {
        FirstPageFragment fragment = new FirstPageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_page, container, false);
        ButterKnife.bind(this, rootView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),
                R.layout.tips_item,
                tips);
        tipsListView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
    }
}
