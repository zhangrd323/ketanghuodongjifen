package com.example.meeting.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.meeting.ConvertUtils;
import com.example.meeting.R;

public class SideLetterBar2 extends View {
    private String[] mLetters = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int choose = -1; // 选中的字母
    private Paint wordsPaint = new Paint(); // 画笔
    private boolean showBg = false; // 是否显示背景
    private OnLetterChangedListener onLetterChangedListener; // 字母改变监听
    private TextView overlay; // 正中显示的悬浮的字母
    /*字母背景画笔*/
    private Paint bgPaint = new Paint();
    /*每一个字母的宽度*/
    private int itemWidth;
    /*每一个字母的高度*/
    private int itemHeight;
    private static final String TAG = "SideLetterBar2";
    /**
     * 固定背景半径
     */
    private float radius;

    public SideLetterBar2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideLetterBar2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideLetterBar2(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getMeasuredWidth();
        itemHeight = getMeasuredHeight() / 27;//26，#个字母
        //动态设置宽高
        setMeasuredDimension(itemWidth, itemHeight * mLetters.length);
    }

    public void setmLetters(String[] items) {
        this.mLetters = items;
    }


    /**
     * 设置悬浮的textview
     *
     * @param overlay
     */
    public void setOverlay(TextView overlay) {
        this.overlay = overlay;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画背景
        if (showBg) {
            canvas.drawColor(Color.TRANSPARENT);
        }
        //获取固定字母半径
        Paint raPaint = new Paint();
        raPaint.setTextSize(ConvertUtils.sp2px(getContext(), 14));
        raPaint.setAntiAlias(true);
//        radius = wordsPaint.measureText("H");
        Rect rect = new Rect();
        raPaint.getTextBounds("H", 0, 1, rect);
        radius = rect.width();
        for (int i = 0; i < mLetters.length; i++) {

            wordsPaint.setTextSize(ConvertUtils.sp2px(getContext(), 14));
            wordsPaint.setAntiAlias(true);

            if (choose == i) {
                //绘制文字圆形背景
                bgPaint.setColor(getResources().getColor(R.color.qianse));
                canvas.drawCircle(itemWidth / 2, i * itemHeight + radius, radius, bgPaint);
                wordsPaint.setColor(Color.BLACK);
            } else {
                wordsPaint.setColor(Color.GRAY);
            }
//
//            //绘制字母
//            float wordX = itemWidth / 2 - wordWidth / 2;
//            float wordY = itemWidth / 2 + i * itemHeight;
//            canvas.drawText(mLetters[i], wordX, wordY, wordsPaint);


            //获取文字的宽高,部分字母过宽
            wordsPaint.getTextBounds(mLetters[i], 0, 1, rect);
            int wordWidth = rect.width();
            float xPos;
            if (wordWidth > radius) {
                xPos = itemWidth / 2 - wordWidth / 2;
            } else {
                xPos = itemWidth / 2 - radius / 2;
            }

            // 文字绘制的起始Y
            float yPos = itemHeight * i + radius * 3 / 2;
            canvas.drawText(mLetters[i], xPos, yPos, wordsPaint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnLetterChangedListener listener = onLetterChangedListener;
        final int c = (int) (y / getHeight() * mLetters.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBg = true;
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < mLetters.length) {
                        listener.onLetterChanged(mLetters[c]);
                        choose = c;
                        invalidate();
                        if (overlay != null) {
                            overlay.setVisibility(VISIBLE);
                            overlay.setText(mLetters[c]);
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < mLetters.length) {
                        listener.onLetterChanged(mLetters[c]);
                        choose = c;
                        invalidate();
                        if (overlay != null) {
                            overlay.setVisibility(VISIBLE);
                            overlay.setText(mLetters[c]);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBg = false;
                choose = -1;
                invalidate();
                if (overlay != null) {
                    overlay.setVisibility(GONE);
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnLetterChangedListener(OnLetterChangedListener onLetterChangedListener) {
        this.onLetterChangedListener = onLetterChangedListener;
    }

    public interface OnLetterChangedListener {
        void onLetterChanged(String letter);
    }
}
