package com.jdroid.javaweb.push;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.jdroid.javaweb.hibernate.AbstractHibernateRepository;

public class DeviceHibernateRepository extends AbstractHibernateRepository<Device> implements DeviceRepository {
	
	protected DeviceHibernateRepository() {
		super(Device.class);
	}
	
	/**
	 * @see com.jdroid.javaweb.push.DeviceRepository#find(java.lang.String, com.jdroid.javaweb.push.DeviceType)
	 */
	@Override
	public Device find(String installationId, DeviceType deviceType) {
		DetachedCriteria criteria = createDetachedCriteria();
		criteria.add(Restrictions.and(Restrictions.eq("installationId", installationId),
			Restrictions.eq("deviceType", deviceType)));
		return this.findUnique(criteria);
	}
}
