package com.hetu.hetuscript.toolwindow

import com.hetu.hetuscript.service.HetuAnalyzerService
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.openapi.components.service
import javax.swing.*
import java.awt.BorderLayout

class HetuAnalyzerToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = HetuAnalyzerToolWindow(project)
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(myToolWindow.getContent(), "", false)
        toolWindow.contentManager.addContent(content)
    }
}

class HetuAnalyzerToolWindow(private val project: Project) {
    private lateinit var panel: JPanel
    private lateinit var textArea: JBTextArea
    private lateinit var analyzeButton: JButton
    private lateinit var clearButton: JButton
    private var analyzeAllCallback: (() -> Unit)? = null

    init {
        createUIComponents()
        // Register this tool window with the service
        val service = project.service<HetuAnalyzerService>()
        service.setToolWindow(this)
    }

    private fun createUIComponents() {
        panel = JPanel(BorderLayout())
        
        // Create toolbar with analyze and clear buttons
        val toolbar = JPanel()
        analyzeButton = JButton("Analyze All .ht Files")
        clearButton = JButton("Clear")
        
        toolbar.add(analyzeButton)
        toolbar.add(clearButton)
        
        // Create text area for results
        textArea = JBTextArea("Hetu Analysis Results will appear here after running analysis...")
        textArea.isEditable = false
        textArea.lineWrap = true
        textArea.wrapStyleWord = true
        
        val scrollPane = JBScrollPane(textArea)
        
        // Add toolbar and text area to main panel
        panel.add(toolbar, BorderLayout.NORTH)
        panel.add(scrollPane, BorderLayout.CENTER)
        
        // Add listeners for buttons
        analyzeButton.addActionListener {
            analyzeAllCallback?.invoke()
        }
        
        clearButton.addActionListener {
            textArea.text = ""
            val service = project.service<HetuAnalyzerService>()
            service.clearResults()
        }
    }
    
    fun getContent(): JComponent {
        return panel
    }
    
    fun updateResults(results: String) {
        textArea.text = results
    }
    
    fun setAnalyzeAllCallback(callback: () -> Unit) {
        this.analyzeAllCallback = callback
    }
}