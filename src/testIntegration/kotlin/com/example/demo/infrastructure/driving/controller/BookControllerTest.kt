package com.example.demo.infrastructure.driving.controller

import com.example.demo.domain.usecase.BookUseCase
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import com.ninjasquad.springmockk.MockkBean
import com.example.demo.domain.model.Book

@WebMvcTest(BookController::class)
class BookControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var useCase: BookUseCase

    @Test
    fun `should return books when GET is called`() {
        every { useCase.listBooksAlphabetically() } returns listOf(
            Book("Alpha", "Author A", isReserved = false),
            Book("Beta", "Author B", isReserved = true)
        )

        mockMvc.get("/books")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.size()") { value(2) }
                jsonPath("$[0].title") { value("Alpha") }
            }

        verify(exactly = 1) { useCase.listBooksAlphabetically() }
    }

    @Test
    fun `should create a book when POST is called`() {
        val payload = """{ "title": "New Book", "author": "New Author", "isReserved": false }"""

        every { useCase.addBook("New Book", "New Author", false) } returns Unit

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isCreated() }
        }

        verify(exactly = 1) { useCase.addBook("New Book", "New Author", false) }
    }

    @Test
    fun `should reserve a book if not already reserved`() {
        val payload = """{ "title": "Book 1", "author": "Author 1", "isReserved": false }"""

        every { useCase.reserveBook("Book 1", "Author 1") } returns true

        mockMvc.post("/books/reserve") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isOk() }
            content { string("Le livre a bien été réservé.") }
        }

        verify(exactly = 1) { useCase.reserveBook("Book 1", "Author 1") }
    }

    @Test
    fun `should return message if book already reserved`() {
        val payload = """{ "title": "Book 1", "author": "Author 1", "isReserved": true }"""

        every { useCase.reserveBook("Book 1", "Author 1") } returns false

        mockMvc.post("/books/reserve") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isOk() }
            content { string("Le livre est déjà réservé.") }
        }

        verify(exactly = 1) { useCase.reserveBook("Book 1", "Author 1") }
    }



    //Fails :
    @Test
    fun `should return 400 when POST payload is invalid`() {
        val payload = """{ "title": "", "author": "Author X", "isReserved": false }"""

        every { useCase.addBook(any(), any(), false) } throws IllegalArgumentException("Le titre ne peut pas être vide")

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isBadRequest() }
        }

        verify(exactly = 1) { useCase.addBook("", "Author X", false) }
    }

    @Test
    fun `should return 500 when use case throws unexpected exception`() {
        val payload = """{ "title": "Valid Title", "author": "Author Y" }"""

        every { useCase.addBook(any(), any(), false) } throws RuntimeException("Database error")

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isInternalServerError() }
        }

        verify(exactly = 1) { useCase.addBook("Valid Title", "Author Y", false) }
    }

    //A voir
    @Test
    fun `should fallback to default reservation value if isReserved is missing`() {
        val payload = """{ "title": "Book X", "author": "Author X" }"""

        every { useCase.addBook("Book X", "Author X", false) } returns Unit

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isCreated() }
        }

        verify { useCase.addBook("Book X", "Author X", false) }
    }

}