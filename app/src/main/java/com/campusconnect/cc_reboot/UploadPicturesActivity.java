package com.campusconnect.cc_reboot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UploadPicturesActivity extends AppCompatActivity {

    @Bind(R.id.container_action_buttons)
    LinearLayout action_buttons_top;
    @Bind(R.id.container_action_buttons_more)
    LinearLayout action_buttons_bottom;

    @Bind(R.id.for_height)
    View for_measure;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private Button next;
    private Button camera;
    Button cancel;
    private  String pictureImagePath="";
    GridView gridView;
    ImageAdapter imageAdapter;
    public static ArrayList<String> urls;
    public static ArrayList<String> uris;

    public static float width;
    public static float height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pictures);

        ButterKnife.bind(this);

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
                uris.clear();
                urls.clear();
                imageAdapter.notifyDataSetChanged();
                finish();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });
        gridView= (GridView) findViewById(R.id.imageGrid);

        imageAdapter = new ImageAdapter(this);
        next = (Button) findViewById(R.id.BtnNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uris.isEmpty()){
                    Toast.makeText(UploadPicturesActivity.this,"Please Select pictures and then press Next",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent description = new Intent(UploadPicturesActivity.this, AddEventActivity.class);
                    description.putExtra("Mode",3);
                    if(CoursePageActivity.courseId!=null) {
                        description.putExtra("courseId", CoursePageActivity.courseId);
                        description.putExtra("courseTitle", CoursePageActivity.courseTitle);
                    }
                    startActivityForResult(description,1);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }else if(requestCode==1)
        {
            if(resultCode==1) {
                urls.clear();
                uris.clear();
                imageAdapter.notifyDataSetChanged();
            }

        }
    }
    private void onCaptureImageResult(Intent data) {
        File imgFile = new File(pictureImagePath);
        urls.add(pictureImagePath);
        uris.add(imgFile.toURI().toString());
        imageAdapter.notifyDataSetChanged();

    }


    private void onSelectFromGalleryResult(Intent data) {

        if (data.getData() != null) {
            try {
                Uri path =  data.getData();
                urls.add(getFilePath(UploadPicturesActivity.this,path));
                uris.add(uris.toString());
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
                    uris.add(uripath.toString());
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


    public ImageAdapter(Context c) {
        mContext = c;
        mInflater = (LayoutInflater.from(mContext));
    }

    public int getCount() {
        return UploadPicturesActivity.uris.size();
    }

    public Object getItem(int position) {
        return UploadPicturesActivity.uris.get(position);
    }

    public long getItemId(int position) {
        return position;
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
            convertView.setTag(holder);
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
        Picasso.with(mContext)
                .load(UploadPicturesActivity.uris.get(position))
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .error(R.mipmap.ic_pages_18)
                .into(holder.imageview);
        return convertView;
    }
}
class ViewHolder {
    ImageView imageview;
    ImageButton delete;
}