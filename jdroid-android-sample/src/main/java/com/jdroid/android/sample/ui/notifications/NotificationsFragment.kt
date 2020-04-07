package com.jdroid.android.sample.ui.notifications

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.glide.GlideBitmapLoader
import com.jdroid.android.notification.NotificationBuilder
import com.jdroid.android.notification.NotificationUtils
import com.jdroid.android.sample.R
import com.jdroid.android.sample.application.AndroidNotificationChannelType
import com.jdroid.java.date.DateUtils
import com.jdroid.java.utils.IdGenerator
import com.jdroid.java.utils.StringUtils

class NotificationsFragment : AbstractFragment() {

    private lateinit var notificationName: EditText
    private lateinit var notificationChannel: EditText
    private lateinit var contentTitle: EditText
    private lateinit var contentText: EditText
    private lateinit var largeIconUrlEditText: EditText
    private lateinit var largeIconDrawable: CheckBox
    private lateinit var urlEditText: EditText

    override fun getContentFragmentLayout(): Int? {
        return R.layout.notifications_fragment
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationName = findView(R.id.notificationName)
        notificationName.setText("myNotification")

        notificationChannel = findView(R.id.notificationChannel)
        notificationChannel.setText(AndroidNotificationChannelType.DEFAULT_IMPORTANCE.getChannelId())

        contentTitle = findView(R.id.contentTitle)
        contentTitle.setText(R.string.contentTitleSample)

        contentText = findView(R.id.contentText)
        contentText.setText(R.string.contextTextSample)

        largeIconUrlEditText = findView(R.id.largeIconUrl)
        largeIconUrlEditText.setText("https://jdroidtools.com/images/gradle.png")

        urlEditText = findView(R.id.url)
        urlEditText.setText("https://jdroidtools.com/uri")

        largeIconDrawable = findView(R.id.largeIconDrawable)
        largeIconDrawable.isChecked = false

        findView<View>(R.id.sendNotification).setOnClickListener {
            AppExecutors.networkIOExecutor.execute {
                val builder = NotificationBuilder(notificationName.text.toString(), notificationChannel.text.toString())
                builder.setSmallIcon(AbstractApplication.get().notificationIconResId)

                if (largeIconDrawable.isChecked) {
                    builder.setLargeIcon(R.drawable.marker)
                } else {
                    val largeIconUrl = largeIconUrlEditText.text.toString()
                    if (StringUtils.isNotEmpty(largeIconUrl)) {
                        builder.setLargeIcon(GlideBitmapLoader(largeIconUrl))
                    }
                }

                builder.setContentTitle(contentTitle.text.toString())
                builder.setContentText(contentText.text.toString())

                val url = urlEditText.text.toString()
                if (StringUtils.isNotBlank(url)) {
                    builder.setSingleTopUrl(url)
                } else {
                    val intent = Intent(activity, AbstractApplication.get().homeActivityClass)
                    builder.setContentIntent(intent)
                }

                builder.setWhen(DateUtils.nowMillis())
                builder.setBlueLight()
                builder.setDefaultSound()

                NotificationUtils.sendNotification(IdGenerator.getIntId(), builder)
            }
        }

        findView<View>(R.id.cancelNotifications).setOnClickListener { NotificationUtils.cancelAllNotifications() }
    }
}
