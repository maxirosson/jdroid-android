package com.jdroid.android.picture;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jdroid.android.R;
import com.jdroid.android.dialog.AbstractDialogFragment;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.DeviceUtils;
import com.jdroid.java.date.DateUtils;

import java.io.File;

public class PictureDialogFragment extends AbstractDialogFragment {
	
	private static final String OUTPUT_FILE_URI_EXTRA = "outputFileUriExtra";
	
	private static final String IMAGE_TYPE = "image/*";
	
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int GALLERY_REQUEST_CODE = 2;
	
	private Uri outputFileUri;
	
	public static Boolean display() {
		return DeviceUtils.hasCamera();
	}
	
	public static void show(Fragment targetFragment) {
		FragmentManager fm = targetFragment.getActivity().getSupportFragmentManager();
		PictureDialogFragment pictureDialogFragment = new PictureDialogFragment();
		pictureDialogFragment.setTargetFragment(targetFragment, 1);
		pictureDialogFragment.show(fm, PictureDialogFragment.class.getSimpleName());
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
			outputFileUri = savedInstanceState.getParcelable(OUTPUT_FILE_URI_EXTRA);
		}
	}
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.jdroid_picture_dialog_fragment;
	}

	/**
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getDialog().setTitle(R.string.jdroid_selectPhoto);
		
		// Configure the take photo button.
		Button camera = findView(R.id.camera);
		if (DeviceUtils.hasCamera()) {
			camera.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					outputFileUri = getOutputMediaFileUri();
					
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
					startActivityForResult(intent, CAMERA_REQUEST_CODE);
				}
			});
		} else {
			camera.setVisibility(View.GONE);
		}
		
		// Configure the choose from library button.
		Button gallery = findView(R.id.gallery);
		gallery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
				imagePickerIntent.setType(IMAGE_TYPE);
				startActivityForResult(imagePickerIntent, GALLERY_REQUEST_CODE);
			}
		});
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			Uri path = null;
			switch (requestCode) {
			
			// Set the default path for the camera pictures if the picture is obtained from the camera.
				case CAMERA_REQUEST_CODE:
					path = outputFileUri;
					break;
				
				// Set the obtained path if the picture is obtained from the device's gallery.
				case GALLERY_REQUEST_CODE:
					path = data.getData();
					break;
			}
			PicturePickerListener listener = (PicturePickerListener)getTargetFragment();
			listener.onPicturePicked(path.toString());
			dismissAllowingStateLoss();
		}
	}
	
	/**
	 * @see android.support.v4.app.DialogFragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putParcelable(OUTPUT_FILE_URI_EXTRA, outputFileUri);
	}
	
	private Uri getOutputMediaFileUri() {
		
		// TODO To be safe, you should check that the SDCard is mounted using Environment.getExternalStorageState()
		// before doing this.
		
		// This location works best if you want the created images to be shared between applications and persist after
		// your app has been uninstalled.
		String appName = AppUtils.getApplicationName().trim().replace(" ", "_");
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				appName);
		
		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
			return null;
		}
		
		// Create a media file name
		String timeStamp = DateUtils.format(DateUtils.now(), "yyyyMMdd_HHmmss");
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".png");
		
		return Uri.fromFile(mediaFile);
	}
	
}
