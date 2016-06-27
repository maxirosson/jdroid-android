package com.jdroid.javaweb.search;

import com.jdroid.java.exception.UnexpectedException;

public class Pager {
	
	private static final Integer DEFAULT_PAGE = 1;
	private static final Integer DEFAULT_PAGE_SIZE = 25;
	
	private Integer size;
	private Integer page;
	
	/**
	 * @param page
	 * @param size
	 */
	public Pager(Integer page, Integer size) {
		
		this.page = page != null ? page : DEFAULT_PAGE;
		this.size = size != null ? size : DEFAULT_PAGE_SIZE;
		
		if (this.page < 1) {
			throw new UnexpectedException("Wrong page [" + page + "]");
		}
		
		if (this.size < 1) {
			throw new UnexpectedException("Wrong page size [" + size + "]");
		}
	}
	
	/**
	 * @return the page
	 */
	public Integer getPage() {
		return page;
	}
	
	/**
	 * @return the size
	 */
	public Integer getSize() {
		return size;
	}
	
	public Integer getOffset() {
		return (page - 1) * size;
	}
	
	public Integer getMaxPages(Long total) {
		return (int)Math.ceil((total * 1.0) / size);
	}
	
	public Boolean isLastPage(Long total) {
		return getMaxPages(total).equals(page);
	}
}
