package com.example.demo.infrastructure.driven.postgres

import com.example.demo.domain.model.Book
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Testcontainers
@SpringBootTest
class BookDAOTest {

    companion object {
        @Container
        val postgres = PostgreSQLContainer<Nothing>("postgres:15").apply {
            withDatabaseName("testdb")
            withUsername("testuser")
            withPassword("testpass")
        }

        @JvmStatic
        @DynamicPropertySource
        fun overrideProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

    @Autowired
    lateinit var bookDAO: BookDAO

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @BeforeEach
    fun cleanDb() {
        jdbcTemplate.update("DELETE FROM book", mapOf<String, Any>())
    }

    @Test
    fun `should save and retrieve a book`() {
        val book = Book("1984", "George Orwell", isReserved = true)
        bookDAO.save(book)

        val result = bookDAO.findAll()

        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("1984")
        assertThat(result[0].author).isEqualTo("George Orwell")
        assertThat(result[0].isReserved).isTrue()
    }

    @Test
    fun `should retrieve empty list when no books`() {
        val result = bookDAO.findAll()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should save and retrieve multiple books`() {
        val books = listOf(
            Book("Dune", "Frank Herbert", isReserved = false),
            Book("Le Petit Prince", "Antoine de Saint-Exupéry", isReserved = true),
            Book("1984", "George Orwell", isReserved = false)
        )
        books.forEach { bookDAO.save(it) }

        val result = bookDAO.findAll()

        assertThat(result).hasSize(3)
        assertThat(result).anyMatch { it.title == "Le Petit Prince" && it.isReserved }
        assertThat(result).anyMatch { it.title == "1984" && !it.isReserved }
        assertThat(result).anyMatch { it.title == "Dune" && !it.isReserved }
    }

    @Test
    fun `should reserve a book successfully`() {
        val book = Book("1984", "George Orwell", isReserved = false)
        bookDAO.save(book)

        val reserved = bookDAO.reserveBook("1984", "George Orwell")
        val result = bookDAO.findAll().first()

        assertThat(reserved).isTrue()
        assertThat(result.isReserved).isTrue()
    }

    @Test
    fun `should not reserve a book already reserved`() {
        val book = Book("1984", "George Orwell", isReserved = true)
        bookDAO.save(book)

        val reserved = bookDAO.reserveBook("1984", "George Orwell")

        assertThat(reserved).isFalse()
    }
}
