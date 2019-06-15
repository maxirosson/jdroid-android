package com.jdroid.android.about;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewContainer;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.ReflectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LibrariesFragment extends AbstractRecyclerFragment {

	private List<Library> libraries = Lists.newArrayList();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		libraries.add(new Library("jdroid", "Jdroid Tools", "Maxi Rosson", "https://jdroidtools.com"));
		libraries.add(new Library("androidJetpack", "Android Jetpack", "Google", "https://developer.android.com/jetpack"));
		libraries.add(new Library("googlePlayServices", "Google Play Services", "Google", "https://developers.google.com/android/guides/overview"));
		libraries.add(new Library("firebase", "Firebase", "Google", "https://firebase.google.com"));
		libraries.add(new Library("leakCanary", "Leak Canary", "Square", "https://github.com/square/leakcanary"));
		libraries.add(new Library("yearclass", "Device Year Class", "Facebook", "https://github.com/facebook/device-year-class"));
		if (ReflectionUtils.getSafeClass("com.jdroid.android.twitter.TwitterAppLifecycleCallback") != null) {
			libraries.add(new Library("tweetUi", "Tweet UI", "Twitter", "https://dev.twitter.com/twitterkit/overview"));
		}
		if (ReflectionUtils.getSafeClass("com.jdroid.android.glide.GlideHelper") != null) {
			Library library = new Library("glide", "Glide",
				"Bump Technologies", "https://bumptech.github.io/glide/");
			libraries.add(library);
		}
		if (ReflectionUtils.getSafeClass("com.jdroid.java.http.okhttp.OkHttpServiceFactory") != null) {
			Library library = new Library("okHttp", "OkHttp",
				"Square", "http://square.github.io/okhttp/");
			libraries.add(library);
		}
		libraries.addAll(getCustomLibraries());

		Collections.sort(libraries, new Comparator<Library>() {
			@Override
			public int compare(Library lib1, Library lib2) {
				return lib1.getName().compareTo(lib2.getName());
			}
		});
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setAdapter(new RecyclerViewAdapter(new LibraryRecyclerViewType(), libraries));
	}

	protected List<Library> getCustomLibraries() {
		return Lists.newArrayList();
	}

	public class LibraryRecyclerViewType extends RecyclerViewType<Library, LibrariesHolder> {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.jdroid_library_item;
		}

		@Override
		protected Class<Library> getItemClass() {
			return Library.class;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			LibrariesHolder holder = new LibrariesHolder(view);
			holder.name = findView(view, R.id.name);
			holder.author = findView(view, R.id.author);
			return holder;
		}

		@Override
		public void fillHolderFromItem(Library item, LibrariesHolder holder) {
			holder.name.setText(item.getName());
			holder.author.setText(item.getAuthor());
		}

		@Override
		public void onItemSelected(Library item, View view) {
			item.onSelected(getActivity());
			AboutAppModule.Companion.get().getModuleAnalyticsSender().trackAboutLibraryOpen(item.getLibraryKey());
		}

		@NonNull
		@Override
		public RecyclerViewContainer getRecyclerViewContainer() {
			return LibrariesFragment.this;
		}
	}

	public static class LibrariesHolder extends RecyclerView.ViewHolder {

		protected TextView name;
		protected TextView author;

		public LibrariesHolder(View itemView) {
			super(itemView);
		}
	}

}
