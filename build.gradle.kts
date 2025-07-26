plugins {
    `java-library`
    id("java")
}

group = "dev.xdark"
version = "1.0"

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}
