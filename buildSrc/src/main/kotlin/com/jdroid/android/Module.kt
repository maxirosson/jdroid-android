package com.jdroid.android

enum class Module(val moduleName: String) {
    DYNAMIC_FEATURE_SAMPLE("features:dynamic_feature_sample"),
    JDROID_ANDROID_CORE("jdroid-android-core"),
    JDROID_ANDROID_ABOUT("jdroid-android-about"),
    JDROID_ANDROID_FIREBASE_ADMOB("jdroid-android-firebase-admob"),
    JDROID_ANDROID_FIREBASE_CRASHLYTICS("jdroid-android-firebase-crashlytics"),
    JDROID_ANDROID_FIREBASE_DATABASE("jdroid-android-firebase-database"),
    JDROID_ANDROID_FIREBASE_FCM("jdroid-android-firebase-fcm"),
    JDROID_ANDROID_FIREBASE_REMOTECONFIG("jdroid-android-firebase-remoteconfig"),
    JDROID_ANDROID_GLIDE("jdroid-android-glide"),
    JDROID_ANDROID_GOOGLE_INAPPBILLING("jdroid-android-google-inappbilling"),
    JDROID_ANDROID_GOOGLE_PLAY_GAMES("jdroid-android-google-play-games"),
    JDROID_ANDROID_GOOGLE_SIGNIN("jdroid-android-google-signin"),
    JDROID_ANDROID_GOOGLE_MAPS("jdroid-android-google-maps"),
    JDROID_ANDROID_FACEBOOK_LOGIN("jdroid-android-facebook-login"),
    JDROID_ANDROID_ROOM("jdroid-android-room"),
    JDROID_ANDROID_SAMPLE("jdroid-android-sample"),
    JDROID_ANDROID_TWITTER("jdroid-android-twitter");

    val gradlePath = ":$moduleName"
}
