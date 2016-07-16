package com.campusconnect.cc_reboot.auxiliary;



import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class AlphaInAnimationAdapter extends AnimationAdapter {

    public AlphaInAnimationAdapter(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    protected Animator[] getAnimators(View view, int size) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationY", size>>1, 0)
        };
    }
}

