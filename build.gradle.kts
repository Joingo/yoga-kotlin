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
    jvm() {
        //sourceCompatibility = JavaVersion.VERSION_1_8
        //targetCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets {
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

/*publishing {
    repositories {
        val lightCraftRepoDir = project.findProperty("lightcraft.repo.location")
        if (lightCraftRepoDir != null) {
            maven {
                name = "LightCraftRepo"
                url = File(lightCraftRepoDir.toString()).toURI()
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}*/