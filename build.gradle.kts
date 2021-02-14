import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    addScriptRepository()
    addScriptDependencies()
}

allprojects {
    addScriptRepository()

    tasks.withType(KotlinCompile::class).configureEach {
        kotlinOptions {
            // Set JVM target to 1.8
            jvmTarget = "1.8"
        }
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
