package com.jdroid.android.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.jdroid.android.kotlin.getRequiredIntArgument
import com.jdroid.android.kotlin.getRequiredSerializableArgument
import java.io.Serializable

class ChoiceItemDialogFragment : AbstractDialogFragment() {

    private lateinit var values: List<ChoiceItem>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        values = arguments?.getSerializable(ITEMS_EXTRA)!! as List<ChoiceItem>
        val selectedItem = getRequiredSerializableArgument<ChoiceItem>(CURRENT_ITEM_EXTRA)
        val resourceTitleId = getRequiredIntArgument(RESOURCE_TITLE_ID_EXTRA)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resourceTitleId)
        builder.setSingleChoiceItems(getAvailableItems(), values.indexOf(selectedItem)) { _, which ->
            val listener = targetFragment as ChoiceItemListener<ChoiceItem>?
            if (values[which] == selectedItem) {
                listener?.onCurrentItemSelected(selectedItem)
            } else {
                listener?.onNewItemSelected(values[which])
            }
            dismiss()
        }

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    private fun getAvailableItems(): Array<CharSequence> {
        return values.map { getString(it.getResourceTitleId()) }.toTypedArray()
    }

    override fun onDestroyView() {
        // This is for rotation change, to preserve the dialog
        if (retainInstance) {
            dialog?.setDismissMessage(null)
        }
        super.onDestroyView()
    }

    companion object {

        const val ITEMS_EXTRA = "itemsExtra"
        const val CURRENT_ITEM_EXTRA = "currentItemExtra"
        const val RESOURCE_TITLE_ID_EXTRA = "resourceTitleId"

        /**
         * @param targetFragment The fragment that invokes the dialog
         * @param values The list must implements ChoiceItem
         * @param currentItem
         * @param resourceTitleId
         */
        fun show(targetFragment: Fragment, values: List<ChoiceItem>, currentItem: ChoiceItem, resourceTitleId: Int) {
            val fragment = ChoiceItemDialogFragment()
            val bundle = Bundle()
            bundle.putSerializable(ITEMS_EXTRA, values as Serializable)
            bundle.putSerializable(CURRENT_ITEM_EXTRA, currentItem)
            bundle.putInt(RESOURCE_TITLE_ID_EXTRA, resourceTitleId)
            fragment.arguments = bundle
            fragment.setTargetFragment(targetFragment, 0)
            val fragmentManager = targetFragment.getParentFragmentManager()
            if (fragmentManager.findFragmentByTag(ChoiceItemDialogFragment::class.java.simpleName) == null) {
                fragment.show(fragmentManager, ChoiceItemDialogFragment::class.java.simpleName)
            }
        }
    }
}
