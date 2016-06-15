package com.campusconnect.cc_reboot.fragment.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentUploadedNotes extends Fragment {

    RecyclerView uploaded_notes_list;
    UploadedNotesListAdapter mUploadedNotesAdapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<NoteBookList> mNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        uploaded_notes_list = (RecyclerView) v.findViewById (R.id.rv_notes);
        mNotes = new ArrayList<>();

        //Setting the recyclerView
        mLayoutManager = new LinearLayoutManager(getActivity());

        mUploadedNotesAdapter = new UploadedNotesListAdapter(v.getContext(),mNotes);
        uploaded_notes_list.setLayoutManager(mLayoutManager);
        uploaded_notes_list.setItemAnimator(new DefaultItemAnimator());
        uploaded_notes_list.setAdapter(mUploadedNotesAdapter);

        return v;
    }
}
