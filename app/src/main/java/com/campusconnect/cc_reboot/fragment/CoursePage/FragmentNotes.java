package com.campusconnect.cc_reboot.fragment.CoursePage;

import android.graphics.AvoidXfermode;
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

import com.campusconnect.cc_reboot.CoursePageActivity;
import com.campusconnect.cc_reboot.POJO.ModelNoteBookList;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.POJO.NoteBookList;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.adapter.CourseListAdapter;
import com.campusconnect.cc_reboot.adapter.NotesListAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentNotes extends Fragment {

    RecyclerView notes_list;
    NotesListAdapter mNotesAdapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<NoteBookList> mNotes;
    Bundle fragArgs;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        fragArgs = getArguments();

        notes_list = (RecyclerView) v.findViewById (R.id.rv_notes);
        mNotes = new ArrayList<>();

        //Setting the recyclerView
        mLayoutManager = new LinearLayoutManager(getActivity());

        mNotesAdapter = new NotesListAdapter(v.getContext(),mNotes, fragArgs.getInt("CourseColor"));
        notes_list.setLayoutManager(mLayoutManager);
        notes_list.setItemAnimator(new DefaultItemAnimator());
        notes_list.setAdapter(mNotesAdapter);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getNoteBookListRequest request = new MyApi.getNoteBookListRequest(CoursePageActivity.courseId);
        Call<ModelNoteBookList> call = myApi.getNoteBookList(request);
        call.enqueue(new Callback<ModelNoteBookList>() {
            @Override
            public void onResponse(Call<ModelNoteBookList> call, Response<ModelNoteBookList> response) {
                ModelNoteBookList modelNoteBookList = response.body();
                List<NoteBookList> noteBookLists = modelNoteBookList.getNoteBookList();
                for(NoteBookList x : noteBookLists)
                {
                    mNotesAdapter.add(x);
                    mNotesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ModelNoteBookList> call, Throwable t) {

            }
        });

        return v;
    }
}
