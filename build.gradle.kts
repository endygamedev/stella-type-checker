import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.ebronnikov.typechecker"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.antlr:antlr4-runtime:4.7.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveFileName.set("stella-type-checker.jar")
        manifest {
            attributes(
                "Main-Class" to "dev.ebronnikov.typechecker.Main"
            )
        }
        mergeServiceFiles()
    }

    named<Jar>("jar") {
        enabled = false
    }
}
