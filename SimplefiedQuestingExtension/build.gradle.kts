plugins {
    kotlin("jvm") version "2.0.21"
    id("com.typewritermc.module-plugin") version "1.3.0"
}
repositories {}
dependencies {
    implementation("com.typewritermc:QuestExtension:0.9.0")
}

typewriter {
    namespace = "martenmrfcyt"

    extension {
        name = "SimplifiedQuesting"
        shortDescription = "An extension that makes making questing easier!"
        description = "This extension adds a bunch of new features to the questing system, making it easier to create simple quests."
        engineVersion = "0.9.0-beta-161"
        channel = com.typewritermc.moduleplugin.ReleaseChannel.BETA
        dependencies {
            dependency("typewritermc", "Quest")
        }
        paper()

    }

}

kotlin {
    jvmToolchain(21)
}