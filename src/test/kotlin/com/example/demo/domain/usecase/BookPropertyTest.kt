package com.example.demo.domain.usecase

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookRepositoryPort
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
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

		useCase.addBook(title, author, isReserved = false)

		verify(exactly = 1) {
			mock.save(match {
                it.title == title && it.author == author && !it.isReserved
			})
		}
	}

	test("should add two books and verify both saves") {
		val title1 = "Clean Code"
		val author1 = "Robert C. Martin"

		val title2 = "Le Petit Prince"
		val author2 = "St Exupery"

		useCase.addBook(title1, author1, isReserved = false)
		useCase.addBook(title2, author2, isReserved = false)

		every { mock.findAll() } returns listOf(
			Book(title1, author1, isReserved = false),
			Book(title2, author2, isReserved = false)
		)

		val books = useCase.listBooksAlphabetically()

		books.shouldContainExactly(
			listOf(
				Book(title1, author1, isReserved = false),
				Book(title2, author2, isReserved = false)
			)
		)
	}

	test("should reserve a book if not already reserved") {
		val title = "1984"
		val author = "George Orwell"

		//En dessous comparable à : val book = Book(..., isReserved = true)
		every { mock.reserveBook(title, author) } returns true

		val result = useCase.reserveBook(title, author)

		result shouldBe true
		verify { mock.reserveBook(title, author) }
	}

	test("should not reserve a book if already reserved") {
		val title = "1984"
		val author = "George Orwell"

		every { mock.reserveBook(title, author) } returns false

		val result = useCase.reserveBook(title, author)

		result shouldBe false
		verify { mock.reserveBook(title, author) }
	}

})
