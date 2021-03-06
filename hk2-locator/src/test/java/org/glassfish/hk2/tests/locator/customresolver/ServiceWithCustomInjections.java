/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.hk2.tests.locator.customresolver;

import jakarta.inject.Inject;

import org.junit.Assert;

import org.glassfish.hk2.api.ServiceLocator;

/**
 * @author jwells
 *
 */
public class ServiceWithCustomInjections {
    private final static String FIELD = "Field";
    private final static String CONSTRUCTOR = "Constructor";
    private final static String METHOD = "Method";
    
    @Inject @Path(FIELD)
    private String byField;
    
    private final String byConstructor;
    
    private String byMethod;
    
    private boolean isValid = false;
    
    @Inject
    private ServiceWithCustomInjections(
            ServiceLocator locator,
            @Path(CONSTRUCTOR) String byConstructor) {
        Assert.assertNotNull(locator);
        this.byConstructor = byConstructor; 
    }
    
    @SuppressWarnings("unused")
    @Inject
    private void viaMethod(@Path(METHOD) String byMethod) {
        this.byMethod = byMethod;
    }
    
    @SuppressWarnings("unused")
    private void postConstruct() {
        Assert.assertEquals(FIELD, byField);
        Assert.assertEquals(CONSTRUCTOR, byConstructor);
        Assert.assertEquals(METHOD, byMethod);
        
        isValid = true;
    }

    /**
     * Called by the test
     * @return true if valid
     */
    public boolean isValid() {
        return isValid;
    }
}
