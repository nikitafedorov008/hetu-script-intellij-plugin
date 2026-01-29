package com.hetu.hetuscript.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.openapi.project.Project

class HetuCompileConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    companion object {
        fun getInstance(type: ConfigurationType): HetuCompileConfigurationFactory {
            return HetuCompileConfigurationFactory(type)
        }
    }
    
    override fun createTemplateConfiguration(project: Project): HetuCompileConfiguration {
        val configuration = HetuCompileConfiguration(project, this, "Hetu Compile")
        configuration.setSourceScriptPath("ht-lib/")
        configuration.setOutputPath("build/hetu/output.out")
        return configuration
    }

    override fun getName(): String = "Hetu Compile"

    override fun getId(): String = HetuCompileConfiguration.ID
}