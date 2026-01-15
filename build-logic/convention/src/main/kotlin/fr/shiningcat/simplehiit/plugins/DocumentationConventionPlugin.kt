/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

/**
 * Convention plugin for documentation tasks
 */
class DocumentationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Task to generate unified module dependency graph (mobile + TV)
            tasks.register("generateUnifiedDependencyGraph", GenerateUnifiedDependencyGraphTask::class.java)
        }
    }
}

abstract class GenerateUnifiedDependencyGraphTask : DefaultTask() {
    @get:Inject
    abstract val execOperations: ExecOperations

    @get:OutputDirectory
    abstract val reportsDir: DirectoryProperty

    init {
        group = "documentation"
        description = "Generates a unified dependency graph combining both mobile and TV apps"
        reportsDir.set(project.layout.buildDirectory.dir("reports"))

        // Disable configuration cache for this task as it uses exec
        notCompatibleWithConfigurationCache("Uses exec to call gradlew commands")
    }

    @TaskAction
    fun generateGraph() {
        reportsDir.get().asFile.mkdirs()

        println("Generating Mobile app dependencies...")
        execOperations.exec {
            commandLine(
                "./gradlew",
                ":android:mobile:app:generateModulesGraphvizText",
                "-Pmodules.graph.output.gv=build/reports/mobile-graph",
                "--no-configure-on-demand"
            )
        }

        println("Generating TV app dependencies...")
        execOperations.exec {
            commandLine(
                "./gradlew",
                ":android:tv:app:generateModulesGraphvizText",
                "-Pmodules.graph.output.gv=build/reports/tv-graph",
                "--no-configure-on-demand"
            )
        }

        println("Combining mobile and TV graphs...")
        val mobileGraph = project.file("build/reports/mobile-graph").readLines()
        val tvGraph = project.file("build/reports/tv-graph").readLines()

        val combinedGraph = buildString {
            appendLine("digraph {")
            appendLine("  rankdir=TB;")
            appendLine()
            appendLine("  // Mobile App Dependencies")
            mobileGraph.drop(1).dropLast(1).forEach { appendLine(it) }
            appendLine()
            appendLine("  // TV App Dependencies")
            tvGraph.drop(1).dropLast(1).forEach { appendLine(it) }
            appendLine("}")
        }

        project.file("build/reports/dependency-graph.gv").writeText(combinedGraph)
        println("Combined graph saved to: build/reports/dependency-graph.gv")

        // Try to convert to PNG if dot is available
        try {
            println("Converting graph to PNG...")
            execOperations.exec {
                commandLine("dot", "-Tpng", "build/reports/dependency-graph.gv", "-o", "docs/project_dependencies_graph.png")
            }
            println("Graph image saved to: docs/project_dependencies_graph.png")
        } catch (e: Exception) {
            println("WARNING: Graphviz (dot command) not found!")
            println("Install Graphviz to generate PNG:")
            println("  macOS: brew install graphviz")
            println("  Ubuntu: sudo apt-get install graphviz")
            println("  Windows: choco install graphviz or download from https://graphviz.org/download/")
        }
    }
}
