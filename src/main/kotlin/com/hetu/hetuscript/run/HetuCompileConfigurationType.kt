package com.hetu.hetuscript.run

import com.hetu.hetuscript.HetuIcons
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.ConfigurationTypeUtil

class HetuCompileConfigurationType : ConfigurationTypeBase(
    "HetuCompileConfiguration",
    "Hetu Script Compile",
    "Compile Hetu Script files to executable or bytecode",
    HetuIcons.FILE
) {
    private val factory: HetuCompileConfigurationFactory
    
    init {
        factory = HetuCompileConfigurationFactory(this)
        addFactory(factory)
    }
    
    companion object {
        val instance: HetuCompileConfigurationType
            get() = ConfigurationTypeUtil.findConfigurationType(HetuCompileConfigurationType::class.java)
    }
    
    override fun getConfigurationFactories(): Array<com.intellij.execution.configurations.ConfigurationFactory> {
        return super.getConfigurationFactories()
    }
}