plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.7.1"
}

group = "com.hetu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Configure IntelliJ Platform Gradle Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    intellijPlatform {
        create("IC", "2025.1.4.1")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)

        // Add necessary plugin dependencies for compilation here, example:
        // bundledPlugin("com.intellij.java")
        bundledPlugin("com.intellij.java")
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.intellij:annotations:+@jar") // sometimes helps
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "251"
        }

        changeNotes = """
            Initial version
        """.trimIndent()
    }
}

sourceSets {
    main {
        java {
            srcDirs("src/gen/java") // 👈 add generated Java sources
        }
        kotlin {
            srcDirs("src/main/kotlin")
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    
    // NOTE: JFlex generation is disabled because we're using a custom Kotlin lexer implementation
    // The generated lexer had conflicts with our custom implementation
    /*
    val generateLexer by registering(Exec::class) {
        group = "generate"
        description = "Generates lexer from .flex files"

        notCompatibleWithConfigurationCache("Uses system JFlex installation")

        // Use java -jar with our combined JFlex JAR
        commandLine = listOf("java", "-jar", "lib/jflex-combined.jar", "--outdir", "src/gen/java/com/hetu/hetuscript", "src/main/antlr/com/hetu/hetuscript/HetuLexer.flex")

        doFirst {
            file("src/gen/java/com/hetu/hetuscript").mkdirs()
        }
    }
    */
}