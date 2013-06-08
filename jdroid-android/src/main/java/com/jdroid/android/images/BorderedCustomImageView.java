package com.jdroid.android.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import com.jdroid.android.R;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.images.CustomImageView.ImageLoadingListener;

/**
 * 
 * @author Maxi Rosson
 */
public class BorderedCustomImageView extends LinearLayout implements ImageHolder {
	
	private CustomImageView customImageView;
	
	public BorderedCustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public BorderedCustomImageView(Context context) {
		super(context);
	}
	
	public BorderedCustomImageView(Context context, int defaultImageResId, int widthResId, int heightResId) {
		super(context);
		LayoutParams lp = new LayoutParams(getResources().getDimensionPixelSize(widthResId),
				getResources().getDimensionPixelSize(heightResId));
		int margin = getResources().getDimensionPixelSize(R.dimen.borderedCustomImageViewMargin);
		lp.setMargins(margin, margin, margin, margin);
		setLayoutParams(lp);
		setBackgroundColor(Color.WHITE);
		
		customImageView = new CustomImageView(getContext());
		lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		margin = getResources().getDimensionPixelSize(R.dimen.borderedCustomImageViewPadding);
		lp.setMargins(margin, margin, margin, margin);
		customImageView.setLayoutParams(lp);
		customImageView.setImageResource(defaultImageResId);
		customImageView.setScaleType(ScaleType.CENTER_CROP);
		customImageView.setId(R.id.image);
		addView(customImageView);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageBitmap(android.graphics.Bitmap)
	 */
	@Override
	public void setImageBitmap(Bitmap bitmap) {
		customImageView.setImageBitmap(bitmap);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#showStubImage()
	 */
	@Override
	public void showStubImage() {
		customImageView.showStubImage();
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#getMaximumWidth()
	 */
	@Override
	public Integer getMaximumWidth() {
		return customImageView.getMaximumWidth();
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#getMaximumHeight()
	 */
	@Override
	public Integer getMaximumHeight() {
		return customImageView.getMaximumHeight();
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(android.net.Uri, int)
	 */
	@Override
	public void setImageContent(Uri imageUri, int stubId) {
		customImageView.setImageContent(imageUri, stubId);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(com.jdroid.android.domain.FileContent, int,
	 *      java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void setImageContent(FileContent fileContent, int stubId, Integer maxWidth, Integer maxHeight) {
		customImageView.setImageContent(fileContent, stubId, maxWidth, maxHeight);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(com.jdroid.android.domain.FileContent, int)
	 */
	@Override
	public void setImageContent(FileContent fileContent, int stubId) {
		customImageView.setImageContent(fileContent, stubId);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(android.net.Uri, int, java.lang.Integer,
	 *      java.lang.Integer)
	 */
	@Override
	public void setImageContent(Uri imageUri, int stubId, Integer maxWidth, Integer maxHeight) {
		customImageView.setImageContent(imageUri, stubId, maxWidth, maxHeight);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#getImageUri()
	 */
	@Override
	public Uri getImageUri() {
		return customImageView.getImageUri();
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#getImageLoadingListener()
	 */
	@Override
	public ImageLoadingListener getImageLoadingListener() {
		return customImageView.getImageLoadingListener();
	}
}
