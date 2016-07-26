package com.campusconnect.auxiliary;


import android.animation.Animator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public abstract class AnimationAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int size;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    private int mDuration = 600;
    private Interpolator mInterpolator = new DecelerateInterpolator();
    private int mLastPosition = -1;

    private boolean isFirstOnly = false;

    public AnimationAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {

        mAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        size=parent.getHeight();
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mAdapter.onBindViewHolder(holder, position);


        if (!isFirstOnly && position > mLastPosition) {
            for (Animator anim : getAnimators(holder.itemView,size)) {
                anim.setDuration(mDuration).start();
                anim.setInterpolator(mInterpolator);

            }
            mLastPosition = position;
        } else {
            ViewHelper.clear(holder.itemView);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void setStartPosition(int start) {
        mLastPosition = start;
    }

    protected abstract Animator[] getAnimators(View view, int size);

    public void setFirstOnly(boolean firstOnly) {
        isFirstOnly = firstOnly;
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }
}

