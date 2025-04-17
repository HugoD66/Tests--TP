package com.example.demo.book.domain.port

import com.example.demo.book.domain.model.Book

interface BookRepositoryPort {
    fun save(book: Book)
    fun findAll(): List<Book>
}
