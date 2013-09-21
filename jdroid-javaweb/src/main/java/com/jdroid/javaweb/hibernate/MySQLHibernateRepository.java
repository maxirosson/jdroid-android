package com.jdroid.javaweb.hibernate;

import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.jdroid.java.repository.RandomRepository;
import com.jdroid.javaweb.domain.Entity;

/**
 * Hibernate Repository with additional implementation that works on MySQL engine only.
 * 
 * @param <T> The {@link Entity}
 * 
 * @author Estefanía Caravatti
 */
public abstract class MySQLHibernateRepository<T extends Entity> extends AbstractHibernateRepository<T> implements
		RandomRepository<T> {
	
	/**
	 * @param entityClass
	 */
	public MySQLHibernateRepository(Class<T> entityClass) {
		super(entityClass);
	}
	
	/**
	 * @see com.jdroid.java.repository.RandomRepository#getRandomList(java.lang.Integer)
	 */
	@Override
	public List<T> getRandomList(Integer maxResults) {
		return findRandomList(createDetachedCriteria(), maxResults);
	}
	
	/**
	 * Find random results that fulfill the given criteria.
	 * 
	 * @param criteria The {@link DetachedCriteria} to fulfill.
	 * @param maxResults Maximum quantity of results to find.
	 * @return The random list.
	 */
	protected List<T> findRandomList(DetachedCriteria criteria, Integer maxResults) {
		criteria.add(getRandomRestriction());
		return find(criteria, maxResults);
	}
	
	/**
	 * @see com.jdroid.java.repository.RandomRepository#getRandom()
	 */
	@Override
	public T getRandom() {
		return getRandom(createDetachedCriteria());
	}
	
	/**
	 * Find a random result that fulfill the given criteria.
	 * 
	 * @param criteria The {@link DetachedCriteria} to fulfill.
	 * @return The random result.
	 */
	protected T getRandom(DetachedCriteria criteria) {
		criteria.add(getRandomRestriction());
		return findUnique(criteria);
	}
	
	/**
	 * @return Criterion to get a random row
	 */
	protected Criterion getRandomRestriction() {
		return Restrictions.sqlRestriction("1=1 order by rand()");
	}
}
