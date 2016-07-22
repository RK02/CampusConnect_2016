package com.campusconnect.cc_reboot.fragment.Profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.campusconnect.cc_reboot.CoursePageActivity;
import com.campusconnect.cc_reboot.POJO.ModelNoteBookList;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.POJO.NoteBookList;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.adapter.NotesListAdapter;
import com.campusconnect.cc_reboot.adapter.UploadedNotesListAdapter;

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
public class FragmentUploadedNotes extends Fragment {

    ImageView no_uploads;
    RecyclerView uploaded_notes_list;
    UploadedNotesListAdapter mUploadedNotesAdapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<NoteBookList> mNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        no_uploads = (ImageView) v.findViewById (R.id.iv_no_notes);
        uploaded_notes_list = (RecyclerView) v.findViewById (R.id.rv_notes);
        mNotes = new ArrayList<>();

        BitmapFactory.Options bm_opts = new BitmapFactory.Options();
        bm_opts.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_value_uploads, bm_opts);
        no_uploads.setImageBitmap(bitmap);

        //Setting the recyclerView
        mLayoutManager = new LinearLayoutManager(getActivity());

        mUploadedNotesAdapter = new UploadedNotesListAdapter(v.getContext(),mNotes);
        uploaded_notes_list.setLayoutManager(mLayoutManager);
        uploaded_notes_list.setItemAnimator(new DefaultItemAnimator());
        uploaded_notes_list.setAdapter(mUploadedNotesAdapter);
        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getUploadedRequest request = new MyApi.getUploadedRequest(getActivity().getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId","fake"));
        Call<ModelNoteBookList> call = myApi.getUploaded(request);
        call.enqueue(new Callback<ModelNoteBookList>() {
            @Override
            public void onResponse(Call<ModelNoteBookList> call, Response<ModelNoteBookList> response) {
                ModelNoteBookList modelNoteBookList = response.body();
                if(modelNoteBookList != null) {
                    List<NoteBookList> noteBookLists = modelNoteBookList.getNoteBookList();
                    for (NoteBookList x : noteBookLists) {
                        mUploadedNotesAdapter.add(x);
                        mUploadedNotesAdapter.notifyDataSetChanged();
                    }
                    if(noteBookLists.isEmpty())
                    {
                        if(Build.VERSION.SDK_INT>=21)
                        {
                            uploaded_notes_list.setBackground(getActivity().getDrawable(R.drawable.no_value_uploads));
                        }
                        else
                        {
                            uploaded_notes_list.setBackground(getResources().getDrawable(R.drawable.no_value_uploads));
                        }
                    }
                    else
                    {
                        uploaded_notes_list.setBackgroundColor(getResources().getColor(R.color.ColorRecyclerBackground));
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
