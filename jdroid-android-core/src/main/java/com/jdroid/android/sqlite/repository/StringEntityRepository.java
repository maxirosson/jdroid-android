package com.jdroid.android.sqlite.repository;

import android.content.ContentValues;
import android.database.Cursor;

import com.jdroid.android.sqlite.Column;
import com.jdroid.android.sqlite.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic repository to store {@link String} objects.
 * 
 */
public abstract class StringEntityRepository extends SQLiteRepository<StringEntity> {
	
	public StringEntityRepository(SQLiteHelper dbHelper) {
		super(dbHelper);
	}
	
	/**
	 * @see SQLiteRepository#createObjectFromCursor(android.database.Cursor)
	 */
	@Override
	protected StringEntity createObjectFromCursor(Cursor cursor) {
		StringEntity entity = new StringEntity();
		entity.setId((String)getColumn(Column.ID).readValue(cursor));
		if (cursor.getColumnIndex(Column.PARENT_ID) >= 0) {
			entity.setParentId((String)getColumn(Column.PARENT_ID).readValue(cursor));
		}
		entity.setValue((String)getColumn(Column.VALUE).readValue(cursor));
		return entity;
	}
	
	@Override
	protected ContentValues createContentValuesFromObject(StringEntity item) {
		ContentValues values = new ContentValues();
		getColumn(Column.ID).addValue(values, item.getId());
		if (item.getParentId() != null) {
			getColumn(Column.PARENT_ID).addValue(values, item.getParentId());
		}
		getColumn(Column.VALUE).addValue(values, item.getValue());
		return values;
	}
	
	/**
	 * Find for the column associated with the given name.
	 * 
	 * @param name column name.
	 * @return return the column if exits, otherwise returns null.
	 */
	protected Column getColumn(String name) {
		for (Column column : getColumns()) {
			if (column.getColumnName().equals(name)) {
				return column;
			}
		}
		return null;
	}
	
	/**
	 * This method allows to replace all string children of a given parent, it will remove any children which are not in
	 * the list, add the new ones and update which are in the list.
	 * 
	 * @param strings string children list to replace.
	 * @param parentId id of parent entity.
	 */
	public void replaceStringChildren(List<String> strings, String parentId) {
		ArrayList<StringEntity> entities = new ArrayList<>();
		for (String string : strings) {
			if (string != null) {
				StringEntity entity = new StringEntity();
				entity.setParentId(parentId);
				entity.setValue(string);
				entities.add(entity);
			}
		}
		replaceChildren(entities, parentId);
	}
	
	/**
	 * This method allows to replace all string children, it will remove any children which are not in the list, add the
	 * new ones and update which are in the list.
	 * 
	 * @param strings string children list to replace.
	 */
	public void replaceStringChildren(List<String> strings) {
		ArrayList<StringEntity> entities = new ArrayList<>();
		for (String string : strings) {
			StringEntity entity = new StringEntity();
			entity.setValue(string);
			entities.add(entity);
		}
		replaceAll(entities);
	}
	
	/**
	 * This method returns the list of strings associated with given parent id.
	 * 
	 * @param parentId of parent entity.
	 * @return list of strings
	 */
	public List<String> getStringChildren(Long parentId) {
		ArrayList<String> strings = new ArrayList<>();
		List<StringEntity> entities = getByField(Column.PARENT_ID, parentId);
		for (StringEntity entity : entities) {
			strings.add(entity.getValue());
		}
		return strings;
	}
	
	/**
	 * Returns all strings.
	 * 
	 * @return list of strings
	 */
	public List<String> getAllString() {
		ArrayList<String> strings = new ArrayList<>();
		List<StringEntity> entities = getAll();
		for (StringEntity entity : entities) {
			strings.add(entity.getValue());
		}
		return strings;
	}
	
}
