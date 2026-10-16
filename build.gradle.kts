plugins {
	java
	antlr
	alias(libs.plugins.sonarqube)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency.management)
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

dependencyLocking {
	lockAllConfigurations()
}

dependencies {
	antlr(libs.antlr)
	implementation(libs.antlr.runtime)
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
