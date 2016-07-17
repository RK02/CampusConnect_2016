package com.campusconnect.cc_reboot.fragment.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusconnect.cc_reboot.POJO.ModelNoteBookList;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.POJO.NoteBookList;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.adapter.BookmarkedNotesListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentBookmarkedNotes extends Fragment {

    RecyclerView boomarked_notes_list;
    BookmarkedNotesListAdapter mBookmarkedNotesAdapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<NoteBookList> mNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        boomarked_notes_list = (RecyclerView) v.findViewById (R.id.rv_notes);
        mNotes = new ArrayList<>();

        //Setting the recyclerView
        mLayoutManager = new LinearLayoutManager(getActivity());

        mBookmarkedNotesAdapter = new BookmarkedNotesListAdapter(v.getContext(),mNotes);
        boomarked_notes_list.setLayoutManager(mLayoutManager);
        boomarked_notes_list.setItemAnimator(new DefaultItemAnimator());
        boomarked_notes_list.setAdapter(mBookmarkedNotesAdapter);
        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getBookmarkedRequest request = new MyApi.getBookmarkedRequest(getActivity().getSharedPreferences("CC",Context.MODE_PRIVATE).getString("profileId","fake"));
        Call<ModelNoteBookList> call = myApi.getBookmarked(request);
        call.enqueue(new Callback<ModelNoteBookList>() {
            @Override
            public void onResponse(Call<ModelNoteBookList> call, Response<ModelNoteBookList> response) {
                ModelNoteBookList modelNoteBookList = response.body();
                Log.i("sw32","bookhere");
                Log.i("sw32bookmark",""+response.code());
                if(modelNoteBookList != null) {
                    List<NoteBookList> noteBookLists = modelNoteBookList.getNoteBookList();
                    for (NoteBookList x : noteBookLists) {
                        Log.i("sw32","bookhere");
                        mBookmarkedNotesAdapter.add(x);
                        mBookmarkedNotesAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelNoteBookList> call, Throwable t) {

            }
        });



        return v;
    }
}
