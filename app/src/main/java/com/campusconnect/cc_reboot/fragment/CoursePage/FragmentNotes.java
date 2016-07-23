package com.campusconnect.cc_reboot.fragment.CoursePage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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
import android.widget.ImageView;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentNotes extends Fragment {

    ImageView no_notebook;
    RecyclerView notes_list;
    NotesListAdapter mNotesAdapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<NoteBookList> mNotes;
    Bundle fragArgs;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        fragArgs = getArguments();

        no_notebook = (ImageView) v.findViewById (R.id.iv_no_notes);
        notes_list = (RecyclerView) v.findViewById (R.id.rv_notes);
        mNotes = new ArrayList<>();

        BitmapFactory.Options bm_opts = new BitmapFactory.Options();
        bm_opts.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_value_notebooks, bm_opts);
        no_notebook.setImageBitmap(bitmap);

        //Setting the recyclerView
        mLayoutManager = new LinearLayoutManager(getActivity());

        mNotesAdapter = new NotesListAdapter(v.getContext(),mNotes, fragArgs.getInt("CourseColor"));
        notes_list.setLayoutManager(mLayoutManager);
        notes_list.setItemAnimator(new DefaultItemAnimator());
        notes_list.setAdapter(mNotesAdapter);

        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getNoteBookListRequest request = new MyApi.getNoteBookListRequest(CoursePageActivity.courseId);
        Call<ModelNoteBookList> call = myApi.getNoteBookList(request);
        call.enqueue(new Callback<ModelNoteBookList>() {
            @Override
            public void onResponse(Call<ModelNoteBookList> call, Response<ModelNoteBookList> response) {
                ModelNoteBookList modelNoteBookList = response.body();
                Log.i("sw32","herenotelist");
                Log.i("sw32","herenotelist " + response.code());
                if(modelNoteBookList != null) {
                    Log.i("sw32","herenotelist1");
                    List<NoteBookList> noteBookLists = modelNoteBookList.getNoteBookList();
                    for (NoteBookList x : noteBookLists) {
                        mNotesAdapter.add(x);
                        mNotesAdapter.notifyDataSetChanged();
                    }
                    if(noteBookLists.isEmpty())
                    {
                        if(Build.VERSION.SDK_INT>=21)
                        {
                            notes_list.setBackground(getActivity().getDrawable(R.drawable.no_value_notebooks));
                        }
                        else
                        {
                            notes_list.setBackground(getResources().getDrawable(R.drawable.no_value_notebooks));
                        }
                    }
                    else
                    {
                        notes_list.setBackgroundColor(getResources().getColor(R.color.ColorRecyclerBackground));
                    }
                }
                Log.i("sw32","herenotelist");
            }

            @Override
            public void onFailure(Call<ModelNoteBookList> call, Throwable t) {

            }
        });

        return v;
    }
}
