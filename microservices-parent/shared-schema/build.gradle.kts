plugins {
    java
    id("java-library")
    id("com.github.davidmc24.gradle.plugin.avro") version "1.5.0"
}

group = "ca.gbc"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.avro:avro:1.12.0")

}

//avro{
//    isCreateSetters=true
//    fieldVisibility="PRIVATE"
//    isEnabledDecimalLogicalType=true
//}



