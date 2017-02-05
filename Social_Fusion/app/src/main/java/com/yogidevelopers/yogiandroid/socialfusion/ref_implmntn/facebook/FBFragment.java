package com.yogidevelopers.yogiandroid.socialfusion.ref_implmntn.facebook;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.yogidevelopers.yogiandroid.socialfusion.R;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fcbk.FBLoginFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FBFragment extends Fragment {


    public FBFragment() {
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

        AccessToken logedIn = AccessToken.getCurrentAccessToken();

        if (logedIn != null) {
//            Log.v(FBFragment.class.getSimpleName(), "Logged In" + logedIn.toString());
            return new FBFeedFragment();
        } else {
//            Log.v(FBFragment.class.getSimpleName(), "Not Logged In");
            return new FBLoginFragment();
        }
    }
}
