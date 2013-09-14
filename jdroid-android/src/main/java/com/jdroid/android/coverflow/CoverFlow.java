package com.jdroid.android.coverflow;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import com.jdroid.java.utils.ReflectionUtils;

/**
 * Cover Flow implementation.
 * 
 * http://code.google.com/p/android-coverflow/
 */
public class CoverFlow extends Gallery {
	
	/**
	 * Graphics Camera used for transforming the matrix of ImageViews.
	 */
	private final Camera camera = new Camera();
	
	/**
	 * The maximum angle the Child ImageView will be rotated by.
	 */
	private int maxRotationAngle = 40;
	
	/**
	 * The maximum zoom on the centre Child.
	 */
	private int maxZoom = 60;
	
	public CoverFlow(final Context context) {
		super(context);
		this.setStaticTransformationsEnabled(true);
	}
	
	public CoverFlow(final Context context, final AttributeSet attrs) {
		this(context, attrs, android.R.attr.galleryStyle);
	}
	
	public CoverFlow(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		setSpacing(-3);
		this.setStaticTransformationsEnabled(true);
	}
	
	/**
	 * Set the max rotational angle of each image.
	 * 
	 * @param maxRotationAngle the mMaxRotationAngle to set
	 */
	public void setMaxRotationAngle(final int maxRotationAngle) {
		this.maxRotationAngle = maxRotationAngle;
	}
	
	/**
	 * Set the max zoom of the centre image.
	 * 
	 * @param maxZoom the mMaxZoom to set
	 */
	public void setMaxZoom(final int maxZoom) {
		this.maxZoom = maxZoom;
	}
	
	/**
	 * @see android.widget.Gallery#getChildStaticTransformation(android.view.View,
	 *      android.view.animation.Transformation)
	 */
	@Override
	protected boolean getChildStaticTransformation(final View child, final Transformation t) {
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);
		View selectedChild = ReflectionUtils.getFieldValue(this, "mSelectedChild");
		if (selectedChild != null) {
			transformImageBitmap(child, t, (Integer)(selectedChild.getTag()));
		}
		return true;
	}
	
	/**
	 * Transform the Image Bitmap by the Angle passed.
	 * 
	 * @param imageView ImageView the ImageView whose bitmap we want to rotate
	 * @param t transformation
	 * @param rotationAngle the Angle by which to rotate the Bitmap
	 */
	private void transformImageBitmap(View child, Transformation t, int selectedIndex) {
		camera.save();
		
		int currentIndex = (Integer)child.getTag();
		boolean isSelected = currentIndex == selectedIndex;
		child.setAlpha(isSelected ? 1 : 0.3f);
		
		Matrix imageMatrix = t.getMatrix();
		int height = child.getLayoutParams().height;
		int width = child.getLayoutParams().width;
		
		float zoomAmount = isSelected ? -maxZoom : maxZoom;
		camera.translate(0.0f, 0.0f, zoomAmount);
		
		float rotationAngle = 0;
		if (!isSelected) {
			rotationAngle = currentIndex < selectedIndex ? maxRotationAngle : -maxRotationAngle;
		}
		camera.rotateY(rotationAngle);
		
		camera.getMatrix(imageMatrix);
		imageMatrix.preTranslate(-(width / 2.0f), -(height / 2.0f));
		imageMatrix.postTranslate((width / 2.0f), (height / 2.0f));
		camera.restore();
	}
}
