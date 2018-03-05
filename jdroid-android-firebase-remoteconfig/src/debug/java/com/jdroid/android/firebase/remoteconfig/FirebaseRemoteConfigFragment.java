package com.jdroid.android.firebase.remoteconfig;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.StringUtils;

import java.util.Date;
import java.util.List;

public class FirebaseRemoteConfigFragment extends AbstractRecyclerFragment {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		List<Object> items = Lists.newArrayList();
		items.add("");
		items.addAll(FirebaseRemoteConfigHelper.getRemoteConfigParameters());
		List<RecyclerViewType> recyclerViewTypes = Lists.<RecyclerViewType>newArrayList(new HeaderRecyclerViewType(), new RemoteConfigParameterRecyclerViewType());
		setAdapter(new RecyclerViewAdapter(recyclerViewTypes, items));
	}

	public class HeaderRecyclerViewType extends RecyclerViewType<String, HeaderViewHolder> {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.jdroid_firebase_remote_config_header;
		}

		@Override
		protected Class<String> getItemClass() {
			return String.class;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			HeaderViewHolder viewHolder = new HeaderViewHolder(view);
			viewHolder.fetchTimeMillis = findView(view, R.id.fetchTimeMillis);
			viewHolder.lastFetchStatus = findView(view, R.id.lastFetchStatus);
			viewHolder.fetch = findView(view, R.id.fetch);
			viewHolder.save = findView(view, R.id.save);
			viewHolder.mocksEnabled = findView(view, R.id.mocksEnabled);
			return viewHolder;
		}

		@Override
		public void fillHolderFromItem(String item, HeaderViewHolder holder) {
			holder.fetch.setEnabled(!FirebaseRemoteConfigHelper.isMocksEnabled());
			holder.fetch.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					FirebaseRemoteConfigHelper.fetchNow(new OnSuccessListener<Void>() {
						@Override
						public void onSuccess(Void aVoid) {
							executeOnUIThread(new Runnable() {
								@Override
								public void run() {
									getAdapter().notifyDataSetChanged();
								}
							});
						}
					});
				}
			});

			holder.mocksEnabled.setChecked(FirebaseRemoteConfigHelper.isMocksEnabled());
			holder.mocksEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					FirebaseRemoteConfigHelper.setMocksEnabled(isChecked);
					getAdapter().notifyDataSetChanged();
				}
			});

			FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfigHelper.getFirebaseRemoteConfig();
			if (firebaseRemoteConfig != null) {
				String fetchDate = DateUtils.formatDateTime(new Date(firebaseRemoteConfig.getInfo().getFetchTimeMillis()));
				holder.fetchTimeMillis.setText(getString(R.string.jdroid_firebaseRemoteConfigFetchDate, fetchDate));
				int status = firebaseRemoteConfig.getInfo().getLastFetchStatus();
				String statusValue = "Unknown";
				if (status == -1) {
					statusValue = "Success";
				} else if (status == 0) {
					statusValue = "No fetch yet";
				} else if (status == 1) {
					statusValue = "Failure";
				} else if (status == 2) {
					statusValue = "Throttled";
				}
				holder.lastFetchStatus.setText(getString(R.string.jdroid_firebaseRemoteConfigLastFetchStatus, statusValue));
			}
		}

		@Override
		public AbstractRecyclerFragment getAbstractRecyclerFragment() {
			return FirebaseRemoteConfigFragment.this;
		}
	}

	public static class HeaderViewHolder extends RecyclerView.ViewHolder {

		public TextView fetchTimeMillis;
		public TextView lastFetchStatus;
		public Button fetch;
		public Button save;
		public CheckBox mocksEnabled;

		public HeaderViewHolder(View itemView) {
			super(itemView);
		}
	}

	public class RemoteConfigParameterRecyclerViewType extends RecyclerViewType<RemoteConfigParameter, RemoteConfigParameterItemHolder> {

		@Override
		protected Class<RemoteConfigParameter> getItemClass() {
			return RemoteConfigParameter.class;
		}

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.jdroid_firebase_remote_config_param;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			RemoteConfigParameterItemHolder holder = new RemoteConfigParameterItemHolder(view);
			holder.key = findView(view, R.id.key);
			holder.specs = findView(view, R.id.specs);
			holder.source = findView(view, R.id.source);
			holder.value = findView(view, R.id.value);
			holder.save = findView(view, R.id.save);
			return holder;
		}

		@Override
		public void fillHolderFromItem(final RemoteConfigParameter item, final RemoteConfigParameterItemHolder holder) {
			holder.key.setText(item.getKey());
			holder.specs.setText(getString(R.string.jdroid_firebaseRemoteConfigSpec, item.isUserProperty().toString(), item.getDefaultValue()));
			holder.source.setText(getString(R.string.jdroid_firebaseRemoteConfigSource, FirebaseRemoteConfigHelper.getSourceName(item)));
			holder.value.setText(FirebaseRemoteConfigHelper.getString(item));
			holder.value.setEnabled(FirebaseRemoteConfigHelper.isMocksEnabled());
			holder.source.setVisibility(FirebaseRemoteConfigHelper.isMocksEnabled() ? View.GONE : View.VISIBLE);
			holder.save.setVisibility(FirebaseRemoteConfigHelper.isMocksEnabled() ? View.VISIBLE : View.GONE);
			holder.save.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					FirebaseRemoteConfigHelper.setParameterMock(item, StringUtils.getNotEmptyString(holder.value.getText().toString()));
				}
			});
		}

		@Override
		public Boolean matchViewType(Object item) {
			return item instanceof RemoteConfigParameter;
		}

		@Override
		public AbstractRecyclerFragment getAbstractRecyclerFragment() {
			return FirebaseRemoteConfigFragment.this;
		}
	}

	public static class RemoteConfigParameterItemHolder extends RecyclerView.ViewHolder {

		protected TextView key;
		protected TextView specs;
		protected TextView source;
		protected EditText value;
		protected Button save;

		public RemoteConfigParameterItemHolder(View itemView) {
			super(itemView);
		}
	}
}
