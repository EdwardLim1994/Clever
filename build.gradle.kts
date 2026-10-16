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

val antlrGeneratedSources = layout.buildDirectory.dir("generated-src/antlr/cleverCompiler")
val generatedCleverSources = layout.buildDirectory.dir("generated/sources/clever")

sourceSets.main {
	antlr.setSrcDirs(emptyList<File>())
	java.srcDir(generatedCleverSources)
}

val cleverCompilerSourceSet = sourceSets.create("cleverCompiler") {
	compileClasspath += configurations.compileClasspath.get()
	runtimeClasspath += configurations.runtimeClasspath.get()
}

tasks.named<org.gradle.api.plugins.antlr.AntlrTask>("generateCleverCompilerGrammarSource") {
	outputDirectory = antlrGeneratedSources.get().asFile
}

sourceSets.test {
	compileClasspath += cleverCompilerSourceSet.output
	runtimeClasspath += cleverCompilerSourceSet.output
}

tasks.named<JavaCompile>(cleverCompilerSourceSet.compileJavaTaskName) {
	dependsOn(tasks.named("generateCleverCompilerGrammarSource"))
	options.annotationProcessorPath = configurations.annotationProcessor.get()
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
	implementation(libs.commons.text)
	implementation(libs.jsoup)
	implementation("org.springframework.boot:spring-boot-starter-web")
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

tasks.withType<JavaCompile>().configureEach {
	options.compilerArgs.add("-parameters")
}

val cleverSource = providers.gradleProperty("cleverSource").orElse("src/main/clever/HelloPage.clever")
val cleverPackage = providers.gradleProperty("cleverPackage").orElse("com.projectclever.clever.generated")
val generatedCleverJava = providers.gradleProperty("cleverOutput").orElse(
	generatedCleverSources.map { it.file("com/projectclever/clever/generated/HelloPage.java").asFile.absolutePath }
)

tasks.register<JavaExec>("generateCleverJava") {
	group = "build"
	description = "Generate Java source from one .clever template."
	dependsOn(tasks.named(cleverCompilerSourceSet.classesTaskName))
	classpath = cleverCompilerSourceSet.runtimeClasspath
	mainClass.set("com.projectclever.clever.compiler.CleverCompiler")
	doFirst {
		args(
			cleverSource.get(),
			cleverPackage.get(),
			generatedCleverJava.get()
		)
	}
}

tasks.named<JavaCompile>("compileJava") {
	dependsOn(tasks.named("generateCleverJava"))
}

tasks.register("compileCleverJava") {
	group = "build"
	description = "Generate and compile one .clever template with the application sources."
	dependsOn(tasks.named("compileJava"))
}

tasks.named<Test>("test") {
	dependsOn(tasks.named("compileCleverJava"))
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
	dependsOn(tasks.named("compileCleverJava"))
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	dependsOn(tasks.named("compileCleverJava"))
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
