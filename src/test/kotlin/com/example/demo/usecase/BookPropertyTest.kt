package com.example.demo.usecase

import com.example.demo.book.domain.model.Book
import com.example.demo.book.domain.port.BookRepositoryPort
import com.example.demo.book.domain.usecase.BookUseCase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookPropertyTest : FunSpec({

	val mock = mockk<BookRepositoryPort>(relaxed = true)
	val useCase = BookUseCase(mock)

	beforeTest {
		clearMocks(mock)
	}

	test("should add any valid book without throwing") {
		val title = "Clean Code"
		val author = "Robert C. Martin"

		useCase.addBook(title, author)

		verify(exactly = 1) {
			mock.save(match {
				it.title == title && it.author == author
			})
		}
	}

	test("should add two books and verify both saves") {
		val title1 = "Clean Code"
		val author1 = "Robert C. Martin"

		val title2 = "Le Petit Prince"
		val author2 = "St Exupery"

		useCase.addBook(title1, author1)
		useCase.addBook(title2, author2)

		every { mock.findAll() } returns listOf(
			Book(title1, author1),
			Book(title2, author2)
		)

		val books = useCase.listBooksAlphabetically()

		books.shouldContainExactly(
			listOf(
				Book(title1, author1),
				Book(title2, author2)
			)
		)
	}

})
