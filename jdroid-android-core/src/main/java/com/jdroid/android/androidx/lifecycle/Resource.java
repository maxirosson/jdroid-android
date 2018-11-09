package com.jdroid.android.androidx.lifecycle;

import com.jdroid.java.exception.AbstractException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*
 * A generic class that contains data and status about loading this data.
 */
public class Resource<T> {

	@NonNull
	private Status status;

	@Nullable
	private T data;

	@Nullable
	private AbstractException exception;

	private Resource(@NonNull Status status, @Nullable T data, @Nullable AbstractException exception) {
		this.status = status;
		this.data = data;
		this.exception = exception;
	}

	@NonNull
	public Status getStatus() {
		return status;
	}

	@Nullable
	public T getData() {
		return data;
	}

	@Nullable
	public AbstractException getException() {
		return exception;
	}

	public static <T> Resource<T> success(@NonNull T data) {
		return new Resource<>(Status.SUCCESS, data, null);
	}

	public static <T> Resource<T> error(AbstractException exception, @Nullable T data) {
		return new Resource<>(Status.ERROR, data, exception);
	}

	public static <T> Resource<T> loading(@Nullable T data) {
		return new Resource<>(Status.LOADING, data, null);
	}

	public enum Status {
		SUCCESS,
		ERROR,
		LOADING
	}
}