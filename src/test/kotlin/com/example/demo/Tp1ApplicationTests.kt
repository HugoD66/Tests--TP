package com.example.demo

import com.example.demo.book.cipher
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int
import io.kotest.property.forAll
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class Tp1ApplicationTests : FunSpec({

	context("cipher input validation") {
		test("should throw if char is not a single character") {
			val exception = shouldThrow<IllegalArgumentException> {
                cipher("AB", 2)
			}
			exception.message shouldBe "test must be a single character"
		}

		test("should throw if char is not uppercase") {
			val exception = shouldThrow<IllegalArgumentException> {
                cipher("a", 2)
			}
			exception.message shouldBe "test must be an uppercase letter"
		}

		test("should throw if key is negative") {
			val exception = shouldThrow<IllegalArgumentException> {
                cipher("A", -2)
			}
			exception.message shouldBe "key must be a positive number"
		}
	}

	context("cipher encryption") {
		val testCases = listOf(
			Triple("A", 2, "C"),
			Triple("A", 5, "F"),
			Triple("Z", 1, "A"),
			Triple("A", 27, "B"),
		)

		testCases.forEach { (char, key, expected) ->
			test("should return $expected when input is $char and key is $key") {
				cipher(char, key) shouldBe expected
			}
		}
	}

	test("Cipher with key 0 return alaways the same letter") {
		val uppercaseLetters = ('A'..'Z').map { it.toString() }

		forAll(Arb.element(uppercaseLetters)) { char ->
			cipher(char, 0) == char
		}
	}

	test("Cipher with key 26 return alaways the same letter") {
		val uppercaseLetters = ('A'..'Z').map { it.toString() }

		forAll(Arb.element(uppercaseLetters)) { char ->
			cipher(char, 26) == char
		}
	}

	test("Cipher alaways retuen an uppercase Char") {
		val uppercaseLetters = ('A'..'Z').map { it.toString() }

		forAll(Arb.element(uppercaseLetters), Arb.int(0..1000)) { char, key ->
			val result = cipher(char, key)
			result.length == 1 && result[0].isUpperCase()
		}
	}
})
