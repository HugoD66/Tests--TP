package com.example.demo.infrastructure.driving.controller
import com.example.demo.infrastructure.driving.dto.BookDTO

import com.example.demo.domain.usecase.BookUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(
    private val useCase: BookUseCase
) {

    @GetMapping
    fun getAll(): List<BookDTO> =
        useCase.listBooksAlphabetically().map { BookDTO(it.title, it.author) }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody dto: BookDTO) {
        useCase.addBook(dto.title, dto.author)
    }
}
