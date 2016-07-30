package com.campusconnect.auxiliary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.campusconnect.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 19/07/2016.
 */
public class NetworkDownDialog extends Dialog {
    public Activity c;
    public Dialog d;
    FrameLayout bk_layout;

    @Bind(R.id.iv_blur_dialog)
    ImageView bk_blur_dialog;
    @Bind(R.id.iv_err)
    ImageView error_view;
    @Bind(R.id.root_layout)
    RelativeLayout root_layout;

    int screen_width, screen_height;

    public NetworkDownDialog(Activity a, FrameLayout background) {
        super(a,android.R.style.Theme_Black_NoTitleBar);
// TODO Auto-generated constructor stub
        this.c = a;
        this.bk_layout = background;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_network_down);
        ButterKnife.bind(this);

//        Animation fadeIn = new AlphaAnimation(0, 1);
//        fadeIn.setInterpolator(new AccelerateInterpolator());
//        fadeIn.setFillAfter(true);
//        fadeIn.setStartOffset(0);
//        fadeIn.setDuration(1000);
//        root_layout.startAnimation(fadeIn);

        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screen_width = size.x;
        screen_height = size.y;




    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        applyBlur();
        super.onWindowFocusChanged(hasFocus);
    }

    private void applyBlur() {
        Bitmap icon = BitmapFactory.decodeResource(c.getResources(),
                R.drawable.splash);

//        bk_blur_dialog.getViewTreeObserver().addOnPreDrawListener(
//                new ViewTreeObserver.OnPreDrawListener() {
//                    @Override
//                    public boolean onPreDraw() {
//                        bk_blur_dialog.getViewTreeObserver().removeOnPreDrawListener(
//                                this);
//                        bk_blur_dialog.buildDrawingCache();
//
//                        Bitmap bmp = bk_blur_dialog.getDrawingCache();
//                        blur(bmp, bk_blur_dialog);
//                        return true;
//                    }
//                });
        blur(icon, bk_blur_dialog);

    }

    private void blur(Bitmap bkg, ImageView view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor;
        float radius;

        scaleFactor = 3;
        radius = 10;

        Bitmap overlay = Bitmap.createBitmap(
                (int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
                / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);

        view.setImageDrawable(new BitmapDrawable(c.getResources(), overlay));
    }
}
