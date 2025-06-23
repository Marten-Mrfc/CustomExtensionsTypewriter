import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.0.21"
    id("io.github.goooler.shadow") version "8.1.7" apply false
    id("com.typewritermc.module-plugin") version "1.3.0" apply false
    `maven-publish`
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")

    repositories {
        // Required
        mavenCentral()
    }

    val targetJavaVersion = 21
    java {
        val javaVersion = JavaVersion.toVersion(targetJavaVersion)
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
    kotlin {
        jvmToolchain(targetJavaVersion)
    }
}


subprojects {
    group = "dev.marten_mrfcyt"
    version = "0.9.0-beta-156"
    apply(plugin = "io.github.goooler.shadow")
    apply(plugin = "com.typewritermc.module-plugin")

    tasks.withType<ShadowJar> {
        exclude("kotlin/**")
        exclude("org/intellij/**")
        exclude("org/jetbrains/**")
        exclude("META-INF/maven/**")
    }

    if (!project.name.startsWith("_")) {
        task<ShadowJar>("buildAndMove") {
            from(tasks.named("shadowJar"))
            group = "build"
            description = "Builds the jar and moves it to the server folder"
            outputs.upToDateWhen { false }

            archiveFileName = "${project.name}.${archiveExtension.get()}"
            destinationDirectory = file("../server/plugins/Typewriter/extensions")
        }
    }
}