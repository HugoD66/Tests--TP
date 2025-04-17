package com.example.demo.book.infrastructure.application

import com.example.demo.book.domain.port.BookRepositoryPort
import com.example.demo.book.domain.usecase.BookUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCasesConfiguration {
    @Bean
    fun myUseCase(bookRepositoryPort: BookRepositoryPort): BookUseCase {
        return BookUseCase(bookRepositoryPort)
    }
}