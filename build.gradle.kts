plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.diffplug.spotless") version "7.0.0.BETA1"
	id("org.sonarqube") version "6.0.1.5171"
}

group = "com.github.renas"
version = "0.0.1-SNAPSHOT"
val springDependencies = "3.3.1"
val mysqlConnector = "8.0.33"
val hibernate = "8.0.1.Final"
val junit = "5.10.3"
val palantirJavaFormat = "2.47.0"

val assertJ = "3.26.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(platform("org.springframework.boot:spring-boot-dependencies:$springDependencies"))
	implementation("mysql:mysql-connector-java:$mysqlConnector")
	implementation("org.hibernate.validator:hibernate-validator:$hibernate")
	implementation("org.hibernate:hibernate-validator:$hibernate")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-mail")

	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.assertj:assertj-core:$assertJ")
	testImplementation("org.junit.jupiter:junit-jupiter:$junit")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
tasks.test {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.bootBuildImage {
	imageName = "renasustek/mind-map-backend"
}

tasks.jacocoTestReport {
	reports {
		xml.required = true
	}
	dependsOn(tasks.test)
}
spotless {
	ratchetFrom("origin/main")
	java {
		toggleOffOn()
		palantirJavaFormat(palantirJavaFormat).formatJavadoc(true)
		removeUnusedImports()
		trimTrailingWhitespace()
		endWithNewline()
	}
}


sonar {
	properties {
		property("sonar.projectKey", "renasustek_MindMapBackend")
		property("sonar.organization", "renasustek")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}

tasks.sonar {
	dependsOn(tasks.check)
}
