package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Tp1Application

fun main(args: Array<String>) {
	runApplication<Tp1Application>(*args)
}

var alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

fun cipher(char: String, key: Number): String {
	passMultipleControls(char, key)

	val oldIndex = alphabet.indexOf(char[0])

	val newIndex = (oldIndex + key.toInt()) % alphabet.length

	return alphabet[newIndex].toString()
}

fun passMultipleControls(char: String, key: Number) {
	if (char.length != 1) {
		throw IllegalArgumentException("test must be a single character")
	}

	if (!char[0].isUpperCase()) {
		throw IllegalArgumentException("test must be an uppercase letter")
	}

	if (key.toInt() < 0) {
		throw IllegalArgumentException("key must be a positive number")
	}
}
