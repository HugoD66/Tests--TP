package com.example.demo.domain.usecase

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookRepositoryPort

class BookUseCase(private val repository: BookRepositoryPort) {

    fun addBook(title: String, author: String, isReserved: Boolean) {
        require(title.isNotBlank()) { "Le titre ne peut pas être vide" }
        require(author.isNotBlank()) { "L'auteur ne peut pas être vide" }
        //Valeur par défaut pour isReserved, si non précisé

        val book = Book(title, author, isReserved)
        repository.save(book)
    }

    fun listBooksAlphabetically(): List<Book> {
        return repository.findAll().sortedBy { it.title.lowercase() }
    }

    fun reserveBook(title: String, author: String): Boolean {
        require(title.isNotBlank()) { "Le titre ne peut pas être vide" }
        require(author.isNotBlank()) { "L'auteur ne peut pas être vide" }

        return repository.reserveBook(title, author)
    }

}
