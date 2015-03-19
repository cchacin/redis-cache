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
package com.groupon.getaways.presentation;

import com.groupon.getaways.persistence.BookDbService;
import com.groupon.getaways.entities.Book;
import com.groupon.getaways.persistence.BookRedisService;
import lombok.Data;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Data
@Named
public class BookBean {

    @Inject
    private BookDbService bookService;
    @Inject
    private BookRedisService bookRedisService;
    private List<Book> booksAvailable;
    private String bookTitle;

    public String fetchBooks() {
        booksAvailable = bookService.findAllBooks();
        return "success";
    }

    public String add() {
        Book book = new Book();
        book.setBookTitle(bookTitle);
        bookRedisService.createBook(bookService.createBook(book));
        return "success";
    }
}
