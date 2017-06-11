package com.sleepaiden.alphebetsong.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sleepaiden.alphebetsong.R;
import com.sleepaiden.alphebetsong.models.AlphebetPage;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private AlphebetPage alphebetPage;

    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.wordTextView) TextView textView;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_ALPHEBET_PAGE = "alphebet_page";

    public PlaceholderFragment() {}

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(AlphebetPage alphebetPage) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ALPHEBET_PAGE, alphebetPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, rootView);
        alphebetPage = getArguments().getParcelable(ARG_ALPHEBET_PAGE);

        Picasso.with(this.getContext())
                .load(alphebetPage.getImageId())
                .into(imageView);
        textView.setText(alphebetPage.getWord());

        return rootView;
    }
}
