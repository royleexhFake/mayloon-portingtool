/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package java.util;


import java.io.Serializable;

/**
 * ArrayList is an implementation of List, backed by an array. All optional
 * operations are supported, adding, removing, and replacing. The elements can
 * be any objects.
 * @since 1.2
 */
public class ArrayList<E> extends AbstractList<E> implements List<E>, Cloneable,
		Serializable, RandomAccess {

	private static final long serialVersionUID = 8683452581122892189L;

	private transient int firstIndex;
    
    private transient int lastIndex;

	private transient E[] array;

	/**
	 * Constructs a new instance of ArrayList with zero capacity.
	 */
	public ArrayList() {
		this(0);
	}

	/**
	 * Constructs a new instance of ArrayList with the specified capacity.
	 * 
	 * @param capacity
	 *            the initial capacity of this ArrayList
	 */
	public ArrayList(int capacity) {
        if(capacity < 0) {
            throw new IllegalArgumentException();
        }
		firstIndex = lastIndex = 0;
		try {
			array = newElementArray(capacity);
		} catch (NegativeArraySizeException e) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Constructs a new instance of ArrayList containing the elements in the
	 * specified collection. The ArrayList will have an initial capacity which
	 * is 110% of the size of the collection. The order of the elements in this
	 * ArrayList is the order they are returned by the collection iterator.
	 * 
	 * @param collection
	 *            the collection of elements to add
	 */
	public ArrayList(Collection<? extends E> collection) {
        if(collection == null) {
            throw new NullPointerException();
        }
		int size = collection.size();
		firstIndex = lastIndex = 0;
		array = newElementArray(size + (size / 10));
		addAll(collection);
	}
    
    @SuppressWarnings("unchecked")
    private E[] newElementArray(int size) {
        return (E[])new Object[size];
    }

	/**
	 * Inserts the specified object into this ArrayList at the specified
	 * location. The object is inserted before any previous element at the
	 * specified location. If the location is equal to the size of this
	 * ArrayList, the object is added at the end.
	 * 
	 * @param location
	 *            the index at which to insert
	 * @param object
	 *            the object to add
	 * 
	 * @exception IndexOutOfBoundsException
	 *                when <code>location < 0 || >= size()</code>
	 */
	@Override
    public void add(int location, E object) {
		int size = lastIndex - firstIndex;
		if (0 < location && location < size) {
			if (firstIndex == 0 && lastIndex == array.length) {
				growForInsert(location, 1);
			} else if ((location < size / 2 && firstIndex > 0)
					|| lastIndex == array.length) {
				System.arraycopy(array, firstIndex, array, --firstIndex,
						location);
			} else {
				int index = location + firstIndex;
				System.arraycopy(array, index, array, index + 1, size
						- location);
				lastIndex++;
			}
			array[location + firstIndex] = object;
		} else if (location == 0) {
			if (firstIndex == 0) {
                growAtFront(1);
            }
			array[--firstIndex] = object;
		} else if (location == size) {
			if (lastIndex == array.length) {
                growAtEnd(1);
            }
			array[lastIndex++] = object;
		} else {
            throw new IndexOutOfBoundsException("Invalid index " + location
                    + ", size is " + size);
        }

		modCount++;
	}

	/**
	 * Adds the specified object at the end of this ArrayList.
	 * 
	 * @param object
	 *            the object to add
	 * @return true
	 */
	@Override
    public boolean add(E object) {
		if (lastIndex == array.length) {
            growAtEnd(1);
        }
		array[lastIndex++] = object;
		modCount++;
		return true;
	}

	/**
     * Inserts the objects in the specified Collection at the specified location
     * in this ArrayList. The objects are added in the order they are returned
     * from the Collection iterator.
     * 
     * @param location the index at which to insert
     * @param collection the Collection of objects
     * @return true if this ArrayList is modified, false otherwise
     * 
     * @exception IndexOutOfBoundsException when
     *            <code>location < 0 || > size()</code>
     */
    @Override
    public boolean addAll(int location, Collection<? extends E> collection) {
        int size = lastIndex - firstIndex;
        if (location < 0 || location > size) {
            throw new IndexOutOfBoundsException("Invalid index " + location
                    + ", size is " + size);
        }
        if(collection == null){
            throw new NullPointerException();
        }
        int growSize = collection.size();
        if (0 < location && location < size) {
            if (array.length - size < growSize) {
                growForInsert(location, growSize);
            } else if ((location < size / 2 && firstIndex > 0)
                    || lastIndex > array.length - growSize) {
                int newFirst = firstIndex - growSize;
                if (newFirst < 0) {
                    int index = location + firstIndex;
                    System.arraycopy(array, index, array, index - newFirst,
                            size - location);
                    lastIndex -= newFirst;
                    newFirst = 0;
                }
                System.arraycopy(array, firstIndex, array, newFirst, location);
                firstIndex = newFirst;
            } else {
                int index = location + firstIndex;
                System.arraycopy(array, index, array, index + growSize, size
                        - location);
                lastIndex += growSize;
            }
        } else if (location == 0) {
            growAtFront(growSize);
            firstIndex -= growSize;
        } else if (location == size) {
            if (lastIndex > array.length - growSize) {
                growAtEnd(growSize);
            }
            lastIndex += growSize;
        }

        if (growSize > 0) {
            Iterator<? extends E> it = collection.iterator();
            int index = location + firstIndex;
            int end = index + growSize;
            while (index < end) {
                array[index++] = it.next();
            }
            modCount++;
            return true;
        }
        return false;
    }

	/**
	 * Adds the objects in the specified Collection to this ArrayList.
	 * 
	 * @param collection
	 *            the Collection of objects
	 * @return true if this ArrayList is modified, false otherwise
	 */
	@Override
    public boolean addAll(Collection<? extends E> collection) {
        if(collection == null) {
            throw new NullPointerException();
        }
		int growSize = collection.size();
		if (growSize > 0) {
			if (lastIndex > array.length - growSize) {
                growAtEnd(growSize);
            }
			Iterator<? extends E> it = collection.iterator();
			int end = lastIndex + growSize;
			while (lastIndex < end) {
                array[lastIndex++] = it.next();
            }
			modCount++;
			return true;
		}
		return false;
	}

	/**
	 * Removes all elements from this ArrayList, leaving it empty.
	 * 
	 * @see #isEmpty
	 * @see #size
	 */
	@Override
    public void clear() {
		if (firstIndex != lastIndex) {
			Arrays.fill(array, firstIndex, lastIndex, null);
			firstIndex = lastIndex = 0;
			modCount++;
		}
	}

	/**
	 * Answers a new ArrayList with the same elements, size and capacity as this
	 * ArrayList.
	 * 
	 * @return a shallow copy of this ArrayList
	 * 
	 * @see java.lang.Cloneable
	 */
	@Override
    @SuppressWarnings("unchecked")
    public Object clone() {
		try {
			ArrayList<E> newList = (ArrayList<E>) super.clone();
			newList.array = array.clone();
			return newList;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	/**
	 * Searches this ArrayList for the specified object.
	 * 
	 * @param object
	 *            the object to search for
	 * @return true if <code>object</code> is an element of this ArrayList,
	 *         false otherwise
	 */
	@Override
    public boolean contains(Object object) {
		if (object != null) {
			for (int i = firstIndex; i < lastIndex; i++) {
                if (object.equals(array[i])) {
                    return true;
                }
            }
		} else {
			for (int i = firstIndex; i < lastIndex; i++) {
                if (array[i] == null) {
                    return true;
                }
            }
		}
		return false;
	}

	/**
	 * Ensures that this ArrayList can hold the specified number of elements
	 * without growing.
	 * 
	 * @param minimumCapacity
	 *            the minimum number of elements that this ArrayList will hold
	 *            before growing
	 */
	public void ensureCapacity(int minimumCapacity) {
		if (array.length < minimumCapacity) {
			if (firstIndex > 0) {
                growAtFront(minimumCapacity - array.length);
            } else {
                growAtEnd(minimumCapacity - array.length);
            }
		}
	}

	/**
	 * Answers the element at the specified location in this ArrayList.
	 * 
	 * @param location
	 *            the index of the element to return
	 * @return the element at the specified index
	 * 
	 * @exception IndexOutOfBoundsException
	 *                when <code>location < 0 || >= size()</code>
	 */
	@Override
    public E get(int location) {
		if (0 <= location && location < (lastIndex - firstIndex)) {
			return array[firstIndex + location];
        }
		throw new IndexOutOfBoundsException("Invalid index " + location
                + ", size is " + (lastIndex - firstIndex));
	}

	private void growAtEnd(int required) {
		int size = lastIndex - firstIndex;
		if (firstIndex >= required - (array.length - lastIndex)) {
			int newLast = lastIndex - firstIndex;
			if (size > 0) {
				System.arraycopy(array, firstIndex, array, 0, size);
				int start = newLast < firstIndex ? firstIndex : newLast;
				Arrays.fill(array, start, array.length, null);
			}
			firstIndex = 0;
			lastIndex = newLast;
		} else {
			int increment = size / 2;
			if (required > increment) {
                increment = required;
            }
			if (increment < 12) {
                increment = 12;
            }
			E[] newArray = newElementArray(size + increment);
			if (size > 0) {
                System.arraycopy(array, firstIndex, newArray, firstIndex, size);
            }
			array = newArray;
		}
	}

	private void growAtFront(int required) {
		int size = lastIndex - firstIndex;
		if (array.length - lastIndex >= required) {
			int newFirst = array.length - size;
			if (size > 0) {
				System.arraycopy(array, firstIndex, array, newFirst, size);
				int length = firstIndex + size > newFirst ? newFirst
						: firstIndex + size;
				Arrays.fill(array, firstIndex, length, null);
			}
			firstIndex = newFirst;
			lastIndex = array.length;
		} else {
			int increment = size / 2;
			if (required > increment) {
                increment = required;
            }
			if (increment < 12) {
                increment = 12;
            }
			E[] newArray = newElementArray(size + increment);
			if (size > 0) {
                System.arraycopy(array, firstIndex, newArray, newArray.length
						- size, size);
            }
			firstIndex = newArray.length - size;
			lastIndex = newArray.length;
			array = newArray;
		}
	}

	private void growForInsert(int location, int required) {
		int size = lastIndex - firstIndex, increment = size / 2;
		if (required > increment) {
            increment = required;
        }
		if (increment < 12) {
            increment = 12;
        }
		E[] newArray = newElementArray(size + increment);
		if (location < size / 2) {
			int newFirst = newArray.length - (size + required);
			System.arraycopy(array, firstIndex + location , newArray, location + increment,
					size - location);
			System.arraycopy(array, firstIndex , newArray, newFirst, location);
			firstIndex = newFirst;
			lastIndex = newArray.length;
		} else {
			System.arraycopy(array, firstIndex, newArray, 0, location);
			System.arraycopy(array, location + firstIndex, newArray, location + required,
					size - location);
			firstIndex = 0;
			lastIndex += required;
		}
		array = newArray;
	}

	/**
	 * Searches this ArrayList for the specified object and returns the index of
	 * the first occurrence.
	 * 
	 * @param object
	 *            the object to search for
	 * @return the index of the first occurrence of the object
	 */
	@Override
    public int indexOf(Object object) {
		if (object != null) {
			for (int i = firstIndex; i < lastIndex; i++) {
                if (object.equals(array[i])) {
                    return i - firstIndex;
                }
            }
		} else {
			for (int i = firstIndex; i < lastIndex; i++) {
                if (array[i] == null) {
                    return i - firstIndex;
                }
            }
		}
		return -1;
	}

	/**
	 * Answers if this ArrayList has no elements, a size of zero.
	 * 
	 * @return true if this ArrayList has no elements, false otherwise
	 * 
	 * @see #size
	 */
	@Override
    public boolean isEmpty() {
		return lastIndex == firstIndex;
	}

	/**
	 * Searches this ArrayList for the specified object and returns the index of
	 * the last occurrence.
	 * 
	 * @param object
	 *            the object to search for
	 * @return the index of the last occurrence of the object
	 */
	@Override
    public int lastIndexOf(Object object) {
		if (object != null) {
			for (int i = lastIndex - 1; i >= firstIndex; i--) {
                if (object.equals(array[i])) {
                    return i - firstIndex;
                }
            }
		} else {
			for (int i = lastIndex - 1; i >= firstIndex; i--) {
                if (array[i] == null) {
                    return i - firstIndex;
                }
            }
		}
		return -1;
	}

	/**
	 * Removes the object at the specified location from this ArrayList.
	 * 
	 * @param location
	 *            the index of the object to remove
	 * @return the removed object
	 * 
	 * @exception IndexOutOfBoundsException
	 *                when <code>location < 0 || >= size()</code>
	 */
	@Override
    public E remove(int location) {
	    /***
	     * @j2sNative
	     * var isObj = typeof (location) == "object";
	     * if (isObj) {
	     * return this.removeObject(location);
	     * }
	     */{}

		E result;
		int size = lastIndex - firstIndex;
		if (0 <= location && location < size) {
			if (location == size - 1) {
				result = array[--lastIndex];
				array[lastIndex] = null;
			} else if (location == 0) {
				result = array[firstIndex];
				array[firstIndex++] = null;
			} else {
				int elementIndex = firstIndex + location;
				result = array[elementIndex];
				if (location < size / 2) {
					System.arraycopy(array, firstIndex, array, firstIndex + 1,
							location);
					array[firstIndex++] = null;
				} else {
					System.arraycopy(array, elementIndex + 1, array,
							elementIndex, size - location - 1);
					array[--lastIndex] = null;
				}
			}
		} else {
            throw new IndexOutOfBoundsException("Invalid index " + location
                    + ", size is " + size);
        }

		modCount++;
		return result;
	}

	/* (non-Javadoc)
     * @see java.util.AbstractCollection#remove(java.lang.Object)
     */
    @Override
    public boolean remove(Object object) {
        return removeObject(object);
    }

    private boolean removeObject(Object object) {
        Object[] a = array;
        if (object != null) {
            for (int i = firstIndex; i < lastIndex; i++) {
                if (object.equals(a[i])) {
                    System.arraycopy(a, i + 1, a, i, --lastIndex - i);
                    a[lastIndex] = null;  // Prevent memory leak
                    modCount++;
                    return true;
                }
            }
        } else {
            for (int i = firstIndex; i < lastIndex; i++) {
                if (a[i] == null) {
                    System.arraycopy(a, i + 1, a, i, --lastIndex - i);
                    a[lastIndex] = null;  // Prevent memory leak
                    modCount++;
                    return true;
                }
            }
        }
        return false;
    }
    /**
	 * Removes the objects in the specified range from the start to the end, but
	 * not including the end index.
	 * 
	 * @param start
	 *            the index at which to start removing
	 * @param end
	 *            the index one past the end of the range to remove
	 * 
	 * @exception IndexOutOfBoundsException
	 *                when <code>start < 0, start > end</code> or
	 *                <code>end > size()</code>
	 */
	@Override
    protected void removeRange(int start, int end) {
		if (start >= 0 && start <= end && end <= lastIndex - firstIndex) {
			if (start == end) {
                return;
            }
			int size = lastIndex - firstIndex;
			if (end == size) {
				Arrays.fill(array, firstIndex + start, lastIndex, null);
				lastIndex = firstIndex + start;
			} else if (start == 0) {
				Arrays.fill(array, firstIndex, firstIndex + end, null);
				firstIndex += end;
			} else {
				System.arraycopy(array, firstIndex + end, array, firstIndex
						+ start, size - end);
				int newLast = lastIndex + start - end;
				Arrays.fill(array, newLast, lastIndex, null);
				lastIndex = newLast;
			}
			modCount++;
		} else {
            throw new IndexOutOfBoundsException("Invalid index " + start
                    + ", size is " + (lastIndex - firstIndex));
        }
	}

	/**
	 * Replaces the element at the specified location in this ArrayList with the
	 * specified object.
	 * 
	 * @param location
	 *            the index at which to put the specified object
	 * @param object
	 *            the object to add
	 * @return the previous element at the index
	 * 
	 * @exception IndexOutOfBoundsException
	 *                when <code>location < 0 || >= size()</code>
	 */
	@Override
    public E set(int location, E object) {
		if (0 <= location && location < lastIndex - firstIndex) {
			E result = array[firstIndex + location];
			array[firstIndex + location] = object;
			return result;
		}
		throw new IndexOutOfBoundsException("Invalid index " + location
                + ", size is " + (lastIndex - firstIndex));
	}

	/**
	 * Answers the number of elements in this ArrayList.
	 * 
	 * @return the number of elements in this ArrayList
	 */
	@Override
    public int size() {
		return lastIndex - firstIndex;
	}

	/**
	 * Answers a new array containing all elements contained in this ArrayList.
	 * 
	 * @return an array of the elements from this ArrayList
	 */
	@Override
    public Object[] toArray() {
		int size = lastIndex - firstIndex;
		Object[] result = new Object[size];
		System.arraycopy(array, firstIndex, result, 0, size);
		return result;
	}

	/**
	 * Answers an array containing all elements contained in this ArrayList. If
	 * the specified array is large enough to hold the elements, the specified
	 * array is used, otherwise an array of the same type is created. If the
	 * specified array is used and is larger than this ArrayList, the array
	 * element following the collection elements is set to null.
	 * 
	 * @param contents
	 *            the array
	 * @return an array of the elements from this ArrayList
	 * 
	 * @exception ArrayStoreException
	 *                when the type of an element in this ArrayList cannot be
	 *                stored in the type of the specified array
	 */
	@Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] contents) {
		int size = lastIndex - firstIndex;
        if (contents.length < size) {
            /**
             * @j2sNative
             * contents = new Array(size);
             **/{}
        }
		System.arraycopy(array, firstIndex, contents, 0, size);
		if (size < contents.length) {
            contents[size] = null;
        }
		return contents;
	}

	/**
	 * Sets the capacity of this ArrayList to be the same as the size.
	 * 
	 * @see #size
	 */
	public void trimToSize() {
		int size = lastIndex - firstIndex;
		E[] newArray = newElementArray(size);
		System.arraycopy(array, firstIndex, newArray, 0, size);
		array = newArray;
		firstIndex = 0;
		lastIndex = array.length;
		modCount++;
	}

	/*
	private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField(
			"size", Integer.TYPE) }; //$NON-NLS-1$
	*/

    /**
     * Override the hashCode method
     * */
    @Override
    public int hashCode() {
        int result = 1;
        Iterator<?> it = iterator();
        while (it.hasNext()) {
            Object object = it.next();
            result = (31 * result) + (object == null ? 0 : object.hashCode());
        }
        return result;
    }
}
