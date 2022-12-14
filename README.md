# Kotlin Yoga Layout

A pure Kotlin Multiplatform port of [Yoga Layout by Facebook](https://github.com/facebook/yoga).

Supports JVM, Android, JS, iOS targets via common source set.

---

### Usage

1. Add the repository to your Gradle project 
```kotlin
repositories {
    maven("https://maven.pkg.github.com/Joingo/yoga-kotlin")
}
```
2. Add the dependency to your project
````kotlin
kotlin {
    commonMain {
        dependencies {
            implementation("com.joingo.yoga:yoga-kotlin:1.0.0")
        }
    }
}
````
3. You're ready to go!