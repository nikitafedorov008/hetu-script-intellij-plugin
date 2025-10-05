package com.hetu.hetuscript.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project

class HetuRunConfiguration(
    project: Project,
    configurationFactory: ConfigurationFactory,
    name: String
) : LocatableConfigurationBase<RunConfigurationOptions>(project, configurationFactory, name) {

    private var scriptPath: String = ""
    private var programParameters: String = ""

    fun setScriptPath(path: String) {
        scriptPath = path
    }

    fun getScriptPath(): String {
        return scriptPath
    }

    fun setProgramParameters(params: String) {
        programParameters = params
    }

    fun getProgramParameters(): String {
        return programParameters
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return HetuRunConfigurationEditor()
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return HetuRunProfileState(environment)
    }

    override fun checkConfiguration() {
        if (scriptPath.isBlank()) {
            throw RuntimeConfigurationError("Hetu script path is not specified")
        }
    }

    override fun writeExternal(element: org.jdom.Element) {
        super.writeExternal(element)
        element.setAttribute("scriptPath", scriptPath)
        element.setAttribute("programParameters", programParameters)
    }

    override fun readExternal(element: org.jdom.Element) {
        super.readExternal(element)
        scriptPath = element.getAttributeValue("scriptPath") ?: ""
        programParameters = element.getAttributeValue("programParameters") ?: ""
    }

    companion object {
        const val ID = "HetuRunConfiguration"
    }
}