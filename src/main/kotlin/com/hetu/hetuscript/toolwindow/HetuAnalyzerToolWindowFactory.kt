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
    private lateinit var refreshButton: JButton
    private lateinit var clearButton: JButton

    init {
        createUIComponents()
        // Register this tool window with the service
        val service = project.service<HetuAnalyzerService>()
        service.setToolWindow(this)
    }

    private fun createUIComponents() {
        panel = JPanel(BorderLayout())
        
        // Create toolbar with refresh and clear buttons
        val toolbar = JPanel()
        refreshButton = JButton("Refresh Analysis")
        clearButton = JButton("Clear")
        
        toolbar.add(refreshButton)
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
        refreshButton.addActionListener {
            // This would trigger a new analysis from the tool window itself
            // For now, just clear the results
            textArea.text = "Running analysis... (Refresh functionality would be implemented here)"
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
}