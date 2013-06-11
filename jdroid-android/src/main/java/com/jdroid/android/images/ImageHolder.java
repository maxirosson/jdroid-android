package com.jdroid.android.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.images.CustomImageView.ImageLoadingListener;

/**
 * 
 * @author Maxi Rosson
 */
public interface ImageHolder {
	
	public void setImageBitmap(Bitmap bitmap);
	
	public void showStubImage();
	
	public Integer getMaximumWidth();
	
	public Integer getMaximumHeight();
	
	public Uri getImageUri();
	
	public Context getContext();
	
	public void setImageContent(Uri imageUri, int stubId);
	
	public void setImageContent(FileContent fileContent, int stubId, Integer maxWidth, Integer maxHeight);
	
	public void setImageContent(FileContent fileContent, int stubId);
	
	/**
	 * @param imageUri The image Uri
	 * @param stubId The id of the resource stub to display while the image is been downloaded
	 * @param maxWidth The maximum width of the image used to scale it. If null, the image won't be scaled
	 * @param maxHeight The maximum height of the image used to scale it. If null, the image won't be scaled
	 */
	public void setImageContent(Uri imageUri, int stubId, Integer maxWidth, Integer maxHeight);
	
	public ImageLoadingListener getImageLoadingListener();
	
	public void runOnUiThread(Runnable runnable);
}
