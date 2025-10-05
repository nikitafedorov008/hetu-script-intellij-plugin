package com.hetu.hetuscript.run

import com.hetu.hetuscript.HetuIcons
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project

class HetuConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    companion object {
        fun getInstance(type: HetuRunConfigurationType): HetuConfigurationFactory {
            return HetuConfigurationFactory(type)
        }
    }

    init {
        // Initialize factory
    }

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return HetuRunConfiguration(project, this, "Hetu Script")
    }

    override fun getName(): String = "Hetu Script"
    override fun getId(): String = "HetuRunConfiguration"
    override fun getIcon() = HetuIcons.FILE
}