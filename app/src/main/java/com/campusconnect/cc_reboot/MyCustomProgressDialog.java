package com.campusconnect.cc_reboot;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 15/07/2016.
 */
public class MyCustomProgressDialog extends ProgressDialog {
    private AnimationDrawable animation;
    public static ProgressDialog ctor(Context context) {
        MyCustomProgressDialog dialog = new MyCustomProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }
    public MyCustomProgressDialog(Context context) {
        super(context);
    }
    public MyCustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Bind(R.id.container_app_icon)
    RelativeLayout app_icon_container;
    @Bind(R.id.iv_app_icon)
    ImageView app_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom_progress);
        ButterKnife.bind(this);

    }
    @Override
    public void show() {
        super.show();
        TransitionDrawable transition = (TransitionDrawable) app_icon_container.getBackground();
        transition.startTransition(550);
    }
    @Override
    public void dismiss() {
        super.dismiss();
    }
}
