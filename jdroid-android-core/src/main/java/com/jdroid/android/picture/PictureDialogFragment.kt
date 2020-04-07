package com.jdroid.android.picture

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.jdroid.android.dialog.AbstractDialogFragment
import com.jdroid.android.utils.AppUtils
import com.jdroid.android.utils.DeviceUtils
import com.jdroid.java.date.DateUtils
import java.io.File

class PictureDialogFragment : AbstractDialogFragment() {

    private var outputFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        outputFileUri = savedInstanceState?.getParcelable(OUTPUT_FILE_URI_EXTRA)
    }

    override fun getContentFragmentLayout(): Int? {
        return com.jdroid.android.R.layout.jdroid_picture_dialog_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setTitle(com.jdroid.android.R.string.jdroid_selectPhoto)

        // Configure the take photo button.
        val camera = findView<Button>(com.jdroid.android.R.id.camera)
        if (DeviceUtils.hasCamera()) {
            camera.setOnClickListener {
                outputFileUri = getOutputMediaFileUri()

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }
        } else {
            camera.visibility = View.GONE
        }

        // Configure the choose from library button.
        val gallery = findView<Button>(com.jdroid.android.R.id.gallery)
        gallery.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_PICK)
            imagePickerIntent.type = IMAGE_TYPE
            startActivityForResult(imagePickerIntent, GALLERY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            var path: Uri? = null
            when (requestCode) {

                // Set the default path for the camera pictures if the picture is obtained from the camera.
                CAMERA_REQUEST_CODE -> path = outputFileUri

                // Set the obtained path if the picture is obtained from the device's gallery.
                GALLERY_REQUEST_CODE -> path = data!!.data
            }
            val listener = targetFragment as PicturePickerListener?
            listener?.onPicturePicked(path!!.toString())
            dismissAllowingStateLoss()
        }
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putParcelable(OUTPUT_FILE_URI_EXTRA, outputFileUri)
    }

    private fun getOutputMediaFileUri(): Uri? {

        // TODO To be safe, you should check that the SDCard is mounted using Environment.getExternalStorageState()
        // before doing this.

        // This location works best if you want the created images to be shared between applications and persist after
        // your app has been uninstalled.
        val appName = AppUtils.getApplicationName().trim().replace(" ", "_")
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            appName
        )

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null
        }

        // Create a media file name
        val timeStamp = DateUtils.format(DateUtils.now(), "yyyyMMdd_HHmmss")
        val mediaFile = File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".png")

        return Uri.fromFile(mediaFile)
    }

    companion object {

        private const val OUTPUT_FILE_URI_EXTRA = "outputFileUriExtra"
        private const val IMAGE_TYPE = "image/*"
        private const val CAMERA_REQUEST_CODE = 1
        private const val GALLERY_REQUEST_CODE = 2

        fun display(): Boolean {
            return DeviceUtils.hasCamera()
        }

        fun show(targetFragment: Fragment) {
            val fm = targetFragment.requireActivity().supportFragmentManager
            val pictureDialogFragment = PictureDialogFragment()
            pictureDialogFragment.setTargetFragment(targetFragment, 1)
            pictureDialogFragment.show(fm, PictureDialogFragment::class.java.simpleName)
        }
    }
}
