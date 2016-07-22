package com.campusconnect.cc_reboot.fragment.Drawer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.adapter.FAQListAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 22/07/2016.
 */
public class FragmentFAQ extends Fragment {

    @Bind(R.id.recycler_faq_page)
    RecyclerView faq_list;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_faq, container, false);
        ButterKnife.bind(this, v);
        context = v.getContext();

        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        faq_list.setLayoutManager(llm);
        faq_list.setHasFixedSize(true);
        faq_list.setItemAnimator(new DefaultItemAnimator());

        FAQListAdapter mFAQListAdapter = new FAQListAdapter(context);
        faq_list.setAdapter(mFAQListAdapter);

        return v;
    }
}
