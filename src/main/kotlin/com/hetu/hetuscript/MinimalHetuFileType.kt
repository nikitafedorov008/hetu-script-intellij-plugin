package com.hetu.hetuscript

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

class MinimalHetuFileType : LanguageFileType(HetuLanguage) {
    override fun getName() = "HetuScript"
    override fun getDescription() = "Hetu Script file"
    override fun getDefaultExtension() = "ht"
    override fun getIcon(): Icon? = null

    companion object {
        val INSTANCE = MinimalHetuFileType()
    }
}