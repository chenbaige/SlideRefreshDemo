package com.hbandroid.sliderefreshdemo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * creater：cyy
 * time：2017/2/8
 * describe：
 */
public class RecyclerViewDivider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mDividerHeight = 0;
    private Paint mPaint;

    private int mOrientation;

    private final Rect mBounds = new Rect();
    private Rect mMarginBounds;

    public RecyclerViewDivider() {
        mOrientation = LinearLayout.VERTICAL;
    }

    public RecyclerViewDivider(boolean isVertical) {
        if (isVertical) {
            mOrientation = LinearLayout.VERTICAL;
        } else {
            mOrientation = LinearLayout.HORIZONTAL;

        }
    }

    public void setDividerHeight(int px) {
        this.mDividerHeight = px;
    }

    public void setDividerHeight(Context c, int dp) {
        this.mDividerHeight = (int) (dp * c.getResources().getDisplayMetrics().density);
    }

    public void setDrawable(@NonNull Drawable drawable) {
        mDivider = drawable;
    }

    public void setColor(int color) {
        mPaint = new Paint();
        mPaint.setColor(color);
    }

    public void setColor(Context c, @ColorRes int colorId) {
        setColor(ContextCompat.getColor(c, colorId));
    }

    public void setMargin(int left, int top, int right, int bottom) {
        mMarginBounds = new Rect();
        mMarginBounds.left = left;
        mMarginBounds.top = top;
        mMarginBounds.right = right;
        mMarginBounds.bottom = bottom;
    }

    public void setMargin(Context c, int leftDp, int topDp, int rightDp, int bottomDp) {
        float density = c.getResources().getDisplayMetrics().density;
        mMarginBounds = new Rect();
        mMarginBounds.left = (int) (leftDp * density);
        mMarginBounds.top = (int) (topDp * density);
        mMarginBounds.right = (int) (rightDp * density);
        mMarginBounds.bottom = (int) (bottomDp * density);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        if (mOrientation == LinearLayout.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    @SuppressLint("NewApi")
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount() -1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
            int top = bottom - mDividerHeight;
            if (null != mDivider) {
                int sicWidth = mDivider.getIntrinsicWidth();
                if (sicWidth > 0) {
                    top = bottom - sicWidth;
                }
                if (null != mMarginBounds) {
                    mDivider.setBounds(left + mMarginBounds.left, top + mMarginBounds.top,
                            right - mMarginBounds.right, bottom - mMarginBounds.bottom);
                } else {
                    mDivider.setBounds(left, top, right, bottom);
                }
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                if (null != mMarginBounds) {
                    canvas.drawRect(left + mMarginBounds.left, top + mMarginBounds.top,
                            right - mMarginBounds.right, bottom - mMarginBounds.bottom, mPaint);
                } else {
                    canvas.drawRect(left, top, right, bottom, mPaint);
                }
            }
        }
        canvas.restore();
    }

    @SuppressLint("NewApi")
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        final int childCount = parent.getChildCount() -1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            int right = mBounds.right + Math.round(ViewCompat.getTranslationX(child));
            int left = right - mDividerHeight;
            if (null != mDivider) {
                int sicWidth = mDivider.getIntrinsicWidth();
                if (sicWidth > 0) {
                    left = right - sicWidth;
                }
                if (null != mMarginBounds) {
                    mDivider.setBounds(left + mMarginBounds.left, top + mMarginBounds.top,
                            right - mMarginBounds.right, bottom - mMarginBounds.bottom);
                } else {
                    mDivider.setBounds(left, top, right, bottom);
                }
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                if (null != mMarginBounds) {
                    canvas.drawRect(left + mMarginBounds.left, top + mMarginBounds.top,
                            right - mMarginBounds.right, bottom - mMarginBounds.bottom, mPaint);
                } else {
                    canvas.drawRect(left, top, right, bottom, mPaint);
                }
            }
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        int lastCount = parent.getAdapter().getItemCount();
        //如果当前条目与是最后一个条目，就不设置divider padding
        if (childAdapterPosition == lastCount) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        if (mOrientation == LinearLayout.VERTICAL) {
            if (0 == mDividerHeight && null != mDivider) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());

            } else {
                outRect.set(0, 0, 0, mDividerHeight);
            }
        } else {
            if (0 == mDividerHeight && null != mDivider) {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            } else {
                outRect.set(0, 0, mDividerHeight, 0);

            }
        }
    }
}
