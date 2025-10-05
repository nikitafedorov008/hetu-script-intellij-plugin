package com.hetu.hetuscript.run

import com.hetu.hetuscript.HetuIcons
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.ConfigurationTypeUtil

class HetuRunConfigurationType : ConfigurationTypeBase(
    "HetuRunConfiguration",
    "Hetu Script",
    "Run Hetu Script files",
    HetuIcons.FILE
) {
    init {
        addFactory(HetuConfigurationFactory(this))
    }
    
    companion object {
        val instance: HetuRunConfigurationType
            get() = ConfigurationTypeUtil.findConfigurationType(HetuRunConfigurationType::class.java)
    }
    
    override fun getConfigurationFactories(): Array<com.intellij.execution.configurations.ConfigurationFactory> {
        return super.getConfigurationFactories()
    }
}