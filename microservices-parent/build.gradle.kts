plugins {
    java
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
}
/*
* GradleProjectSettings>
        <option name="externalProjectPath" value="$PROJECT_DIR$/order-service" />
        <option name="gradleHome" value="$PROJECT_DIR$/../../../../Gradle/gradle-8.10-bin/gradle-8.10" />
        <option name="modules">
          <set>
            <option value="$PROJECT_DIR$/order-service" />
          </set>
        </option>
      </GradleProjectSettings>*/

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
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
