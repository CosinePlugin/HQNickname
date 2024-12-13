plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.7.7"
}

group = "kr.cosine.nickname"
version = "2.0.0"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper", "paper-api", "1.20.6-R0.1-SNAPSHOT")
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {
    test {
        useJUnitPlatform()
    }
    jar {
        archiveFileName.set("${rootProject.name}-${rootProject.version}.jar")
        destinationDirectory.set(file("D:\\서버\\1.20.6 - 개발\\plugins"))
    }
}