package it.unibz.testhunter.utils;

import java.util.ArrayList;
import java.util.Collection;


public class FilteredCollection {
	
	public static <T> Collection<T> filter(Collection<T> target, Predicate<T> predicate) {
	    Collection<T> result = new ArrayList<T>();
	    for (T element: target) {
	        if (predicate.apply(element)) {
	            result.add(element);
	        }
	    }
	    return result;
	}

}
