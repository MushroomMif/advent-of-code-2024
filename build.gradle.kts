plugins {
    kotlin("jvm") version "2.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    fun ktor(module: String) {
        implementation("io.ktor:ktor-$module:3.0.2")
    }

    ktor("client-core")
    ktor("client-cio")
    implementation("io.github.classgraph:classgraph:4.8.179")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.2")
}
