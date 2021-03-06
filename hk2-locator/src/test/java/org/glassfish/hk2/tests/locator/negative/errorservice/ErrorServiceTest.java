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

package org.glassfish.hk2.tests.locator.negative.errorservice;

import java.util.List;

import org.junit.Assert;

import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.tests.locator.utilities.LocatorHelper;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.junit.Test;

/**
 * Note that the ordering of the test methods in this
 * class is very important.  Therefore we do not rely
 * on the Junit ordering of &#64;Test methods, instead
 * having one test that calls the other tests in the
 * order in which they have to go in order to
 * function properly
 *
 * @author jwells
 *
 */
public class ErrorServiceTest {
    private final static String TEST_NAME = "ErrorServiceTest";
    private final static ServiceLocator locator = LocatorHelper.create(TEST_NAME, new ErrorServiceModule());

    /** The string the exception will throw */
    public final static String EXCEPTION_STRING = "Expected Exception";

    /** Another exception string */
    public final static String EXCEPTION_STRING_DUEX = "Expected Exception Duex";

    private void testLookupPriorToFixing(ErrorServiceImpl esi) {
        esi.clear();

        FaultyClass fc = locator.getService(FaultyClass.class);
        Assert.assertNull(fc);

        Descriptor faultyDesc = locator.getBestDescriptor(BuilderHelper.createContractFilter(FaultyClass.class.getName()));
        Assert.assertNotNull(faultyDesc);

        ActiveDescriptor<?> fromError = esi.getDescriptor();
        Assert.assertNotNull(fromError);

        Assert.assertEquals(faultyDesc, fromError);

        Assert.assertNull(esi.getInjectee());

        MultiException me = esi.getMe();
        Assert.assertNotNull(me);

        Assert.assertTrue("Expected " + EXCEPTION_STRING + " but got " + me.getMessage(), me.getMessage().contains(EXCEPTION_STRING));
    }

    private void testLookupInjecteePriorToFixing(ErrorServiceImpl esi) {
        esi.clear();

        try {
            locator.getService(InjectedWithFaultyClass.class);
            Assert.fail("The bad injection point would cause this to fail");
        }
        catch (MultiException me) {
            Assert.assertTrue(me.getMessage(),
                    me.getMessage().contains("There was no object available for injection at "));

        }

        Descriptor faultyDesc = locator.getBestDescriptor(BuilderHelper.createContractFilter(FaultyClass.class.getName()));
        Assert.assertNotNull(faultyDesc);

        ActiveDescriptor<?> fromError = esi.getDescriptor();
        Assert.assertNotNull(fromError);

        Assert.assertEquals(faultyDesc, fromError);

        Assert.assertNotNull(esi.getInjectee());

        MultiException me = esi.getMe();
        Assert.assertNotNull(me);

        Assert.assertTrue("Expected " + EXCEPTION_STRING + " but got " + me.getMessage(), me.getMessage().contains(EXCEPTION_STRING));
    }

    private void testLookupHandlesPriorToFixing(ErrorServiceImpl esi) {
        esi.clear();

        List<FaultyClass> faulties = locator.getAllServices(
                FaultyClass.class);
        Assert.assertTrue("faulties.size=" + faulties.size(), faulties.isEmpty());

        Descriptor faultyDesc = locator.getBestDescriptor(BuilderHelper.createContractFilter(FaultyClass.class.getName()));
        Assert.assertNotNull(faultyDesc);

        ActiveDescriptor<?> fromError = esi.getDescriptor();
        Assert.assertNotNull(fromError);

        Assert.assertEquals(faultyDesc, fromError);

        Assert.assertNull(esi.getInjectee());

        MultiException me = esi.getMe();
        Assert.assertNotNull(me);

        Assert.assertTrue("Expected " + EXCEPTION_STRING + " but got " + me.getMessage(), me.getMessage().contains(EXCEPTION_STRING));
    }

    private void testLookupHandlesWithContractPriorToFixing(ErrorServiceImpl esi) {
        esi.clear();

        List<ServiceHandle<FaultyClass>> handles = locator.getAllServiceHandles(FaultyClass.class);
        Assert.assertTrue("handles.size=" + handles.size(), handles.isEmpty());

        Descriptor faultyDesc = locator.getBestDescriptor(BuilderHelper.createContractFilter(FaultyClass.class.getName()));
        Assert.assertNotNull(faultyDesc);

        ActiveDescriptor<?> fromError = esi.getDescriptor();
        Assert.assertNotNull(fromError);

        Assert.assertEquals(faultyDesc, fromError);

        Assert.assertNull(esi.getInjectee());

        MultiException me = esi.getMe();
        Assert.assertNotNull(me);

        Assert.assertTrue("Expected " + EXCEPTION_STRING + " but got " + me.getMessage(), me.getMessage().contains(EXCEPTION_STRING));
    }

    private void testLookupPriorToFixingButThrowing(ErrorServiceImpl esi) {
        esi.doThrow();
        esi.clear();

        try {
            locator.getService(FaultyClass.class);
            Assert.fail("The error service now throws an assertion error");
        }
        catch (MultiException me) {
            Assert.assertTrue("Expected " + EXCEPTION_STRING_DUEX + " but got " + me.getMessage(),
                    me.getMessage().contains(EXCEPTION_STRING_DUEX));

        }

        ActiveDescriptor<?> fromError = esi.getDescriptor();
        Assert.assertNotNull(fromError);

        Assert.assertNull(esi.getInjectee());

        MultiException me = esi.getMe();
        Assert.assertNotNull(me);

        Assert.assertTrue("Expected " + EXCEPTION_STRING + " but got " + me.getMessage(), me.getMessage().contains(EXCEPTION_STRING));
    }

    private void testLookupPriorToFixingButReThrowing(ErrorServiceImpl esi) {
        esi.reThrow();
        esi.clear();

        try {
            locator.getService(FaultyClass.class);
            Assert.fail("The error service now rethrows the ME error");
        }
        catch (MultiException me) {
            Assert.assertTrue("Expected " + EXCEPTION_STRING + " but got " + me.getMessage(), me.getMessage().contains(EXCEPTION_STRING));
        }

        ActiveDescriptor<?> fromError = esi.getDescriptor();
        Assert.assertNotNull(fromError);

        Assert.assertNull(esi.getInjectee());

        MultiException me = esi.getMe();
        Assert.assertNotNull(me);

        Assert.assertTrue("Expected " + EXCEPTION_STRING + " but got " + me.getMessage(), me.getMessage().contains(EXCEPTION_STRING));
    }

    private void testLookupAfterFixing() {
        FaultyClass fc = locator.getService(FaultyClass.class);
        Assert.assertNotNull(fc);
    }

    private void testLookupInjecteeAfterFixing() {
        InjectedWithFaultyClass iwfc = locator.getService(InjectedWithFaultyClass.class);
        Assert.assertNotNull(iwfc);
    }

    /**
     * This test ensures that the other methods are called in the proper order
     */
    @Test
    public void testOrdered() {
        ErrorServiceImpl esi = locator.getService(ErrorServiceImpl.class);

        testLookupPriorToFixing(esi);
        testLookupInjecteePriorToFixing(esi);
        testLookupHandlesPriorToFixing(esi);
        testLookupHandlesWithContractPriorToFixing(esi);
        testLookupPriorToFixingButThrowing(esi);
        testLookupPriorToFixingButReThrowing(esi);

        TempermentalLoader.fixIt();

        testLookupAfterFixing();
        testLookupInjecteeAfterFixing();
    }

}
