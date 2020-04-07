package com.jdroid.android.google.inappbilling.client

object TestProductType {

    // When you make an in-app billing request with this product ID, the Google Play app responds as though you
    // successfully purchased an item. The response includes a JSON string, which contains fake purchase information
    // (for example, a fake order ID). In some cases, the JSON string is signed and the response includes the signature
    // so you can test your signature verification implementation using these responses.
    val PURCHASED = "android.test.purchased"

    // When you make an in-app billing request with this product ID the Google Play app responds as though the purchase
    // was canceled. This can occur when an error is encountered in the order process, such as an invalid credit card,
    // or when you cancel a user's order before it is charged.
    val CANCELED = "android.test.canceled"

    // When you make an in-app billing request with this product ID, the Google Play app responds as though the item
    // being purchased was not listed in your application's product list.
    val UNAVAILABLE = "android.test.item_unavailable"
}
