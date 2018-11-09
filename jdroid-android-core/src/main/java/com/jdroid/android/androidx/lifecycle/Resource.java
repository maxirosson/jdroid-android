package com.jdroid.android.androidx.lifecycle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*
 * A generic class that contains data and status about loading this data.
 */
public class Resource<T> {

	public static <T> Resource<T> success(@NonNull T data) {
		return new Resource<>(Status.SUCCESS, data, null);
	}

	public static <T> Resource<T> error(Exception exception, @Nullable T data) {
		return new Resource<>(Status.ERROR, data, exception);
	}

	public static <T> Resource<T> loading(@Nullable T data) {
		return new Resource<>(Status.LOADING, data, null);
	}

	@NonNull
	private Status status;

	@Nullable
	private T data;

	@Nullable
	private Exception exception;

	private Resource(@NonNull Status status, @Nullable T data, @Nullable Exception exception) {
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
	public Exception getException() {
		return exception;
	}

	public enum Status {
		SUCCESS,
		ERROR,
		LOADING
	}
}