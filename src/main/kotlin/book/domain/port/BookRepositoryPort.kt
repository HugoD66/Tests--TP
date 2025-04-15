package book.domain.port

import book.domain.model.Book

interface BookRepositoryPort {
    fun save(book: Book)
    fun findAll(): List<Book>
}
