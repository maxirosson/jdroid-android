package com.jdroid.android.sample.database.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class SampleEntity {

	@PrimaryKey
	@NonNull
	private String id;

	@ColumnInfo(name = "field")
	private String field;

	@Ignore
	private String ignoredField;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getIgnoredField() {
		return ignoredField;
	}

	public void setIgnoredField(String ignoredField) {
		this.ignoredField = ignoredField;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("SampleEntity{");
		sb.append("id='").append(id).append('\'');
		sb.append(", field='").append(field).append('\'');
		sb.append(", ignoredField='").append(ignoredField).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
