package com.jdroid.android.barcode;

/**
 * <p>
 * Encapsulates the result of a barcode scan invoked through {@link BarcodeUtils}.
 * </p>
 */
public class BarcodeIntentResult {

	private final String contents;
	private final String formatName;

	BarcodeIntentResult(String contents, String formatName) {
		this.contents = contents;
		this.formatName = formatName;
	}

	/**
	 * @return raw content of barcode
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * @return name of format, like "QR_CODE", "UPC_A". See <code>BarcodeFormat</code> for more format names.
	 */
	public String getFormatName() {
		return formatName;
	}

}
