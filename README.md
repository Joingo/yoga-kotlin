# Yoga Layout / Flexbox

A pure Kotlin Multiplatform CSS flexbox implementation.

Code and unit tests translated from the original C source code of [Yoga Layout by Facebook](https://github.com/facebook/yoga).

Supports JVM, Android, JS, iOS targets and is usable in common source set.

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
