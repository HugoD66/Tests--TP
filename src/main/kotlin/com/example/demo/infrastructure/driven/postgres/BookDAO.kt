package com.example.demo.infrastructure.driven.postgres

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookRepositoryPort
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class BookDAO(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : BookRepositoryPort {

    override fun save(book: Book) {
        val sql = """
            INSERT INTO book (title, author, is_reserved)
            VALUES (:title, :author, :isReserved)
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("title", book.title)
            .addValue("author", book.author)
            .addValue("isReserved", book.isReserved)

        jdbcTemplate.update(sql, params)
    }

    override fun findAll(): List<Book> {
        val sql = "SELECT title, author, is_reserved FROM book"

        return jdbcTemplate.query(sql, BookRowMapper())
    }

    override fun isBookReserved(title: String, author: String): Boolean {
        val sql = """
        SELECT is_reserved FROM book 
        WHERE title = :title AND author = :author
    """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("title", title)
            .addValue("author", author)

        return jdbcTemplate.queryForObject(sql, params, Boolean::class.java) ?: false
    }

    override fun reserveBook(title: String, author: String): Boolean {
        if (isBookReserved(title, author)) return false

        val sql = """
        UPDATE book SET is_reserved = true 
        WHERE title = :title AND author = :author
    """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("title", title)
            .addValue("author", author)

        return jdbcTemplate.update(sql, params) > 0
    }


    class BookRowMapper : RowMapper<Book> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Book {
            return Book(
                title = rs.getString("title"),
                author = rs.getString("author"),
                isReserved = rs.getBoolean("is_reserved")
            )
        }
    }
}

