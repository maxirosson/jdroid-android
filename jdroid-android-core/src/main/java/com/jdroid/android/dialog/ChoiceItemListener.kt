package com.jdroid.android.dialog

interface ChoiceItemListener<T : ChoiceItem> {

    /**
     * This method is called when new item is selected
     *
     * @param selectedItem
     */
    fun onNewItemSelected(selectedItem: T)

    /**
     * This method is called when the selected item is the item that was previously selected
     *
     * @param selectedItem
     */
    fun onCurrentItemSelected(selectedItem: T)
}
