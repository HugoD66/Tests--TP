import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
	java
	jacoco
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("info.solidsoft.pitest") version "1.15.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}


kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.test {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)

	reports {
		junitXml.required.set(true)
		html.required.set(true)
	}
}

jacoco {
	toolVersion = "0.8.10"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)

	reports {
		xml.required.set(true)
		html.required.set(true)
		csv.required.set(false)
	}
}

pitest {
	junit5PluginVersion.set("1.2.1")
	pitestVersion.set("1.15.0")

	targetClasses.set(listOf("com.example.demo.book.*"))
	targetTests.set(listOf("com.example.demo.usecase.*"))

	threads.set(4)
	outputFormats.set(listOf("HTML", "XML"))
	timestampedReports.set(false)
}

testing {
	suites {
		val testIntegration by registering(JvmTestSuite::class) {
			sources {
				kotlin {
					setSrcDirs(listOf("src/testIntegration/kotlin"))
				}
				compileClasspath += sourceSets.main.get().output
				runtimeClasspath += sourceSets.main.get().output
				resources {
					setSrcDirs(listOf("src/testIntegration/resources"))
				}
			}

			/*targets {
				all {
					testTask.configure {
						useJUnitPlatform()
						shouldRunAfter("test") // Garanti ordre d'execution
					}
				}
			}*/
		}
	}
}


val testIntegrationImplementation: Configuration by configurations.getting {
	extendsFrom(configurations.implementation.get())
}


dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	//Dépendances pour la DB
	implementation("org.liquibase:liquibase-core")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
	testImplementation("io.kotest:kotest-assertions-core:5.9.1")
	testImplementation("io.kotest:kotest-property:5.9.1")
	testImplementation("io.mockk:mockk:1.14.0")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	//Driven lecture en base ( DB )
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.postgresql:postgresql")

	//Drivin
	testIntegrationImplementation("io.mockk:mockk:1.13.8")
	testIntegrationImplementation("io.kotest:kotest-assertions-core:5.9.1")
	testIntegrationImplementation("io.kotest:kotest-runner-junit5:5.9.1")
	testIntegrationImplementation("com.ninja-squad:springmockk:4.0.2")
	testIntegrationImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
	testIntegrationImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}

	//Ajout des dépendance création Docker ( DB )
	testIntegrationImplementation("org.testcontainers:postgresql:1.19.1")
	testIntegrationImplementation("org.testcontainers:jdbc-test:1.12.0")
	testIntegrationImplementation("org.testcontainers:testcontainers:1.19.1")
	testIntegrationImplementation("io.kotest.extensions:kotest-extensions-testcontainers:2.0.2")
}


tasks.named("jacocoTestReport", JacocoReport::class) {
	dependsOn("test", "testIntegration") // Ordre d'execution

	executionData.setFrom(
		fileTree(buildDir).include(
			"/jacoco/test.exec",
			"/jacoco/testIntegration.exec"
		)
	)
}
