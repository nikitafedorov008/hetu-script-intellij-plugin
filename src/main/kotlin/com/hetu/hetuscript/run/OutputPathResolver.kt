package com.hetu.hetuscript.run

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

object OutputPathResolver {
    private val defaultOutputRelative = "build/hetu/output.out"

    /**
     * Resolve the output path according to rules:
     * - if configuredOutput is non-empty and not equal to default -> return it unchanged
     * - else if a pubspec.yaml exists in the same project tree as sourceFile:
     *     - if it contains a `version: X` -> use `<name>-<version>.out`
     *     - otherwise use `<name>.out`
     * - else -> return project's default `build/hetu/output.out` (preserve existing behaviour)
     */
    fun resolveOutputPath(project: Project, sourceFile: VirtualFile?, configuredOutput: String?): String {
        val configured = configuredOutput?.takeIf { it.isNotBlank() }
        val projectBase = project.basePath ?: System.getProperty("user.dir")
        val buildDir = File(projectBase, "build/hetu")
        if (configured != null && !isDefaultOutputPath(configured)) {
            // explicit user-configured path -> respect it
            return configured
        }

        // if no source file or pubspec cannot be found, keep original default behaviour
        if (sourceFile == null) {
            buildDir.mkdirs()
            return File(projectBase, defaultOutputRelative).absolutePath
        }

        // search upwards from the source file's folder for pubspec.yaml until project base
        var dir: VirtualFile? = sourceFile.parent
        val projectBaseVf = LocalFileSystem.getInstance().findFileByPath(projectBase)
        var pubspec: VirtualFile? = null
        while (dir != null && dir != projectBaseVf) {
            val candidate = dir.findChild("pubspec.yaml")
            if (candidate != null && !candidate.isDirectory) {
                pubspec = candidate
                break
            }
            dir = dir.parent
        }

        val baseName = sourceFile.nameWithoutExtension
        buildDir.mkdirs()

        if (pubspec == null) {
            // preserve legacy behaviour when pubspec absent
            return File(projectBase, defaultOutputRelative).absolutePath
        }

        // try to read version from pubspec.yaml
        val text = try {
            String(pubspec.contentsToByteArray())
        } catch (ex: Exception) {
            ""
        }

        val versionRegex = Regex("^\\s*version\\s*:\\s*['\"]?([^\n'\"]+)['\"]?", RegexOption.MULTILINE)
        val match = versionRegex.find(text)
        val filename = if (match != null) {
            val ver = sanitizeVersion(match.groupValues[1])
            "$baseName-$ver.out"
        } else {
            "$baseName.out"
        }

        return File(buildDir, filename).absolutePath
    }

    private fun sanitizeVersion(v: String): String {
        return v.trim().replace(Regex("[^A-Za-z0-9._-]"), "-")
    }

    private fun isDefaultOutputPath(path: String): Boolean {
        return path.endsWith("output.out") || path.contains("/build/hetu/")
    }
}
