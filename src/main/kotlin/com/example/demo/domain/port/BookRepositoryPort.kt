package com.example.demo.domain.port

import com.example.demo.domain.model.Book

interface BookRepositoryPort {
    fun save(book: Book)
    fun findAll(): List<Book>
}
