package com.jdroid.javaweb.utils;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.NumberUtils;
import com.jdroid.java.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Utilities for CSV
 */
public class CSVUtils {

	private CSVUtils(){}
	
	public static interface ValueConverter<T> {
		
		public T fromString(String value);
		
		public String[] toArray(Collection<T> values);
	}
	
	public static class StringConverter implements ValueConverter<String> {
		
		private final static StringConverter INSTANCE = new StringConverter();
		
		public static StringConverter get() {
			return INSTANCE;
		}
		
		/**
		 * @see com.jdroid.javaweb.utils.CSVUtils.ValueConverter#fromString(java.lang.String)
		 */
		@Override
		public String fromString(String value) {
			return value;
		}
		
		/**
		 * @see com.jdroid.javaweb.utils.CSVUtils.ValueConverter#toArray(java.util.Collection)
		 */
		@Override
		public String[] toArray(Collection<String> values) {
			return values.toArray(new String[] {});
		}
	}
	
	public static class LongConverter implements ValueConverter<Long> {
		
		private final static LongConverter INSTANCE = new LongConverter();
		
		public static LongConverter get() {
			return INSTANCE;
		}
		
		/**
		 * @see com.jdroid.javaweb.utils.CSVUtils.ValueConverter#fromString(java.lang.String)
		 */
		@Override
		public Long fromString(String value) {
			return NumberUtils.getLong(value);
		}
		
		/**
		 * @see com.jdroid.javaweb.utils.CSVUtils.ValueConverter#toArray(java.util.Collection)
		 */
		@Override
		public String[] toArray(Collection<Long> values) {
			List<String> stringsValues = Lists.newArrayList();
			for (Long each : values) {
				stringsValues.add(each.toString());
			}
			return stringsValues.toArray(new String[] {});
		}
	}
	
	/**
	 * Reads a csv file and returns a list of T with the values
	 * 
	 * @param csv A comma separated value string
	 * @param valueConverter A {@link ValueConverter}
	 * @param <T> A {@link ValueConverter}
	 * @return A list with all the values into the file
	 */
	public static <T> List<T> fromCSV(String csv, ValueConverter<T> valueConverter) {
		return com.jdroid.java.utils.StringUtils.isNotEmpty(csv) ? CSVUtils.fromCSV(new StringReader(csv),
			valueConverter) : Lists.<T>newArrayList();
	}
	
	/**
	 * Reads a csv file and returns a list of string with the values
	 * 
	 * @param csv A comma separated value string
	 * @return A list with all the values into the file
	 */
	public static List<String> fromCSV(String csv) {
		return CSVUtils.fromCSV(csv, StringConverter.get());
	}
	
	/**
	 * Reads a csv file and returns a list of string with the values
	 * 
	 * @param csvFile The csv file
	 * @return A list with all the values into the file
	 */
	public static List<String> fromCSV(File csvFile) {
		try {
			return CSVUtils.fromCSV(new FileInputStream(csvFile));
		} catch (IOException exception) {
			throw new UnexpectedException("Error reading the file: " + csvFile, exception);
		}
	}
	
	/**
	 * Reads a csv file and returns a list of string with the values
	 * 
	 * @param inputStream The input stream with the csv values
	 * @return A list with all the values into the file
	 */
	public static List<String> fromCSV(InputStream inputStream) {
		return CSVUtils.fromCSV(new InputStreamReader(inputStream));
	}
	
	/**
	 * Reads a csv and returns a list of string with the values
	 * 
	 * @param reader The csv reader
	 * @return A list with all the values given by the reader
	 */
	public static List<String> fromCSV(Reader reader) {
		return CSVUtils.fromCSV(reader, StringConverter.get());
	}
	
	public static <T> List<T> fromCSV(Reader reader, ValueConverter<T> valueConverter) {
		try {
			List<T> result = Lists.newArrayList();
			CSVReader csvReader = new CSVReader(reader);
			try {
				for (String[] values : csvReader.readAll()) {
					for (String value : values) {
						value = value.trim();
						if (com.jdroid.java.utils.StringUtils.isNotEmpty(value)) {
							result.add(valueConverter.fromString(value));
						}
					}
				}
				return result;
			} finally {
				csvReader.close();
			}
		} catch (IOException exception) {
			throw new UnexpectedException("Error reading the csv", exception);
		}
	}
	
	public static String toCSVFromToString(Collection<?> values) {
		Collection<String> stringValues = Lists.newArrayList();
		for (Object value : values) {
			stringValues.add(value.toString());
		}
		return CSVUtils.toCSV(stringValues);
	}
	
	public static <T> String toCSV(Collection<T> values, ValueConverter<T> valueConverter, char separator) {
		if (values != null) {
			StringWriter writer = new StringWriter();
			CSVUtils.writeCSV(writer, values, valueConverter, separator);
			return writer.toString();
		} else {
			return null;
		}
	}
	
	/**
	 * @param <T>
	 * @param values A collection with the values
	 * @param valueConverter A {@link ValueConverter}
	 * @return A string with the comma separated values
	 */
	public static <T> String toCSV(Collection<T> values, ValueConverter<T> valueConverter) {
		return toCSV(values, valueConverter, CSVWriter.DEFAULT_SEPARATOR);
	}
	
	public static String toCSV(Collection<String> values) {
		return CSVUtils.toCSV(values, StringConverter.get());
	}
	
	public static String toCSV(Collection<String> values, char separator) {
		return CSVUtils.toCSV(values, StringConverter.get(), separator);
	}
	
	/**
	 * @param values A collection with the values
	 * @return csv file
	 */
	@SuppressWarnings("resource")
	public static File toCSVFile(Collection<String> values) {
		try {
			String fileName = "csv_file" + DateUtils.now().getTime();
			File file = File.createTempFile(fileName, ".csv");
			CSVUtils.writeCSV(new FileOutputStream(file), values);
			return file;
		} catch (IOException exception) {
			throw new UnexpectedException("Error creating a temporal file to write the csv values", exception);
		}
	}
	
	/**
	 * @param outputStream The output stream where the csv will be written
	 * @param values A collection with the values
	 */
	public static void writeCSV(OutputStream outputStream, Collection<String> values) {
		CSVUtils.writeCSV(new OutputStreamWriter(outputStream), values);
	}
	
	public static <T> void writeCSV(Writer writer, Collection<T> values, ValueConverter<T> valueConverter) {
		writeCSV(writer, values, valueConverter, CSVWriter.DEFAULT_SEPARATOR);
	}
	
	/**
	 * @param <T>
	 * @param writer The writer where the csv will be written
	 * @param values A collection with the values
	 * @param valueConverter A {@link ValueConverter}
	 * @param separator the delimiter to use for separating entries
	 */
	public static <T> void writeCSV(Writer writer, Collection<T> values, ValueConverter<T> valueConverter,
			char separator) {
		try {
			CSVWriter csvWriter = new CSVWriter(writer, separator, CSVWriter.NO_QUOTE_CHARACTER, StringUtils.EMPTY);
			csvWriter.writeNext(valueConverter.toArray(values));
			csvWriter.close();
		} catch (IOException exception) {
			throw new UnexpectedException("Error writing the values", exception);
		}
	}
	
	public static void writeCSV(Writer writer, Collection<String> values) {
		CSVUtils.writeCSV(writer, values, StringConverter.get());
	}
	
	/**
	 * @param writer The writer where the csv will be written
	 * @param values A collection with the values
	 */
	public static void writeCSVRow(Writer writer, Collection<String> values) {
		try {
			// all the values will be quoted
			CSVWriter csvWriter = new CSVWriter(writer);
			csvWriter.writeNext(values.toArray(new String[] {}));
			csvWriter.close();
		} catch (IOException exception) {
			throw new UnexpectedException("Error writing the values", exception);
		}
	}
	
	/**
	 * @param outputStream The output stream where the csv will be written
	 * @param values A List of String[], with each String[] representing a line of the file.
	 */
	public static void writeMultipleColumnCSV(OutputStream outputStream, List<String[]> values) {
		CSVUtils.writeMultipleColumnCSV(new OutputStreamWriter(outputStream), values);
	}
	
	/**
	 * @param values A List of String[], with each String[] representing a line of the file.
	 * @return csv file
	 */
	@SuppressWarnings("resource")
	public static File toMultipleColumnCSVFile(List<String[]> values) {
		try {
			String fileName = "csv_file" + DateUtils.now().getTime();
			File file = File.createTempFile(fileName, ".csv");
			CSVUtils.writeMultipleColumnCSV(new FileOutputStream(file), values);
			return file;
		} catch (IOException exception) {
			throw new UnexpectedException("Error creating a temporal file to write the csv values", exception);
		}
	}
	
	/**
	 * @param writer The writer where the csv will be written
	 * @param values A List of String[], with each String[] representing a line of the file.
	 */
	public static void writeMultipleColumnCSV(Writer writer, List<String[]> values) {
		try {
			CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
			csvWriter.writeAll(values);
			csvWriter.close();
		} catch (IOException exception) {
			throw new UnexpectedException("Error writing the values", exception);
		}
	}
}
