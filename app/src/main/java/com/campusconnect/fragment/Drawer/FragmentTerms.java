package com.campusconnect.fragment.Drawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusconnect.R;

/**
 * Created by RK on 15/06/2016.
 */
public class FragmentTerms extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_terms,container,false);

        return v;
    }
}
