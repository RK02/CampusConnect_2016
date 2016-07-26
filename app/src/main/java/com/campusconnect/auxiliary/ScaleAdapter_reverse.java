package com.campusconnect.auxiliary;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ScaleAdapter_reverse extends AnimationAdapter {

    public ScaleAdapter_reverse(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    protected Animator[] getAnimators(View view, int size) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationY", 0, size>>1)
        };
    }
}

