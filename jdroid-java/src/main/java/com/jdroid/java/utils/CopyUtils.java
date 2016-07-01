package com.jdroid.java.utils;

import com.jdroid.java.exception.UnexpectedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CopyUtils {

	private CopyUtils(){}

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T cloneSerializable(T object) {
		T cloneObject;

		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(bout);
			oout.writeObject(object);
			byte[] bytes = bout.toByteArray();
			oout.close();

			ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
			ObjectInputStream oin = new ObjectInputStream(bin);
			cloneObject = (T)oin.readObject();
			oin.close();
		} catch (IOException | ClassNotFoundException e) {
			throw new UnexpectedException("Failed to clone " + object.getClass().getSimpleName()
					+ " using serialization", e);
		}

		return cloneObject;
	}
}
