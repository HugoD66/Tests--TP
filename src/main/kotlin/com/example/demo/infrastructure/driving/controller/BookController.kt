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
        useCase.listBooksAlphabetically().map { BookDTO(it.title, it.author, it.isReserved) }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody dto: BookDTO) {
        useCase.addBook(dto.title, dto.author, dto.isReserved)
    }

    @PostMapping("/reserve")
    fun reserveBook(@RequestBody dto: BookDTO): String {
        val reserved = useCase.reserveBook(dto.title, dto.author)
        return if (reserved) {
            "Le livre a bien été réservé."
        } else {
            "Le livre est déjà réservé."
        }
    }

}
