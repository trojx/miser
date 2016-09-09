package com.miser.li.miser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {


    private String mTitle = "Default";

    public static final String TITLE = "title";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null)
        {
            mTitle = getArguments().getString(TITLE);

        }
        TextView textView = new TextView(getActivity());
        textView.setTextSize(30);
        //textView.setTextColor(Color.parseColor("#00000000"));
        textView.setText(mTitle);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

}
