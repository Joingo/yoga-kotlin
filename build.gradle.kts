plugins {
    kotlin("multiplatform") version "1.7.22"
    `maven-publish`
}

group = "com.joingo.yoga"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js(IR).browser()
    ios()
    iosSimulatorArm64()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        val jvmMain by getting {
            dependencies {
                compileOnly("org.jetbrains:annotations:20.1.0")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
            }
        }
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven("https://maven.pkg.github.com/Joingo/yoga-kotlin") {
            name = "GitHubPackages"
            credentials {
                username =
                    (project.findProperty("gpr.user") ?: System.getenv("GPR_USER")) as? String
                password =
                    (project.findProperty("gpr.key") ?: System.getenv("GPR_API_KEY")) as? String
            }
        }
    }
}