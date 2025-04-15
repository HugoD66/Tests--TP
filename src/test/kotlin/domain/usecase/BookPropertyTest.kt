package domain.usecase

import book.domain.model.Book
import book.domain.port.BookRepositoryPort
import book.domain.usecase.BookUseCase
import io.kotest.core.spec.style.FunSpec
import io.mockk.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookPropertyTest : FunSpec({
	val mock = mockk<BookRepositoryPort>(relaxed = true)
	val useCase = BookUseCase(mock)

	test("should add any valid book without throwing") {
		var title = "Clean Code"
		var author = "Robert C. Martin"
		var bookCreated = Book(title, author)


		useCase.addBook(title, author)

		verify(exactly = 1) { mock.save(bookCreated) }
	}

	test("should throw exception when book title is empty") {
		var titleOne = "Clean Code"
		var authorOne = "Robert C. Martin"
		val bookOne = Book(titleOne, authorOne)

		var titleTwo = "Le Petit Prince"
		var authorTwo = "St Exupery"
		val bookTwo = Book(titleTwo, authorTwo)

		every { mock.save(bookOne) } just runs
		every { mock.save(bookTwo) } just runs

		verify { mock.save(bookOne) }
		verify { mock.save(bookTwo) }
	}
})
