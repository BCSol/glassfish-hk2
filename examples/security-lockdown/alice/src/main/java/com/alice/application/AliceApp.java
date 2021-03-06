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

package com.alice.application;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.acme.security.AuditService;
import org.jvnet.hk2.annotations.Service;

/**
 * A service offered by Alice that Mallory is allowed to use.  This application
 * injects the AuditService, which it should have access to (since it is Alice)
 * but which Mallory himself does not have direct access to
 * 
 * @author jwells
 *
 */
@Service @Singleton
public class AliceApp {
    @Inject
    private AuditService auditor;
    
    /**
     * This service makes use of the auditor service directly, which
     * should be allowed as Alice itself has the ability to use the
     * audit service
     * 
     * @param fromMe
     */
    public void doAuditedService(String fromMe) {
        auditor.auditLog("Alice performed an audit for " + fromMe);
    }
}
