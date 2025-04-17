package com.example.demo.domain.usecase

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookRepositoryPort

class BookUseCase(private val repository: BookRepositoryPort) {

    fun addBook(title: String, author: String) {
        require(title.isNotBlank()) { "Le titre ne peut pas être vide" }
        require(author.isNotBlank()) { "L'auteur ne peut pas être vide" }

        val book = Book(title, author)
        repository.save(book)
    }

    fun listBooksAlphabetically(): List<Book> {
        return repository.findAll().sortedBy { it.title.lowercase() }
    }
}
