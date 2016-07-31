package com.campusconnect;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UploadPicturesActivity extends AppCompatActivity {

    @Bind(R.id.container_action_buttons)
    LinearLayout action_buttons_top;
    @Bind(R.id.container_action_buttons_more)
    LinearLayout action_buttons_bottom;

    @Bind(R.id.ib_close)
    ImageButton close;

    @Bind(R.id.for_height)
    View for_measure;

    @Bind(R.id.iv_no_upload)
    ImageView no_upload;

    View uploadDefaultActionView;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public static Button btnSelect;
    private Button next;
    public static Button camera;
    Button cancel;
    private  String pictureImagePath="";
    ImageAdapter imageAdapter;
    public static ArrayList<String> urls;
    public static ArrayList<String> uris;
    private FirebaseAnalytics firebaseAnalytics;

    public static float width;
    public static float height;
    public static int action;
    final int READ_EXTERNAL=69;
    final int WRITE_EXTERNAL=70;
    public static GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pictures);

        ButterKnife.bind(this);
        gridView= (GridView) findViewById(R.id.imageGrid);
        imageAdapter = new ImageAdapter(this);

        BitmapFactory.Options bm_opts = new BitmapFactory.Options();
        bm_opts.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.upload_photo_default, bm_opts);
        no_upload.setImageBitmap(bitmap);

        if(getIntent().hasExtra("urls"))
        {
            urls = getIntent().getStringArrayListExtra("urls");
            uris = getIntent().getStringArrayListExtra("uris");
            imageAdapter.notifyDataSetChanged();
            Log.i("sw32adapter","checking");
        }
        else {
            urls = new ArrayList<>();
            uris = new ArrayList<>();
            imageAdapter.notifyDataSetChanged();
            Log.i("sw32adapter","checkingnew");
        }
        if (Build.VERSION.SDK_INT >= 23) {
            //do your check here
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("sw32perm","Permission is granted");
            }
            else
            {
                Log.i("sw32perm","not granted");
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL);
                }
            }
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("sw32perm","Permission is granted");
            }
            else
            {
                Log.i("sw32perm","not granted");
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL);
                }
            }
        }
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        final ViewTreeObserver observer= for_measure.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        width = for_measure.getWidth();
                        height = for_measure.getHeight();
                    }
                });

        btnSelect = (Button) findViewById(R.id.btnSelectPhoto);
        cancel = (Button) findViewById(R.id.BtnCancel);
        camera = (Button) findViewById(R.id.btnCamera);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                galleryIntent();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UploadPicturesActivity.this);
                alertDialog.setMessage("Are you sure?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(uris!=null) {
                            uris.clear();
                            urls.clear();
                            imageAdapter.notifyDataSetChanged();
                        }
                        finish();                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();



            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UploadPicturesActivity.this);
                alertDialog.setMessage("Are you sure?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(uris!=null) {
                            uris.clear();
                            urls.clear();
                            imageAdapter.notifyDataSetChanged();
                        }
                        finish();                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();



            }
        });

        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });
        gridView= (GridView) findViewById(R.id.imageGrid);

        next = (Button) findViewById(R.id.BtnNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (uris.isEmpty()) {
                        if(getCallingActivity()!=null)
                        {
                            Intent data = new Intent();
                            data.putStringArrayListExtra("urls", urls);
                            data.putStringArrayListExtra("uris", uris);
                            setResult(1, data);
                            finish();
                        }
                        else {

                            Toast.makeText(UploadPicturesActivity.this, "Please Select pictures and then press Next", Toast.LENGTH_LONG).show();
                        }
                        } else {
                        if (getCallingActivity() != null) {
                            Intent data = new Intent();
                            data.putStringArrayListExtra("urls", urls);
                            data.putStringArrayListExtra("uris", uris);
                            setResult(1, data);
                            finish();
                        } else {
                            Intent description = new Intent(UploadPicturesActivity.this, AddEventActivity.class);
                            description.putExtra("Mode", 3);
                            if (getIntent().hasExtra("courseId")) {
                                description.putExtra("courseId", getIntent().getStringExtra("courseId"));
                                description.putExtra("courseTitle", getIntent().getStringExtra("courseTitle"));
                            } else if (getIntent().hasExtra("courseName")) {
                                description.putExtra("courseName", getIntent().getStringExtra("courseName"));
                                description.putExtra("description", getIntent().getStringExtra("description"));
                            }
                            Bundle params = new Bundle();
                            params.putString("pictures_uploaded", uris.size() + " pictures");
                            firebaseAnalytics.logEvent("pictures_selected_and_continue", params);
                            description.putExtra("urls",urls);
                            description.putExtra("uris",uris);
                            startActivityForResult(description, 1);
                        }
                    }
            }
        });
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(UploadPicturesActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
        if(uris.isEmpty())
        {
            no_upload.setVisibility(View.VISIBLE);
        }
        else
            no_upload.setVisibility(View.GONE);
    }



    @Override
    public void onBackPressed() {
        cancel.performClick();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File photoFile = new File(pictureImagePath);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
            Bundle data = new Bundle();
            startActivityForResult(takePictureIntent, REQUEST_CAMERA,data);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case WRITE_EXTERNAL:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        no_upload.setVisibility(View.GONE);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE){
                onSelectFromGalleryResult(data);
            action =0;}
            else if (requestCode == REQUEST_CAMERA){
                onCaptureImageResult(data);action=1;}
        }else if(requestCode==1) {
            if (resultCode == 1) {
                urls.clear();
                uris.clear();
                imageAdapter.notifyDataSetChanged();
                String temp = data.getStringExtra("courseId");
                if (temp!=null)
                {
                    firebaseAnalytics.logEvent("upload_successful",new Bundle());
                }
                finish();
            }
            else if(resultCode==2)
            {
                final String cname = data.getStringExtra("courseName")+"";
                final String desc = data.getStringExtra("description")+"";
                Log.i("sw32uploadnext", cname + ":" + desc);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UploadPicturesActivity.this,AddEventActivity.class);
                        intent.putExtra("courseName",cname+"");
                        intent.putExtra("description",desc+"");
                        intent.putExtra("urls",urls);
                        intent.putExtra("uris",uris);
                        startActivityForResult(intent,1);
                    }
                });

            }
        }
    }
    private void onCaptureImageResult(Intent data) {
        File imgFile = new File(pictureImagePath);
        urls.add(pictureImagePath);

        if(imageAdapter.getCount()==0) {

            uris.add(Uri.fromFile(imgFile).toString());
            uris.add("add");
        }
        else
        {
            uris.add(uris.indexOf("add"),Uri.fromFile(imgFile).toString());
        }
        Log.i("sw32uri",Uri.fromFile(imgFile).toString() +"  sw32 uri");
        imageAdapter.notifyDataSetChanged();

    }


    private void onSelectFromGalleryResult(Intent data) {

        if (data.getData() != null) {
            try {
                Uri path =  data.getData();
                urls.add(getFilePath(UploadPicturesActivity.this,path));
                if(imageAdapter.getCount()==0) {
                    uris.add(path.toString());
                    uris.add("add");
                }else{uris.add(uris.indexOf("add"),path.toString());}
                imageAdapter.notifyDataSetChanged();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


        }
        else
        {
            ClipData clipData = data.getClipData();
            for (int i=0; i<clipData.getItemCount();i++)
            {
                try {
                    Uri uripath = clipData.getItemAt(i).getUri();
                    if(imageAdapter.getCount()==0) {
                        uris.add(uripath.toString());
                        uris.add("add");
                    }
                    else
                    {
                        uris.add(uris.indexOf("add"),uripath.toString());
                    }
                    urls.add(getFilePath(UploadPicturesActivity.this,uripath));
                    imageAdapter.notifyDataSetChanged();
                }  catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    View uploadDefaultActionView;
    RelativeLayout layout_default_upload;
    Bitmap bm_default_upload;
    ImageView iv_default_upload;
    Drawable drawable;

    public ImageAdapter(Context c) {
        mContext = c;
        mInflater = (LayoutInflater.from(mContext));
    }

    public int getCount() {
        if(UploadPicturesActivity.uris!=null){
            if(UploadPicturesActivity.uris.size()==0) {
                    return 0;
                }
            else {
                callbackgridview();
                return UploadPicturesActivity.uris.size();
            }}
        else
        {
            return 0;
        }

    }

    public Object getItem(int position) {

        return UploadPicturesActivity.uris.get(position);

    }





    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.gallery_item, null);
            convertView.setMinimumWidth((int)(UploadPicturesActivity.width/2));
            convertView.setMinimumHeight((int)(UploadPicturesActivity.height/2));
            holder.imageview = (ImageView) convertView.findViewById(R.id.grid_item_image);
            holder.delete = (ImageButton) convertView.findViewById(R.id.delete);
            holder.parent_layout = (RelativeLayout) convertView.findViewById(R.id.GridItem12);
            convertView.setTag(holder);

            uploadDefaultActionView =  mInflater.inflate(R.layout.card_default_upload, null);
            uploadDefaultActionView.setMinimumWidth((int)(UploadPicturesActivity.width/2));
            uploadDefaultActionView.setMinimumHeight((int)(UploadPicturesActivity.height/2));

            layout_default_upload = (RelativeLayout) uploadDefaultActionView.findViewById(R.id.container_default_upload);

            layout_default_upload.setDrawingCacheEnabled(true);

            //View to Bitmap conversion
            layout_default_upload.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            layout_default_upload.layout(0, 0, layout_default_upload.getMeasuredWidth(), layout_default_upload.getMeasuredHeight());
            layout_default_upload.buildDrawingCache(true);
            bm_default_upload = Bitmap.createBitmap(layout_default_upload.getDrawingCache());
            layout_default_upload.setDrawingCacheEnabled(false); // clear drawing cache
            //Bitmap to drawable conversion
            drawable = new BitmapDrawable(mContext.getResources(), bm_default_upload);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UploadPicturesActivity.uris.remove(position);
                    UploadPicturesActivity.urls.remove(position);
                    notifyDataSetChanged();
                }
            });



        //I have changed the .load() content of Picasso just to test...default upload is a drawable now and comes up on an error. It is getting offset for some reason.

        if(UploadPicturesActivity.uris.get(position).equals("add"))
        {
            holder.delete.setVisibility(View.GONE);
            if(UploadPicturesActivity.action == 0){
            holder.imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UploadPicturesActivity.btnSelect.performClick();
                }
            });}
            else if(UploadPicturesActivity.action ==1)
            {
                holder.imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UploadPicturesActivity.camera.performClick();
                    }
                });
            }


            Picasso.with(mContext)
                    .load(R.drawable.bk_default_upload)
                    .fit()
                    .centerInside()
                    .error(drawable)
                    .into(holder.imageview);

        }

        else {
            holder.delete.setVisibility(View.VISIBLE);
            holder.imageview.setOnClickListener(null);
            Picasso.with(mContext)
                    .load(UploadPicturesActivity.uris.get(position))
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .error(mContext.getResources().getDrawable(R.drawable.upload_enhance))
                    .fit()
                    .centerInside()
                    .into(holder.imageview);
        }

            return convertView;
    }


    public long getItemId(int position) {
        return position;
    }

    public void callbackgridview()
    {
       UploadPicturesActivity.gridView.setBackgroundColor(mContext.getResources().getColor(R.color.ColorRecyclerBackground));
    }
}
class ViewHolder {
    ImageView imageview;
    ImageButton delete;
    RelativeLayout parent_layout;
}