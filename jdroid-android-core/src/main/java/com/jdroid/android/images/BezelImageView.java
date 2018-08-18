package com.jdroid.android.images;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jdroid.android.R;

/**
 * An {@link ImageView} that draws its contents inside a mask and draws a border
 * drawable on top. This is useful for applying a beveled look to image contents, but is also
 * flexible enough for use with other desired aesthetics.
 */
public class BezelImageView extends AppCompatImageView {

	private Paint mBlackPaint;
	private Paint mMaskedPaint;

	private Rect mBounds;
	private RectF mBoundsF;

	private Drawable borderDrawable;
	protected Drawable maskDrawable;

	private ColorMatrixColorFilter mDesaturateColorFilter;
	private boolean mDesaturateOnPress = false;

	private boolean mCacheValid = false;
	private Bitmap mCacheBitmap;
	private int mCachedWidth;
	private int mCachedHeight;

	public BezelImageView(Context context) {
		this(context, null);
	}

	public BezelImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BezelImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		attributeInitialization(context, attrs, defStyle);

		// Other initialization
		mBlackPaint = new Paint();
		mBlackPaint.setColor(0xff000000);

		mMaskedPaint = new Paint();
		mMaskedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

		// Always want a cache allocated.
		mCacheBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

		if (mDesaturateOnPress) {
			// Create a desaturate color filter for pressed state.
			ColorMatrix cm = new ColorMatrix();
			cm.setSaturation(0);
			mDesaturateColorFilter = new ColorMatrixColorFilter(cm);
		}
	}

	protected void attributeInitialization(Context context, AttributeSet attrs, int defStyle) {
		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.jdroid_bezelImageView,
			defStyle, 0);

		if (maskDrawable == null) {
			maskDrawable = a.getDrawable(R.styleable.jdroid_bezelImageView_jdroid_maskDrawable);
			if (maskDrawable != null) {
				maskDrawable.setCallback(this);
			}
		}

		borderDrawable = a.getDrawable(R.styleable.jdroid_bezelImageView_jdroid_borderDrawable);
		if (borderDrawable != null) {
			borderDrawable.setCallback(this);
		}

		mDesaturateOnPress = a.getBoolean(R.styleable.jdroid_bezelImageView_jdroid_desaturateOnPress,
			mDesaturateOnPress);

		a.recycle();
	}

	@Override
	protected boolean setFrame(int l, int t, int r, int b) {
		final boolean changed = super.setFrame(l, t, r, b);
		mBounds = new Rect(0, 0, r - l, b - t);
		mBoundsF = new RectF(mBounds);

		if (borderDrawable != null) {
			borderDrawable.setBounds(mBounds);
		}
		if (maskDrawable != null) {
			maskDrawable.setBounds(mBounds);
		}

		if (changed) {
			mCacheValid = false;
		}

		return changed;
	}

	@SuppressLint("WrongConstant")
	@Override
	protected void onDraw(@NonNull Canvas canvas) {
		if (mBounds == null) {
			return;
		}

		int width = mBounds.width();
		int height = mBounds.height();

		if (width == 0 || height == 0) {
			return;
		}

		if (!mCacheValid || width != mCachedWidth || height != mCachedHeight) {
			// Need to redraw the cache
			if (width == mCachedWidth && height == mCachedHeight) {
				// Have a correct-sized bitmap cache already allocated. Just erase it.
				mCacheBitmap.eraseColor(0);
			} else {
				// Allocate a new bitmap with the correct dimensions.
				mCacheBitmap.recycle();
				//noinspection AndroidLintDrawAllocation
				mCacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
				mCachedWidth = width;
				mCachedHeight = height;
			}

			Canvas cacheCanvas = new Canvas(mCacheBitmap);
			if (maskDrawable != null) {
				int sc = cacheCanvas.save();
				maskDrawable.draw(cacheCanvas);
				mMaskedPaint.setColorFilter((mDesaturateOnPress && isPressed())
					? mDesaturateColorFilter : null);
				cacheCanvas.saveLayer(mBoundsF, mMaskedPaint,
					Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
				super.onDraw(cacheCanvas);
				cacheCanvas.restoreToCount(sc);
			} else if (mDesaturateOnPress && isPressed()) {
				int sc = cacheCanvas.save();
				cacheCanvas.drawRect(0, 0, mCachedWidth, mCachedHeight, mBlackPaint);
				mMaskedPaint.setColorFilter(mDesaturateColorFilter);
				cacheCanvas.saveLayer(mBoundsF, mMaskedPaint,
					Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
				super.onDraw(cacheCanvas);
				cacheCanvas.restoreToCount(sc);
			} else {
				super.onDraw(cacheCanvas);
			}

			if (borderDrawable != null) {
				borderDrawable.draw(cacheCanvas);
			}
		}

		// Draw from cache
		canvas.drawBitmap(mCacheBitmap, mBounds.left, mBounds.top, null);
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		if (borderDrawable != null && borderDrawable.isStateful()) {
			borderDrawable.setState(getDrawableState());
		}
		if (maskDrawable != null && maskDrawable.isStateful()) {
			maskDrawable.setState(getDrawableState());
		}
		if (isDuplicateParentStateEnabled()) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	@Override
	public void invalidateDrawable(@NonNull Drawable who) {
		if (who == borderDrawable || who == maskDrawable) {
			invalidate();
		} else {
			super.invalidateDrawable(who);
		}
	}

	@Override
	protected boolean verifyDrawable(@NonNull Drawable who) {
		return who == borderDrawable || who == maskDrawable || super.verifyDrawable(who);
	}
}
