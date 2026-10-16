plugins {
	java
	antlr
	id("org.sonarqube") version "7.2.3.7755"
	id("org.springframework.boot") version "4.1.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.projectclever"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	antlr("org.antlr:antlr4:4.13.2")
	implementation("org.antlr:antlr4-runtime:4.13.2")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

sonar {
	properties {
		property("sonar.projectKey", "EdwardLim1994_Clever")
		property("sonar.qualitygate.wait", "true")
		providers.environmentVariable("SONAR_ORGANIZATION").orNull
			?.takeIf { it.isNotBlank() }
			?.let { property("sonar.organization", it) }
	}
}
