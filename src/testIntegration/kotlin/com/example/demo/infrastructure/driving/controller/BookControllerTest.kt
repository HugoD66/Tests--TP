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
            Book("Alpha", "Author A"),
            Book("Beta", "Author B")
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
        val payload = """{ "title": "New Book", "author": "New Author" }"""

        every { useCase.addBook("New Book", "New Author") } returns Unit

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isCreated() }
        }

        verify(exactly = 1) { useCase.addBook("New Book", "New Author") }
    }

    //Fails :
    @Test
    fun `should return 400 when POST payload is invalid`() {
        val payload = """{ "title": "", "author": "Author X" }"""

        every { useCase.addBook(any(), any()) } throws IllegalArgumentException("Le titre ne peut pas être vide")

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isBadRequest() }
        }

        verify(exactly = 1) { useCase.addBook("", "Author X") }
    }

    @Test
    fun `should return 500 when use case throws unexpected exception`() {
        val payload = """{ "title": "Valid Title", "author": "Author Y" }"""

        every { useCase.addBook(any(), any()) } throws RuntimeException("Database error")

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isInternalServerError() }
        }

        verify(exactly = 1) { useCase.addBook("Valid Title", "Author Y") }
    }

}