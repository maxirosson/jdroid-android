package com.jdroid.android.about

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jdroid.android.recycler.AbstractRecyclerFragment
import com.jdroid.android.recycler.RecyclerViewAdapter
import com.jdroid.android.recycler.RecyclerViewContainer
import com.jdroid.android.recycler.RecyclerViewType
import com.jdroid.java.utils.ReflectionUtils

open class LibrariesFragment : AbstractRecyclerFragment() {

    private val libraries = mutableListOf<Library>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        libraries.add(Library("jdroid", "Jdroid Tools", "Maxi Rosson", "https://jdroidtools.com"))
        libraries.add(Library("androidJetpack", "Android Jetpack", "Google", "https://developer.android.com/jetpack"))
        libraries.add(Library("googlePlayServices", "Google Play Services", "Google", "https://developers.google.com/android/guides/overview"))
        libraries.add(Library("firebase", "Firebase", "Google", "https://firebase.google.com"))
        libraries.add(Library("yearclass", "Device Year Class", "Facebook", "https://github.com/facebook/device-year-class"))
        if (ReflectionUtils.getSafeClass("com.jdroid.android.twitter.TwitterAppLifecycleCallback") != null) {
            libraries.add(Library("tweetUi", "Tweet UI", "Twitter", "https://dev.twitter.com/twitterkit/overview"))
        }
        if (ReflectionUtils.getSafeClass("com.jdroid.android.glide.GlideHelper") != null) {
            val library = Library(
                "glide", "Glide",
                "Bump Technologies", "https://bumptech.github.io/glide/"
            )
            libraries.add(library)
        }
        if (ReflectionUtils.getSafeClass("com.jdroid.java.http.okhttp.OkHttpServiceFactory") != null) {
            val library = Library(
                "okHttp", "OkHttp",
                "Square", "http://square.github.io/okhttp/"
            )
            libraries.add(library)
        }
        libraries.addAll(getCustomLibraries())
        libraries.sortWith(Comparator { lib1, lib2 -> lib1.name.compareTo(lib2.name) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter(RecyclerViewAdapter(LibraryRecyclerViewType(), libraries))
    }

    protected open fun getCustomLibraries(): List<Library> {
        return listOf()
    }

    inner class LibraryRecyclerViewType : RecyclerViewType<Library, LibrariesHolder>() {

        override fun getLayoutResourceId(): Int {
            return R.layout.jdroid_library_item
        }

        override fun getItemClass(): Class<Library> {
            return Library::class.java
        }

        override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
            val holder = LibrariesHolder(view)
            holder.name = findView(view, R.id.name)
            holder.author = findView(view, R.id.author)
            return holder
        }

        override fun fillHolderFromItem(item: Library, holder: LibrariesHolder) {
            holder.name.text = item.name
            holder.author.text = item.author
        }

        public override fun onItemSelected(item: Library, view: View) {
            item.onSelected(activity)
            AboutAppModule.get().getModuleAnalyticsSender().trackAboutLibraryOpen(item.libraryKey)
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@LibrariesFragment
        }
    }

    class LibrariesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var name: TextView
        lateinit var author: TextView
    }
}
