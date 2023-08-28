package com.bluetooth.cup.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class CircleImageView extends ImageView {

    private Xfermode mXfermode;
    private Drawable mDrawableBeforeBlur;
    private Bitmap mMaskBitmap;
    private Paint mPaint;

    public CircleImageView(Context context) {
        super(context);
        init();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(false);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInEditMode()) {
            int i = canvas.saveLayer(0.0f, 0.0f, getWidth(), getHeight(),
                    null, Canvas.ALL_SAVE_FLAG);
            try {
                Bitmap bitmap = null;
                BitmapDrawable drawable = (BitmapDrawable) getDrawable();
                if (drawable != null) {
                    // Allocation onDraw but it's ok because it will not always be called.
                    bitmap = Bitmap.createBitmap(getWidth(),
                            getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas bitmapCanvas = new Canvas(bitmap);

                    Bitmap drawableBitmap = drawable.getBitmap();
                    //按照短边裁剪成正方形
                    int widthOrg = drawableBitmap.getWidth();
                    int heightOrg = drawableBitmap.getHeight();
                    if (widthOrg != heightOrg) {
                        int square = Math.min(widthOrg, heightOrg);
                        //从图中截取正中间的正方形部分。
                        int x = square == widthOrg ? 0 : (widthOrg - square) / 2;
                        int y = square == heightOrg ? 0 : (heightOrg - heightOrg) / 2;

                        drawableBitmap = Bitmap.createBitmap(drawableBitmap, x, y, square, square);
                    }

                    RectF rectf = new RectF(0, 0, getWidth(), getHeight());

                    bitmapCanvas.drawBitmap(drawableBitmap, null, rectf, mPaint);

                    if (mMaskBitmap == null || mMaskBitmap.isRecycled()) {
                        mMaskBitmap = getBitmap(getWidth(), getHeight());
                    }
                    mPaint.setXfermode(mXfermode);
                    bitmapCanvas.drawBitmap(mMaskBitmap, 0.0f, 0.0f, mPaint);
                }

                // Bitmap already loaded.
                if (bitmap != null) {
                    mPaint.setXfermode(null);
                    mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, mPaint);
                    return;
                }
            } catch (Exception e) {
                System.gc();
            } finally {
                canvas.restoreToCount(i);
            }
        } else {
            super.onDraw(canvas);
        }
    }

    public static Bitmap getBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        canvas.drawOval(new RectF(0.0f, 0.0f, width, height), paint);
        return bitmap;
    }
}
