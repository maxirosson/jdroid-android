package com.jdroid.android.images;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.jdroid.android.domain.FileContent;

public class CustomImageView extends ImageView implements ImageHolder {
	
	private int stubId;
	private Integer maxWidth;
	private Integer maxHeight;
	private Uri imageUri;
	private ImageLoadingListener imageLoadingListener;
	
	public CustomImageView(Context context) {
		super(context);
	}
	
	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(com.jdroid.android.domain.FileContent, int)
	 */
	@Override
	public void setImageContent(FileContent fileContent, int stubId) {
		setImageContent(fileContent, stubId, null, null);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(com.jdroid.android.domain.FileContent, int,
	 *      java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void setImageContent(FileContent fileContent, int stubId, Integer maxWidth, Integer maxHeight) {
		setImageContent(fileContent != null ? fileContent.getUri() : null, stubId, maxWidth, maxHeight);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(android.net.Uri, int)
	 */
	@Override
	public void setImageContent(Uri imageUri, int stubId) {
		setImageContent(imageUri, stubId, null, null);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(android.net.Uri, int, java.lang.Integer,
	 *      java.lang.Integer)
	 */
	@Override
	public void setImageContent(Uri imageUri, int stubId, Integer maxWidth, Integer maxHeight) {
		this.stubId = stubId;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		if (imageUri != null) {
			this.imageUri = imageUri;
			ImageLoader.get().displayImage(imageUri, this);
		} else {
			showStubImage();
			if (imageLoadingListener != null) {
				imageLoadingListener.onStubImageLoaded();
			}
		}
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#showStubImage()
	 */
	@Override
	public void showStubImage() {
		this.setImageResource(stubId);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#getMaximumWidth()
	 */
	@Override
	public Integer getMaximumWidth() {
		return maxWidth;
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#getMaximumHeight()
	 */
	@Override
	public Integer getMaximumHeight() {
		return maxHeight;
	}
	
	/**
	 * @return the imageUri
	 */
	@Override
	public Uri getImageUri() {
		return imageUri;
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#getImageLoadingListener()
	 */
	@Override
	public ImageLoadingListener getImageLoadingListener() {
		return imageLoadingListener;
	}
	
	/**
	 * @param imageLoadingListener the imageLoadingListener to set
	 */
	public void setImageLoadingListener(ImageLoadingListener imageLoadingListener) {
		this.imageLoadingListener = imageLoadingListener;
	}
	
	public static interface ImageLoadingListener {
		
		public void onImageLoaded();
		
		public void onStubImageLoaded();
	}
	
}
