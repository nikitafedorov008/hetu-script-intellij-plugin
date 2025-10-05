package com.hetu.hetuscript.service

import com.hetu.hetuscript.toolwindow.HetuAnalyzerToolWindow
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class HetuAnalyzerService(val project: Project) {
    private var toolWindow: HetuAnalyzerToolWindow? = null
    
    fun setToolWindow(toolWindow: HetuAnalyzerToolWindow) {
        this.toolWindow = toolWindow
    }
    
    fun updateResults(results: String) {
        toolWindow?.updateResults(results)
    }
    
    fun clearResults() {
        toolWindow?.updateResults("")
    }
}