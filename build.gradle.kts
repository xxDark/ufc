plugins {
    `java-library`
    id("java")
}

group = "dev.xdark"
version = "1.0"

repositories.mavenCentral()

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

tasks.test {
    useJUnitPlatform()
}