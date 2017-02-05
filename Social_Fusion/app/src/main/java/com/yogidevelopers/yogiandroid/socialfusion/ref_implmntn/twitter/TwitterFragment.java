package com.yogidevelopers.yogiandroid.socialfusion.ref_implmntn.twitter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Session;
import com.yogidevelopers.yogiandroid.socialfusion.R;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.twtr.TwitterLoginFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterFragment extends Fragment {


    public TwitterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    public static Fragment getFragment() {

        Session activeSession = Twitter.getSessionManager().getActiveSession();

        if (activeSession != null) {
            return new TwitterTimelineFragment();
        } else {
            return new TwitterLoginFragment();
        }
    }
}
