plugins {
    java
    id("com.gradleup.shadow") version "8.3.0"
}

group = "dev.ebronnikov.typechecker"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.antlr:antlr4-runtime:4.13.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    archiveFileName.set("stella-type-checker.jar")
    manifest {
        attributes(
            "Main-Class" to "dev.ebronnikov.typechecker.Main"
        )
    }
    mergeServiceFiles()
}
