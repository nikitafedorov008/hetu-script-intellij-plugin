package com.hetu.hetuscript.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project

class HetuCompileConfiguration(
    project: Project,
    configurationFactory: ConfigurationFactory,
    name: String
) : LocatableConfigurationBase<RunConfigurationOptions>(project, configurationFactory, name) {

    private var sourceScriptPath: String = "ht-lib/"
    private var outputPath: String = "build/hetu/output.out"
    private var programParameters: String = ""

    fun setSourceScriptPath(path: String) {
        sourceScriptPath = path
    }

    fun getSourceScriptPath(): String {
        return sourceScriptPath
    }

    fun setOutputPath(path: String) {
        outputPath = path
    }

    fun getOutputPath(): String {
        return outputPath
    }

    fun setProgramParameters(params: String) {
        programParameters = params
    }

    fun getProgramParameters(): String {
        return programParameters
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return HetuCompileConfigurationEditor()
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return HetuCompileProfileState(environment)
    }

    override fun checkConfiguration() {
        if (sourceScriptPath.isBlank()) {
            throw RuntimeConfigurationError("Source script path is not specified")
        }
        if (outputPath.isBlank()) {
            throw RuntimeConfigurationError("Output path is not specified")
        }
    }

    override fun writeExternal(element: org.jdom.Element) {
        super.writeExternal(element)
        element.setAttribute("sourceScriptPath", sourceScriptPath)
        element.setAttribute("outputPath", outputPath)
        element.setAttribute("programParameters", programParameters)
    }

    override fun readExternal(element: org.jdom.Element) {
        super.readExternal(element)
        sourceScriptPath = element.getAttributeValue("sourceScriptPath") ?: ""
        outputPath = element.getAttributeValue("outputPath") ?: ""
        programParameters = element.getAttributeValue("programParameters") ?: ""
    }

    companion object {
        const val ID = "HetuCompileConfiguration"
    }
}