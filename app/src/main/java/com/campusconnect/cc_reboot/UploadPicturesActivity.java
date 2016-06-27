package com.campusconnect.cc_reboot;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
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
    public static HashMap<Bitmap,String> images_paths;


    public static float width;
    public static float height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pictures);

        ButterKnife.bind(this);
        images_paths = new HashMap<>();

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
                ImageAdapter.mThumbIds.clear();
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
                if(ImageAdapter.mThumbIds.isEmpty()){
                    Toast.makeText(UploadPicturesActivity.this,"Please Select pictures and then press Next",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent description = new Intent(UploadPicturesActivity.this, AddEventActivity.class);
                    description.putExtra("Mode",3);
                    description.putExtra("courseId",CoursePageActivity.courseId);
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
                ImageAdapter.mThumbIds.clear();
                images_paths.clear();
                imageAdapter.notifyDataSetChanged();
            }

        }
    }

    private void onCaptureImageResult(Intent data) {
        File imgFile = new File(pictureImagePath);
        Bitmap imageBitmap;
        imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(imgFile.getAbsolutePath());
        }
        catch(Exception e){}
        int orientation = 0;
        if (ei != null) {
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        }
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                imageBitmap = rotateImage(imageBitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                imageBitmap = rotateImage(imageBitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                imageBitmap = rotateImage(imageBitmap, 270);
                break;
        }
        Bitmap temp = Bitmap.createScaledBitmap(imageBitmap,400,400,false);
        ImageAdapter.mThumbIds.add(temp);
        images_paths.put(temp,pictureImagePath);
        imageAdapter.notifyDataSetChanged();

    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;


        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data.getData() != null) {
            try {
                Uri path =  data.getData();
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Bitmap temp = Bitmap.createScaledBitmap(bm,400,600,false);
                ImageAdapter.mThumbIds.add(temp);
                images_paths.put(temp,getPictureImagePath(path));
                imageAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else
        {
            ClipData clipData = data.getClipData();
            ArrayList<Bitmap> bitmaps = new ArrayList<>();
            for (int i=0; i<clipData.getItemCount();i++)
            {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), clipData.getItemAt(i).getUri());
                    Uri uripath = clipData.getItemAt(i).getUri();
                    Bitmap temp = Bitmap.createScaledBitmap(bitmap,400,600,false);
                    ImageAdapter.mThumbIds.add(temp);
                    images_paths.put(temp,getPictureImagePath(uripath));
                    imageAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    String getPictureImagePath(Uri uripath)
    {
        String wholeID = DocumentsContract.getDocumentId(uripath);

// Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

// where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();
        return filePath;
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
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return mThumbIds.get(position);
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
        holder.imageview.setId(position);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThumbIds.remove(position);
                notifyDataSetChanged();
            }
        });
//        holder.imageview.setImageBitmap(Bitmap.createScaledBitmap(mThumbIds.get(position),UploadPicturesActivity.width/2,UploadPicturesActivity.height/2,false));
        holder.imageview.setImageBitmap(mThumbIds.get(position));

        return convertView;
    }
    static ArrayList<Bitmap> mThumbIds = new ArrayList<>();
}
class ViewHolder {
    ImageView imageview;
    ImageButton delete;
}