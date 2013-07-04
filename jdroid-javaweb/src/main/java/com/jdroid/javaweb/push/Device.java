package com.jdroid.javaweb.push;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.javaweb.domain.Entity;

/**
 * 
 * @author Maxi Rosson
 */
@javax.persistence.Entity
public class Device extends Entity {
	
	private String installationId;
	private String registrationId;
	
	@Enumerated(value = EnumType.STRING)
	private DeviceType deviceType;
	
	private Boolean disabled;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private Device() {
		// Do nothing, is required by hibernate
	}
	
	public Device(String installationId, DeviceType deviceType) {
		this(installationId, null, deviceType);
	}
	
	public Device(String installationId, String registrationId, DeviceType deviceType) {
		
		if (deviceType == null) {
			throw new UnexpectedException("The device type is required");
		}
		this.deviceType = deviceType;
		
		this.installationId = installationId;
		this.registrationId = registrationId;
		disabled = false;
	}
	
	public void updateRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	
	/**
	 * @return the installationId
	 */
	public String getInstallationId() {
		return installationId;
	}
	
	/**
	 * @return the registrationId
	 */
	public String getRegistrationId() {
		return registrationId;
	}
	
	/**
	 * @return the deviceType
	 */
	public DeviceType getDeviceType() {
		return deviceType;
	}
	
	/**
	 * @return the disabled
	 */
	public Boolean isDisabled() {
		return disabled;
	}
	
	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Device [installationId=" + installationId + ", registrationId=" + registrationId + ", deviceType="
				+ deviceType + ", disabled=" + disabled + "]";
	}
	
}
