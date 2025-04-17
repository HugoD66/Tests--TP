package com.example.demo.infrastructure.application

import com.example.demo.domain.port.BookRepositoryPort
import com.example.demo.domain.usecase.BookUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCasesConfiguration {
    @Bean
    fun myUseCase(bookRepositoryPort: BookRepositoryPort): BookUseCase {
        return BookUseCase(bookRepositoryPort)
    }
}