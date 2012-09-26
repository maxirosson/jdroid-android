package com.jdroid.android.activity;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.dialog.AbstractDialogFragment;
import com.jdroid.android.domain.UriFileContent;
import com.jdroid.android.utils.AndroidUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class PictureDialogFragment extends AbstractDialogFragment {
	
	private static final String IMAGE_TYPE = "image/*";
	
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int GALLERY_REQUEST_CODE = 2;
	
	private Uri outputFileUri;
	
	private Button camera;
	private Button gallery;
	
	public static Boolean display() {
		return AndroidUtils.hasCamera() || AndroidUtils.hasGallery();
	}
	
	public static void show(Fragment targetFragment) {
		FragmentManager fm = targetFragment.getActivity().getSupportFragmentManager();
		PictureDialogFragment pictureDialogFragment = new PictureDialogFragment();
		pictureDialogFragment.setTargetFragment(targetFragment, 1);
		pictureDialogFragment.show(fm, PictureDialogFragment.class.getSimpleName());
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.picture_dialog_fragment, container, false);
		
		camera = (Button)view.findViewById(R.id.camera);
		gallery = (Button)view.findViewById(R.id.gallery);
		
		getDialog().setTitle(R.string.selectPhoto);
		return view;
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Google TV is not displaying the title of the dialog.
		if (AndroidUtils.isGoogleTV()) {
			setStyle(STYLE_NO_TITLE, 0);
		}
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		// Configure the take photo button.
		if (AndroidUtils.hasCamera()) {
			camera.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					File file = new File(AbstractApplication.get().getImagesCacheDirectory(),
							System.currentTimeMillis() + ".png");
					outputFileUri = Uri.fromFile(file);
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
					startActivityForResult(intent, CAMERA_REQUEST_CODE);
				}
			});
		} else {
			camera.setVisibility(View.GONE);
		}
		
		// Configure the choose from library button.
		if (AndroidUtils.hasGallery()) {
			gallery.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
					imagePickerIntent.setType(IMAGE_TYPE);
					startActivityForResult(imagePickerIntent, GALLERY_REQUEST_CODE);
				}
			});
		} else {
			gallery.setVisibility(View.GONE);
		}
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
			
			// Set the default path for the camera pictures if the picture is
			// obtained from the camera.
				case CAMERA_REQUEST_CODE:
					path = outputFileUri;
					break;
				
				// Set the obtained path if the picture is obtained from the device's gallery.
				case GALLERY_REQUEST_CODE:
					path = data.getData();
					break;
			}
			PicturePickerListener listener = (PicturePickerListener)getTargetFragment();
			listener.onPicturePicked(new UriFileContent(path));
			dismissAllowingStateLoss();
		}
	}
	
}
