package com.jdroid.javaweb.hibernate;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.google.common.base.Function;
import com.jdroid.java.repository.ObjectNotFoundException;
import com.jdroid.java.repository.Repository;
import com.jdroid.javaweb.domain.Entity;
import com.jdroid.javaweb.search.PagedResult;
import com.jdroid.javaweb.search.Pager;
import com.jdroid.javaweb.search.Sorting;

/**
 * Hibernate Repository
 * 
 * @param <T> The {@link Entity}
 */
public class AbstractHibernateRepository<T extends Entity> extends HibernateDaoSupport implements Repository<T> {
	
	/** Replace each object with the '?' character for SQL */
	public static final Function<Object, String> SQL_REPLACEMENT = new Function<Object, String>() {
		
		@Override
		public String apply(Object from) {
			return "?";
		}
	};
	
	private Class<T> entityClass;
	
	/**
	 * @param entityClass The class of the {@link Entity}
	 */
	public AbstractHibernateRepository(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	/**
	 * @return The {@link Entity} class
	 */
	protected final Class<T> getEntityClass() {
		return this.entityClass;
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#get(java.lang.Long)
	 */
	@Override
	public T get(Long id) throws ObjectNotFoundException {
		T t = getHibernateTemplate().get(this.entityClass, id);
		if (t == null) {
			throw new ObjectNotFoundException(entityClass, id);
		}
		return t;
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#add(com.jdroid.java.domain.Identifiable)
	 */
	@Override
	public void add(T entity) {
		getHibernateTemplate().save(entity);
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#addAll(java.util.Collection)
	 */
	@Override
	public void addAll(Collection<T> entities) {
		for (T entity : entities) {
			add(entity);
		}
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#update(com.jdroid.java.domain.Identifiable)
	 */
	@Override
	public void update(T item) {
		getHibernateTemplate().update(item);
	};
	
	/**
	 * @see com.jdroid.java.repository.Repository#remove(com.jdroid.java.domain.Identifiable)
	 */
	@Override
	public void remove(T entity) {
		getHibernateTemplate().delete(entity);
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#remove(java.lang.Long)
	 */
	@Override
	public void remove(Long id) {
		this.remove(getHibernateTemplate().get(this.entityClass, id));
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#removeAll()
	 */
	@Override
	public void removeAll() {
		getHibernateTemplate().deleteAll(getHibernateTemplate().loadAll(this.entityClass));
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#removeAll(java.util.Collection)
	 */
	@Override
	public void removeAll(Collection<T> items) {
		getHibernateTemplate().deleteAll(items);
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#getAll()
	 */
	@Override
	public List<T> getAll() {
		return getHibernateTemplate().loadAll(this.entityClass);
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#replaceAll(java.util.Collection)
	 */
	@Override
	public void replaceAll(Collection<T> entities) {
		removeAll();
		addAll(entities);
	}
	
	/**
	 * @return A {@link Criteria} for the entity class
	 */
	protected Criteria createCriteria() {
		return this.getSession().createCriteria(this.entityClass).setCacheable(false);
	}
	
	/**
	 * @param propertyName The property name
	 * @param value The property value
	 * @return The object which matches the property with the value
	 */
	protected T findUnique(String propertyName, Object value) {
		return this.findUnique(this.createDetachedCriteria().add(Restrictions.eq(propertyName, value)));
	}
	
	/**
	 * @param detachedCriteria The {@link DetachedCriteria} with the filter specification
	 * @return The object which matches with the criteria
	 */
	protected T findUnique(DetachedCriteria detachedCriteria) throws ObjectNotFoundException {
		List<T> list = this.find(detachedCriteria);
		if (list.isEmpty()) {
			throw new ObjectNotFoundException(this.entityClass);
		}
		return list.iterator().next();
	}
	
	/**
	 * @param propertyName The property name
	 * @param value The property value
	 * @return An objects list
	 */
	protected List<T> find(String propertyName, Object value) {
		return this.find(this.createDetachedCriteria().add(Restrictions.eq(propertyName, value)));
	}
	
	/**
	 * Finds a list of objects that matches with the {@link DetachedCriteria}
	 * 
	 * @param detachedCriteria The {@link DetachedCriteria} with the filter specification
	 * @return An objects list
	 */
	@SuppressWarnings("unchecked")
	protected List<T> find(DetachedCriteria detachedCriteria) {
		return this.getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Find a list of objects with pagination.
	 * 
	 * @param pager
	 * @return a result page
	 */
	protected PagedResult<T> find(Pager pager) {
		return this.find(this.createDetachedCriteria(), pager);
	}
	
	protected PagedResult<T> find(DetachedCriteria detachedCriteria, Pager pager) {
		return find(detachedCriteria, pager, null);
	}
	
	protected PagedResult<T> find(DetachedCriteria detachedCriteria, Pager pager, Sorting sorting) {
		
		// To ensure that one-to-many associations do not return duplicate rows
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		// Check if this is the last page
		Long count = size(detachedCriteria);
		Integer totalPages = pager.getMaxPages(count);
		
		if (pager.getPage() > totalPages) {
			return new PagedResult<T>();
		}
		
		// Reset criteria after count
		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		if (sorting != null) {
			Order order = null;
			if (sorting.isAscending()) {
				order = Order.asc(sorting.getSortingKey().getProperty());
			} else {
				order = Order.desc(sorting.getSortingKey().getProperty());
			}
			detachedCriteria.addOrder(order);
		}
		
		@SuppressWarnings("unchecked")
		List<T> data = this.getHibernateTemplate().findByCriteria(detachedCriteria, pager.getOffset(), pager.getSize());
		
		return new PagedResult<T>(data, pager.isLastPage(count));
	}
	
	/**
	 * @return A {@link DetachedCriteria} for the entity class
	 */
	protected DetachedCriteria createDetachedCriteria() {
		return DetachedCriteria.forClass(this.entityClass);
	}
	
	/**
	 * Returns the number of objects that match with the filter
	 * 
	 * @param detachedCriteria The criteria with the filter
	 * @return The size of object that match with the filter
	 */
	protected Long size(final DetachedCriteria detachedCriteria) {
		return size(detachedCriteria, Projections.rowCount());
	}
	
	/**
	 * Returns the number of objects that match with the filter
	 * 
	 * @param detachedCriteria the criteria with the filter
	 * @param projection the projection to use when querying for the size
	 * @return the size of object that match with the filter
	 */
	protected Long size(final DetachedCriteria detachedCriteria, Projection projection) {
		detachedCriteria.setProjection(projection);
		return this.getHibernateTemplate().execute(new HibernateCallback<Long>() {
			
			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				CriteriaImpl criteria = (CriteriaImpl)detachedCriteria.getExecutableCriteria(session);
				for (Iterator<?> iterator = criteria.iterateOrderings(); iterator.hasNext();) {
					iterator.next();
					iterator.remove();
				}
				if (criteria.getMaxResults() != null) {
					criteria.setMaxResults(Integer.MAX_VALUE);
				}
				if (criteria.getFirstResult() != null) {
					criteria.setFirstResult(0);
				}
				return (Long)criteria.uniqueResult();
			}
		});
	}
	
	/**
	 * Returns true if an object for the defined criteria exists
	 * 
	 * @param detachedCriteria The detached criteria with the filter
	 * @return If exist any entity that matched with the filter
	 */
	protected boolean exists(DetachedCriteria detachedCriteria) {
		return !this.size(detachedCriteria).equals(0L);
	}
	
	/**
	 * @param propertyName The property name
	 * @param value The property value
	 * @return Whether the object that matches the property with the value exists or not
	 */
	protected boolean exists(String propertyName, Object value) {
		return this.exists(this.createDetachedCriteria().add(Restrictions.eq(propertyName, value)));
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#isEmpty()
	 */
	@Override
	public Boolean isEmpty() {
		return size(createDetachedCriteria()).equals(0);
	}
}
