package hu.bme.jurinmaxim.mobwebnhf.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import java.util.Random;

import hu.bme.jurinmaxim.mobwebnhf.R;

public class LetterImageView extends android.support.v7.widget.AppCompatImageView {

    private char mLetter;
    private Paint mTextPaint;
    private Paint mBackgroundPaint;
    private int mTextColor = Color.WHITE;
    private boolean isOval;

    public LetterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(randomColor());
    }

    public void setLetter(char letter) {
        mLetter = letter;
        invalidate();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        invalidate();
    }

    public boolean isOval() {
        return isOval;
    }

    public void setOval(boolean oval) {
        isOval = oval;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getDrawable() == null) {
            mTextPaint.setTextSize(getHeight() - getTextPadding() * 2);
            if (isOval()) {
                canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, Math.min(getWidth(), getHeight()) / 2f,
                        mBackgroundPaint);
            } else {
                canvas.drawRect(0, 0, getWidth(), getHeight(), mBackgroundPaint);
            }
            @SuppressLint("DrawAllocation") Rect textBounds = new Rect();
            mTextPaint.getTextBounds(String.valueOf(mLetter), 0, 1, textBounds);
            float textWidth = mTextPaint.measureText(String.valueOf(mLetter));
            float textHeight = textBounds.height();
            canvas.drawText(String.valueOf(mLetter), getWidth() / 2f - textWidth / 2f,
                    getHeight() / 2f + textHeight / 2f, mTextPaint);
        }
    }

    private float getTextPadding() {
        return 8 * getResources().getDisplayMetrics().density;
    }

    private int randomColor() {
        Random random = new Random();
        String[] colorsArr = getResources().getStringArray(R.array.colors);
        return Color.parseColor(colorsArr[random.nextInt(colorsArr.length)]);
    }
}
