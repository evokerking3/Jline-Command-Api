/*
 * This is a library subproject for the command API.
 */

plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.36.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // Expose these to consumers of the API
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

// ============ Maven Publishing Configuration ============ \\

mavenPublishing {
    coordinates(
        groupId = "dev.evokerking",
        artifactId = "evoker-api",
        version = "1.0.0"
    )
    pom {
        name.set("Evoker API")
        description.set("A set of lightweight utilities.")
        inceptionYear.set("2026")
        url.set("https://github.com/evokerking3/command-api")
        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
                distribution.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("evokerking")
                name.set("EvokerKing")
                email.set("me@evokerking.dev")
                url.set("https://github.com/evokerking3")
            }
        }
        scm {
            url.set("https://github.com/evokerking3/command-api")
            connection.set("scm:git:git://github.com/evokerking3/command-api.git")
            developerConnection.set("scm:git:ssh://git@github.com/evokerking3/command-api.git")
        }
    }

    repositories {
        maven {
            name = "Evokerking's Maven Repository"
            url = uri(
                if (version.toString().endsWith("SNAPSHOT"))
                    "http://gb-lon03.altr.cc:25237/snapshots"
                else
                    "http://gb-lon03.altr.cc:25237/releases"
            )
            credentials {
                username = providers.gradleProperty("evokerkingRepoUsername").getOrNull()
                password = providers.gradleProperty("evokerkingRepoPassword").getOrNull()
            }
        }
    }


    signAllPublications()
}