package com.jdroid.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import com.jdroid.android.R;

/**
 * Layout that shows a Switch button or falls back to a CheckBox for API level 13 or below. <br/>
 * 
 * To use the jdroid:label attribute from an xml file, you need to add the following xmlns:
 * xmlns:jdroid="http://schemas.android.com/apk/res/${Your app's package as declared in your manifest file}".
 * 
 * @author Estefanía Caravatti
 */
public class SwitchButton extends FrameLayout {
	
	private CompoundButton switchButton;
	
	/**
	 * @param context
	 * @param attrs
	 */
	public SwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater.from(context).inflate(R.layout.switch_button, this, true);
		switchButton = (CompoundButton)this.findViewById(R.id.switch_button);
		
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.switchButton, 0, 0);
		
		String label = typedArray.getString(R.styleable.switchButton_label);
		if (label != null) {
			switchButton.setText(label);
		}
		
		typedArray.recycle();
	}
	
	public void setChecked(Boolean checked) {
		switchButton.setChecked(checked);
		
	}
	
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		switchButton.setOnCheckedChangeListener(listener);
	}
	
}
