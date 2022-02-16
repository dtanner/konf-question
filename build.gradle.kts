plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
}

repositories {
    mavenCentral()
}

val targetJvm = 11
tasks {
    compileJava { options.release.set(targetJvm) }
    compileKotlin { kotlinOptions { jvmTarget = "$targetJvm" } }
    compileTestKotlin { kotlinOptions { jvmTarget = "$targetJvm" } }

    withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.2.10")
    implementation("net.logstash.logback:logstash-logback-encoder:7.0.1") {
        exclude(group = "com.fasterxml.jackson.core")
    }
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")
    implementation("org.slf4j:slf4j-api:1.7.35")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("io.kotest:kotest-assertions-core:5.1.0")

    implementation("com.uchuhimo:konf:1.1.2")

}

