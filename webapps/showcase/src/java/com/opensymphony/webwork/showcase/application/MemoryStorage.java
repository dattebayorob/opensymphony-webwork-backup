/* ====================================================================
 * The OpenSymphony Software License, Version 1.1
 *
 * (this license is derived and fully compatible with the Apache Software
 * License - see http://www.apache.org/LICENSE.txt)
 *
 * Copyright (c) 2001-2005 The OpenSymphony Group. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        OpenSymphony Group (http://www.opensymphony.com/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "OpenSymphony" and "The OpenSymphony Group"
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact license@opensymphony.com .
 *
 * 5. Products derived from this software may not be called "OpenSymphony"
 *    or "WebWork", nor may "OpenSymphony" or "WebWork" appear in their
 *    name, without prior written permission of the OpenSymphony Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */
package com.opensymphony.webwork.showcase.application;

import com.opensymphony.webwork.showcase.exception.CreateException;
import com.opensymphony.webwork.showcase.exception.DuplicateKeyException;
import com.opensymphony.webwork.showcase.exception.StorageException;
import com.opensymphony.webwork.showcase.exception.UpdateException;
import com.opensymphony.webwork.showcase.model.IdEntity;
import com.opensymphony.webwork.showcase.application.Storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * MemoryStorage.
 * Very simple in-memory persistence emulation.
 *
 * @author <a href="mailto:gielen@it-neering.net">Rene Gielen</a>
 */

public class MemoryStorage implements Storage {

	private static final long serialVersionUID = -3950031281339525427L;

	private static final Log log = LogFactory.getLog(MemoryStorage.class);
    
    
    private Map memory = new HashMap();

    private Map getEntityMap ( Class entityClass ) {
        if (entityClass != null) {
            Map tryMap = (Map) memory.get(entityClass);
            if (tryMap == null) {
                synchronized(memory) {
                    tryMap = new HashMap();
                    memory.put(entityClass, tryMap);
                }
            }
            return tryMap;
        } else {
            return null;
        }
    }

    private IdEntity intStore( Class entityClass, IdEntity object ) {
        getEntityMap(entityClass).put(object.getId(), object);
        return object;
    }

    public IdEntity get( Class entityClass, Serializable id ) {
        if (entityClass != null && id != null) {
            return (IdEntity) getEntityMap(entityClass).get(id);
        } else {
            return null;
        }
    }

    public Serializable create ( IdEntity object ) throws CreateException {
        if (object == null) {
            throw new CreateException("Either given class or object was null");
        }
        if (object.getId() == null) {
            throw new CreateException("Cannot store object with null id");
        }
        if (get(object.getClass(), object.getId()) != null) {
            throw new DuplicateKeyException("Object with this id already exists.");
        }
        return intStore(object.getClass(), object).getId();
    }

    public IdEntity update ( IdEntity object ) throws UpdateException {
        if (object == null) {
            throw new UpdateException("Cannot update null object.");
        }
        if ( get(object.getClass(), object.getId())==null ) {
            throw new UpdateException("Object to update not found.");
        }
        return intStore(object.getClass(), object);
    }

    public Serializable merge ( IdEntity object ) throws StorageException {
        if (object == null) {
            throw new StorageException("Cannot merge null object");
        }
        if (object.getId() == null || get(object.getClass(), object.getId())==null) {
            return create(object);
        } else {
            return update(object).getId();
        }
    }

    public int delete( Class entityClass, Serializable id ) throws CreateException {
        try {
            if (get(entityClass, id) != null) {
                getEntityMap(entityClass).remove(id);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            throw new CreateException(e);
        }
    }

    public int delete( IdEntity object ) throws CreateException {
        if (object == null) {
            throw new CreateException("Cannot delete null object");
        }
        return delete(object.getClass(), object.getId());
    }

    public Collection findAll( Class entityClass ) {
        if (entityClass != null) {
        	return getEntityMap(entityClass).values();
        } else {
            return new ArrayList();
        }
    }

    public void reset() {
        this.memory = new HashMap();
    }

}
