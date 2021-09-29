plugins {
    kotlin("jvm") version "1.5.21"
    id("java-gradle-plugin")
    id("maven-publish")
}

group = "com.aliucord"
version = "0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
    google()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib", kotlin.coreLibrariesVersion))
    compileOnly(gradleApi())

    implementation("com.google.code.gson:gson:2.8.8")
    implementation("commons-codec:commons-codec:1.15")
    implementation("de.skuzzle:semantic-version:2.1.0")
}

gradlePlugin {
    plugins {
        create("com.aliucord.repoer") {
            id = "com.aliucord.repoer"
            implementationClass = "com.aliucord.repoer.RepoerPlugin"
        }
    }
}