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
	
	public CustomImageView(Context context) {
		super(context);
	}
	
	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setImageContent(FileContent fileContent, int stubId) {
		setImageContent(fileContent, stubId, null, null);
	}
	
	public void setImageContent(FileContent fileContent, int stubId, Integer maxWidth, Integer maxHeight) {
		setImageContent(fileContent != null ? fileContent.getUri() : null, stubId, maxWidth, maxHeight);
	}
	
	public void setImageContent(Uri imageUri, int stubId) {
		setImageContent(imageUri, stubId, null, null);
	}
	
	/**
	 * @param imageUri The image Uri
	 * @param stubId The id of the resource stub to display while the image is been downloaded
	 * @param maxWidth The maximum width of the image used to scale it. If null, the image won't be scaled
	 * @param maxHeight The maximum height of the image used to scale it. If null, the image won't be scaled
	 */
	public void setImageContent(Uri imageUri, int stubId, Integer maxWidth, Integer maxHeight) {
		this.stubId = stubId;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		if (imageUri != null) {
			this.setTag(imageUri);
			ImageLoader.get().displayImage(imageUri, this);
		} else {
			showStubImage();
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
}
