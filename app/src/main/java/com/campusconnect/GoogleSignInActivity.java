/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.campusconnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.POJO.ModelGetProfile;
import com.campusconnect.POJO.MyApi;
import com.campusconnect.fragment.Home.FragmentCourses;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class GoogleSignInActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
// [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]
    private GoogleApiClient mGoogleApiClient;
    LinearLayout rootView;
//    private TextView mStatusTextView;
//    private TextView mDetailTextView;
    private FirebaseAnalytics firebaseAnalytics;
    String personName,personEmail,personId,personPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);

        rootView = (LinearLayout)findViewById(R.id.main_layout);

        BitmapFactory.Options bm_opts = new BitmapFactory.Options();
        bm_opts.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_portrait_two, bm_opts);
        BitmapDrawable background = new BitmapDrawable(bitmap);
        rootView.setBackground(background);

        if(!getIntent().hasExtra("logout")){
            if(getSharedPreferences("CC",MODE_PRIVATE).contains("profileId")){
                Intent home = new Intent(GoogleSignInActivity.this,HomeActivity2.class);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);
                finish();
            }
        }
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
// Views
//        mStatusTextView = (TextView) findViewById(R.id.status);
//        mDetailTextView = (TextView) findViewById(R.id.detail);
// Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);
// [START config_signin]
// Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
// [END config_signin]
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
// [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
// [END initialize_auth]
// [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
// User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
// User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
// [START_EXCLUDE]
                updateUI(user);
// [END_EXCLUDE]
            }
        };
// [END auth_state_listener]
    }
    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]
// [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]
// [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
// Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount acct = result.getSignInAccount();
                firebaseAuthWithGoogle(acct);
            }else {
// Google Sign In failed, update UI appropriately
// [START_EXCLUDE]
                updateUI(null);
// [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]
// [START auth_with_google]
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
// [START_EXCLUDE silent]
        showProgressDialog();
// [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
// If sign in fails, display a message to the user. If sign in succeeds
// the auth state listener will be notified and logic to handle the
// signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Bundle params = new Bundle();
                            params.putString("firebaseauth","success");
                            firebaseAnalytics.logEvent("login_firebase",params);
                            personName = acct.getDisplayName();
                            personEmail = acct.getEmail();
                            personId = acct.getId();
                            Log.i("sw32", personId + ": here");
                            if(acct.getPhotoUrl()!=null)
                                personPhoto = acct.getPhotoUrl().toString()+"";
                            else
                                personPhoto = "shit";
//                            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
                            SharedPreferences sharedpreferences = getSharedPreferences("CC", Context.MODE_PRIVATE);
                            if (sharedpreferences.contains("profileId")) {
                                Intent home = new Intent(GoogleSignInActivity.this, HomeActivity2.class);
                                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(home);
                                finish();
                            } else {
                                //TODO: SIGN-IN
                                Retrofit retrofit = new Retrofit.
                                        Builder()
                                        .baseUrl(MyApi.BASE_URL)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                MyApi myApi = retrofit.create(MyApi.class);
                                Log.i("sw32",personEmail + " : " + personId + " : " + FirebaseInstanceId.getInstance().getToken());
                                MyApi.getProfileRequest body = new MyApi.getProfileRequest(personEmail,personId,FirebaseInstanceId.getInstance().getToken());
                                Call<ModelGetProfile> call = myApi.getProfile(body);
                                call.enqueue(new Callback<ModelGetProfile>() {
                                    @Override
                                    public void onResponse(Call<ModelGetProfile> call, Response<ModelGetProfile> response) {
                                        ModelGetProfile profile = response.body();
                                        if(profile!=null){
                                            if(profile.getResponse().equals("0"))
                                            {
                                                SharedPreferences sharedPreferences = getSharedPreferences("CC", MODE_PRIVATE);
                                                sharedPreferences
                                                        .edit()
                                                        .putString("profileId", profile.getProfileId())
                                                        .putString("collegeId", profile.getCollegeId())
                                                        .putString("personId", personId)
                                                        .putString("collegeName", profile.getCollegeName())
                                                        .putString("batchName", profile.getBatchName())
                                                        .putString("branchName", profile.getBranchName())
                                                        .putString("sectionName", profile.getSectionName())
                                                        .putString("profileName", personName)
                                                        .putString("email", personEmail)
                                                        .putString("photourl", profile.getPhotourl())
                                                        .apply();
                                                Intent home = new Intent(GoogleSignInActivity.this,HomeActivity2.class);
                                                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(home);
                                                finish();
                                            }
                                            else{
                                                Intent registration = new Intent(GoogleSignInActivity.this,RegistrationPageActivity.class);
                                                registration.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                registration.putExtra("personName",personName);
                                                registration.putExtra("personEmail",personEmail);
                                                registration.putExtra("personPhoto",personPhoto);
                                                registration.putExtra("personId",personId);
                                                startActivity(registration);
                                                finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ModelGetProfile> call, Throwable t) {

                                    }
                                });

                            }
                        }
// [START_EXCLUDE]
                        hideProgressDialog();
// [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]
// [START signin]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]
    private void signOut() {
// Firebase sign out
        mAuth.signOut();
// Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }
    private void revokeAccess() {
// Firebase sign out
        mAuth.signOut();
// Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
//            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
// An unresolvable error has occurred and Google APIs (including Sign-In) will not
// be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MyApp.activityResumed();
        ConnectionChangeReceiver.broadcast(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApp.activityPaused();
    }
}