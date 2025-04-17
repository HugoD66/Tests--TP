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
            INSERT INTO book (title, author)
            VALUES (:title, :author)
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("title", book.title)
            .addValue("author", book.author)

        jdbcTemplate.update(sql, params)
    }

    override fun findAll(): List<Book> {
        val sql = "SELECT title, author FROM book"

        return jdbcTemplate.query(sql, BookRowMapper())
    }

    class BookRowMapper : RowMapper<Book> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Book {
            return Book(
                title = rs.getString("title"),
                author = rs.getString("author")
            )
        }
    }
}

