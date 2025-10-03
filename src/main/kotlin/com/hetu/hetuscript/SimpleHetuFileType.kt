package com.hetu.hetuscript

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class SimpleHetuFileType : LanguageFileType(HetuLanguage) {
    init {
        println("SimpleHetuFileType initialized")
    }
    
    override fun getName() = "HetuScript"
    override fun getDescription() = "Hetu Script file"
    override fun getDefaultExtension() = "ht"
    override fun getIcon(): Icon? = HetuIcons.FILE
}