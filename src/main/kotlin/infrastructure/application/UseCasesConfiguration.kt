package infrastructure.application

import book.domain.usecase.BookUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCasesConfiguration {
    @Bean
    fun myUseCase(dependancy: MyDependency): BookUseCase {
        return BookUseCase(dependency)
    }
}