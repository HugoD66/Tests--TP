package com.example.demo.infrastructure.driven.postgres

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookRepositoryPort
import org.springframework.stereotype.Service

@Service
class BookDAO : BookRepositoryPort {

    private val books = mutableListOf<Book>()

    override fun save(book: Book) {
        books.add(book)
    }

    override fun findAll(): List<Book> {
        return books.toList()
    }
}
