import dependencies.Dep
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.dsl.ScriptHandler
import org.gradle.kotlin.dsl.ScriptHandlerScope
import org.gradle.kotlin.dsl.repositories

// buildscript
fun ScriptHandler.addScriptRepository() {
    repositories {
        addScriptDependencies()
    }
}

// allprojects
fun Project.addScriptRepository() {
    repositories {
        addScriptDependencies()
    }
}

private fun RepositoryHandler.addScriptDependencies() {
    google {
        content {
            includeGroupByRegex("com.android.*")
            includeGroupByRegex("androidx.*")
            includeGroupByRegex("com.google.*")
        }
    }
    mavenCentral()
    jcenter {
        content {
            includeModule("org.jetbrains.trove4j", "trove4j")
        }
    }
}

fun ScriptHandlerScope.addScriptDependencies() {
    dependencies {
        classpath(Dep.GradlePlugin.android)
        classpath(Dep.GradlePlugin.kotlin)
    }
}
