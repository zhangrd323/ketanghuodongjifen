package com.example.meeting.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.meeting.ConvertUtils;

/**
 * @Describe
 * @Author thc 悬浮顶部的headerDecoration
 * @Date 2017/3/31
 */
public class SectionDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "SectionDecoration";

    private DecorationCallback callback;
    private TextPaint textPaint;
    private Paint paint;
    private Paint linePaint;//划线
    private int topGap;
    private Paint.FontMetrics fontMetrics;
    private int mFirstPos;
    /**
     * 首字母距离左边距离，默认15dp.根据实际情况计算，传入
     */
    private int marginLeft = 15;

    public SectionDecoration(Context context, DecorationCallback decorationCallback, int sectionColor, int textColor, int sectionHeight, int textSize, int marginLeft) {
        this.callback = decorationCallback;

        paint = new Paint();
        paint.setColor(sectionColor);
        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#e5e5e5"));
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(ConvertUtils.sp2px(context, textSize));
        textPaint.setColor(textColor);
        textPaint.getFontMetrics(fontMetrics);
        textPaint.setTextAlign(Paint.Align.LEFT);
        fontMetrics = new Paint.FontMetrics();
        topGap = ConvertUtils.dp2px(context, sectionHeight);
        this.marginLeft = marginLeft;

    }


    public SectionDecoration setFirstPos(int firstPos) {
        mFirstPos = firstPos;
        return this;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        Log.i(TAG, "getItemOffsets：" + pos);
        long groupId = callback.getGroupId(pos);
        if (groupId < 0) return;
        if (isFirstInGroup(pos) && groupId != '*') { //同组的第一个才添加padding,并且不是第一组
            outRect.top = topGap;
        } else {
            outRect.top = 0;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        long preGroupId, groupId = -1;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);

            preGroupId = groupId;
            groupId = callback.getGroupId(position);
            if (groupId < 0 || groupId == preGroupId) continue;

            String textLine = callback.getGroupFirstLine(position).toUpperCase();
            if (TextUtils.isEmpty(textLine)) continue;

            int viewBottom = view.getBottom();
            float textY = Math.max(topGap, view.getTop());
            if (position + 1 < itemCount) { // 下一个和当前不一样移动当前
                long nextGroupId = callback.getGroupId(position + 1);
                if (nextGroupId != groupId && viewBottom < textY) { // 组内最后一个view进入了header
                    textY = viewBottom;
                }
            }

            c.drawRect(left, textY - topGap, right, textY, paint);
            c.drawText(textLine, ConvertUtils.dp2px(view.getContext(), marginLeft), textY - ConvertUtils.dp2px(view.getContext(), 10), textPaint);
            if (isShowLine()) {
                c.drawLine(left, textY - topGap, right - ConvertUtils.dp2px(view.getContext(), 25), textY - topGap, linePaint);
            }
        }
    }

    private boolean isFirstInGroup(int pos) {
        if (pos == 0 && mFirstPos != 0) {
            return false;
        } else if (pos == mFirstPos) {
            return true;
        } else {
            long prevGroupId = callback.getGroupId(pos - 1);
            long groupId = callback.getGroupId(pos);
            return prevGroupId != groupId;
        }
    }

    protected boolean isShowLine() {
        return true;
    }


    public interface DecorationCallback {

        long getGroupId(int position);

        String getGroupFirstLine(int position);
    }
}