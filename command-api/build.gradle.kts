/*
 * This is a library subproject for the command API.
 */

plugins {
    `java-library`
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // Expose these to consumers of the API
    api("org.jline:jline:3.30.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withJavadocJar()
    withSourcesJar()
}
tasks.named<Test>("test") {
    useJUnitPlatform()
}

// ============ Maven Publishing Configuration ============

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "dev.evokerking"
            artifactId = "command-api"
            version = "1.0.0"
            from(components["java"])

            pom {
                name.set("Command API")
                description.set("A lightweight command framework with autocomplete support for building interactive CLI applications")
                url.set("https://github.com/EvokerKing/command-api")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                
                developers {
                    developer {
                        id.set("evokerking")
                        name.set("EvokerKing")
                        email.set("dev@evokerking.dev")
                    }
                }
                
                scm {
                    connection.set("scm:git:https://github.com/EvokerKing/command-api.git")
                    developerConnection.set("scm:git:ssh://git@github.com/EvokerKing/command-api.git")
                    url.set("https://github.com/EvokerKing/command-api")
                }
            }
        }
    }

    repositories {
        maven {
            name = "MavenCentral"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            
            credentials {
                username = System.getenv("MAVEN_USERNAME") ?: ""
                password = System.getenv("MAVEN_PASSWORD") ?: ""
            }
        }
        
        // Optional: Local repository for testing
        maven {
            name = "LocalTest"
            url = uri(layout.buildDirectory.dir("repos/releases"))
        }
    }
}

// ============ Signing Configuration ============

val isPublishingToMavenCentral = System.getenv("MAVEN_USERNAME") != null

signing {
    // Only sign when publishing to Maven Central (not for local testing)
    isRequired = isPublishingToMavenCentral
    
    if (isPublishingToMavenCentral) {
        sign(publishing.publications["mavenJava"])
        
        // Use environment variables or properties for signing configuration
        val signingKey = System.getenv("GPG_SIGNING_KEY") ?: ""
        val signingPassword = System.getenv("GPG_SIGNING_PASSWORD") ?: ""
        
        if (signingKey.isNotBlank() && signingPassword.isNotBlank()) {
            useInMemoryPgpKeys(signingKey, signingPassword)
        } else {
            useGpgCmd()
        }
    }
}