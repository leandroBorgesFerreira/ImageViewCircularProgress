package br.com.simplepass.circularprogressimagelib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by leandro on 5/31/16.
 */
public class CircularAnimatedDrawableImage extends Drawable implements Animatable {
    private ValueAnimator mValueAnimatorAngle;
    private ValueAnimator mValueAnimatorSweep;
    private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator SWEEP_INTERPOLATOR = new DecelerateInterpolator();
    private static final int ANGLE_ANIMATOR_DURATION = 2000;
    private static final int SWEEP_ANIMATOR_DURATION = 900;
    private static final Float MIN_SWEEP_ANGLE = 30f;

    private final RectF fBounds = new RectF();
    private Paint mPaint;
    private Paint mPaintBg;
    private View mAnimatedView;

    private float mBorderWidth;
    private float mCurrentGlobalAngle;
    private float mCurrentSweepAngle;
    private float mCurrentGlobalAngleOffset;

    private boolean mModeAppearing;
    private boolean mRunning;


    /**
     *
     * @param view View to be animated
     * @param borderWidth The width of the spinning bar
     * @param arcColor The color of the spinning bar
     */
    public CircularAnimatedDrawableImage(View view, float borderWidth, int arcColor, Context context) {
        mAnimatedView = view;

        mBorderWidth = borderWidth;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setColor(arcColor);

        mPaintBg = new Paint();
        mPaintBg.setAntiAlias(true);
        mPaintBg.setStyle(Paint.Style.FILL);
        mPaintBg.setColor(ContextCompat.getColor(context, R.color.progress_back_ground));

        setupAnimations();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        fBounds.left = bounds.left + mBorderWidth / 2f + .5f;
        fBounds.right = bounds.right - mBorderWidth / 2f - .5f;
        fBounds.top = bounds.top + mBorderWidth / 2f + .5f;
        fBounds.bottom = bounds.bottom - mBorderWidth / 2f - .5f;

    }

    /**
     * Start the animation
     */
    @Override
    public void start() {
        if (mRunning) {
            return;
        }

        mRunning = true;
        mValueAnimatorAngle.start();
        mValueAnimatorSweep.start();
    }

    /**
     * Stops the animation
     */
    @Override
    public void stop() {
        if (!mRunning) {
            return;
        }

        mRunning = false;
        mValueAnimatorAngle.cancel();
        mValueAnimatorSweep.cancel();
    }

    /**
     * Method the inform if the animation is in process
     *
     * @return
     */
    @Override
    public boolean isRunning() {
        return mRunning;
    }

    /**
     * Method called when the drawable is going to draw itself.
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        Log.d("Drwable", "Passou aqui!");

        float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
        float sweepAngle = mCurrentSweepAngle;
        if (!mModeAppearing) {
            startAngle = startAngle + sweepAngle;
            sweepAngle = 360 - sweepAngle - MIN_SWEEP_ANGLE;
        } else {
            sweepAngle += MIN_SWEEP_ANGLE;
        }

        double radius = 1.4 * (fBounds.right - fBounds.left)/2;

        canvas.drawCircle(mAnimatedView.getWidth()/2, mAnimatedView.getHeight()/2, (float) radius, mPaintBg);
        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);

    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    /**
     * Set the Global angle of the spinning bar.
     * @param currentGlobalAngle
     */
    public void setCurrentGlobalAngle(float currentGlobalAngle) {
        mCurrentGlobalAngle = currentGlobalAngle;
    }

    /**
     * Sets the current sweep angle, so the fancy loading animation can happen
     *
     * @param currentSweepAngle
     */
    public void setCurrentSweepAngle(float currentSweepAngle) {
        mCurrentSweepAngle = currentSweepAngle;
    }

    /**
     * Set up all the animations. There are two animation: Global angle animation and sweep animation.
     */
    private void setupAnimations() {
        mValueAnimatorAngle = ValueAnimator.ofFloat(0, 360f);
        mValueAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
        mValueAnimatorAngle.setDuration(ANGLE_ANIMATOR_DURATION);
        mValueAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimatorAngle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d("Drwable", "Passou no animator!");
                setCurrentGlobalAngle((float)animation.getAnimatedValue());
                invalidateSelf();
                mAnimatedView.invalidate();
            }
        });

        mValueAnimatorSweep = ValueAnimator.ofFloat(0, 360f - 2 * MIN_SWEEP_ANGLE);
        mValueAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
        mValueAnimatorSweep.setDuration(SWEEP_ANIMATOR_DURATION);
        mValueAnimatorSweep.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimatorSweep.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                toggleAppearingMode();
            }
        });
        mValueAnimatorSweep.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d("Drwable", "Passou no animator!");
                setCurrentSweepAngle((float)animation.getAnimatedValue());
                invalidateSelf();
                mAnimatedView.invalidate();
            }
        });

    }

    /**
     * This method is called in every repetition of the animation, so the animation make the sweep
     * growing and then make it shirinking.
     */
    private void toggleAppearingMode() {
        mModeAppearing = !mModeAppearing;
        if (mModeAppearing) {
            mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360;
        }
    }


}
