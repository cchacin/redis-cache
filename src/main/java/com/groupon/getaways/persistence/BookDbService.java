/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.groupon.getaways.persistence;

import com.groupon.getaways.annotations.Find;
import com.groupon.getaways.annotations.Merge;
import com.groupon.getaways.annotations.NamedQuery;
import com.groupon.getaways.annotations.Persist;
import com.groupon.getaways.annotations.QueryParam;
import com.groupon.getaways.annotations.Remove;
import com.groupon.getaways.entities.Book;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

@Stateless
public abstract class BookDbService implements Serializable, InvocationHandler {

    @PersistenceContext(unitName = "book-pu")
    private EntityManager em;

    @Persist
    public abstract Book createBook(final Book book);

    @NamedQuery(Book.FIND_ALL)
    public abstract List<Book> findAllBooks();

    @Find
    public abstract Book findBook(Long id);

    @NamedQuery(Book.FIND_BY_TITLE)
    public abstract Book findBook(@QueryParam("title") final String title);

    @Merge
    public abstract Book updateBook(final Book book);

    @Remove
    public abstract void removeBook(final Book book);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return PersistenceHandler.invoke(em, method, args);
    }
}
