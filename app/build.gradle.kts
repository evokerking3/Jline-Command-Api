/*
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/9.2.1/userguide/building_java_projects.html in the Gradle documentation.
 *
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit test framework.
    testImplementation(libs.junit)

    // Project dependencies
    implementation(project(":command-api"))

    // This dependency is used by the application.
    implementation(libs.guava)
    implementation("org.jline:jline:3.30.0")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("net.sf.jopt-simple:jopt-simple:6.0-alpha-3")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "dev.evokerking.App"
}

tasks.jar {
    dependsOn(":command-api:jar")
    manifest {
        attributes["Main-Class"] = "dev.evokerking.App"
    }
    from(sourceSets.main.get().output)
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    archiveFileName.set("app.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
