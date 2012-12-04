/*******************************************************************************
 * Copyright (c) 2007 java2script.org and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Zhou Renjian - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.internal.xhtml;

/**
 * @author zhou renjian
 *
 * 2006-4-27
 */
public class History {
	
	public native void forward();
	
	public native void back();
	
	public native void go(String location);
	
}
