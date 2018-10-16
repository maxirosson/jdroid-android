package com.jdroid.android.sample.database.room;

import java.util.Date;
import java.util.List;

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

	private Date date;

	private List<String> stringList;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<String> getStringList() {
		return stringList;
	}

	public void setStringList(List<String> stringList) {
		this.stringList = stringList;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("SampleEntity{");
		sb.append("id='").append(id).append('\'');
		sb.append(", field='").append(field).append('\'');
		sb.append(", date=").append(date);
		sb.append(", stringList=").append(stringList);
		sb.append(", ignoredField='").append(ignoredField).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
