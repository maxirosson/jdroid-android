package com.jdroid.java.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Maxi Rosson
 */
public class Lists {
	
	// ArrayList
	
	/**
	 * Creates a <i>mutable</i>, empty {@code ArrayList} instance.
	 * 
	 * @return a new, empty {@code ArrayList}
	 */
	public static <E> ArrayList<E> newArrayList() {
		return new ArrayList<E>();
	}
	
	/**
	 * Creates a <i>mutable</i> {@code ArrayList} instance containing the given elements.
	 * 
	 * @param elements the elements that the list should contain, in order
	 * @return a new {@code ArrayList} containing those elements
	 */
	public static <E> ArrayList<E> newArrayList(E... elements) {
		ArrayList<E> list = new ArrayList<E>();
		Collections.addAll(list, elements);
		return list;
	}
	
	public static <E> List<E> safeArrayList(List<E> list) {
		return list != null ? list : Lists.<E>newArrayList();
	}
	
	/**
	 * Creates a <i>mutable</i> {@code ArrayList} instance containing the given elements.
	 * 
	 * @param elements the elements that the list should contain, in order
	 * @return a new {@code ArrayList} containing those elements
	 */
	public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
		return (elements instanceof Collection) ? new ArrayList<E>((Collection<? extends E>)elements)
				: newArrayList(elements.iterator());
	}
	
	/**
	 * Creates a <i>mutable</i> {@code ArrayList} instance containing the given elements.
	 * 
	 * @param elements the elements that the list should contain, in order
	 * @return a new {@code ArrayList} containing those elements
	 */
	public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
		ArrayList<E> list = newArrayList();
		while (elements.hasNext()) {
			list.add(elements.next());
		}
		return list;
	}
	
	// LinkedList
	
	/**
	 * Creates an empty {@code LinkedList} instance.
	 * 
	 * @return a new, empty {@code LinkedList}
	 */
	public static <E> LinkedList<E> newLinkedList() {
		return new LinkedList<E>();
	}
	
	/**
	 * Creates a {@code LinkedList} instance containing the given elements.
	 * 
	 * @param elements the elements that the list should contain, in order
	 * @return a new {@code LinkedList} containing those elements
	 */
	public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
		LinkedList<E> list = newLinkedList();
		for (E element : elements) {
			list.add(element);
		}
		return list;
	}
}
