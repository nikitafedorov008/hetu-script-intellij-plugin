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
    private lateinit var analyzeButton: JButton
    private lateinit var clearButton: JButton
    private var analyzeAllCallback: (() -> Unit)? = null

    // UI model
    private val listModel = javax.swing.DefaultListModel<AnalysisResult>()
    private val resultList = com.intellij.ui.components.JBList<AnalysisResult>(listModel)
    // detail area removed — list now contains inline summaries (double-click still opens file)

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
        analyzeButton = JButton("Analyze")
        clearButton = JButton("Clear")

        toolbar.add(analyzeButton)
        toolbar.add(clearButton)

        // Configure list and detail area
        resultList.cellRenderer = object : javax.swing.ListCellRenderer<AnalysisResult> {
            private val panel = JPanel(BorderLayout())

            override fun getListCellRendererComponent(
                list: javax.swing.JList<out AnalysisResult>?,
                value: AnalysisResult?,
                index: Int,
                isSelected: Boolean,
                cellHasFocus: Boolean
            ): java.awt.Component {
                panel.removeAll()
                if (value == null) return panel

                // File name (bold) + inline multi-line message below (HTML for wrapping)
                val fileLabel = com.intellij.ui.components.JBLabel("<html><b>${escapeHtml(value.filePath)}</b></html>")
                val messageHtml = buildString {
                    append("<html><div style=\"margin-top:6px; font-size:11px; color:#424242\">")
                    // color errors/warnings (use neutral tone for errors)
                    val color = when (value.status) {
                        AnalysisStatus.ERROR -> "#455A64"
                        AnalysisStatus.WARNING -> "#F9A825"
                        AnalysisStatus.SUCCESS -> "#2E7D32"
                        AnalysisStatus.UNKNOWN -> "#616161"
                    }
                    append("<div style=\"color:$color\">${escapeHtml(value.message).replace("\n", "<br/>")}</div>")
                    append("</div></html>")
                }
                val messageLabel = com.intellij.ui.components.JBLabel(messageHtml)

                // status badge on the right
                val statusLabel = com.intellij.ui.components.JBLabel(when (value.status) {
                    AnalysisStatus.ERROR -> "ERROR"
                    AnalysisStatus.WARNING -> "WARNING"
                    AnalysisStatus.SUCCESS -> "OK"
                    AnalysisStatus.UNKNOWN -> "?"
                })

                statusLabel.foreground = when (value.status) {
                    AnalysisStatus.ERROR -> java.awt.Color(0x455A64)
                    AnalysisStatus.WARNING -> java.awt.Color(0xF9A825)
                    AnalysisStatus.SUCCESS -> java.awt.Color(0x2E7D32)
                    AnalysisStatus.UNKNOWN -> java.awt.Color(0x616161)
                }

                val textPanel = JPanel()
                textPanel.layout = javax.swing.BoxLayout(textPanel, javax.swing.BoxLayout.Y_AXIS)
                fileLabel.alignmentX = java.awt.Component.LEFT_ALIGNMENT
                messageLabel.alignmentX = java.awt.Component.LEFT_ALIGNMENT
                textPanel.add(fileLabel)
                textPanel.add(javax.swing.Box.createVerticalStrut(4))
                textPanel.add(messageLabel)

                val infoPanel = JPanel(BorderLayout())
                infoPanel.add(textPanel, BorderLayout.WEST)
                infoPanel.add(statusLabel, BorderLayout.EAST)

                if (isSelected) {
                    panel.background = list?.selectionBackground
                    fileLabel.foreground = list?.selectionForeground
                } else {
                    panel.background = list?.background
                    fileLabel.foreground = list?.foreground
                }

                panel.add(infoPanel, BorderLayout.CENTER)
                panel.border = javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6)
                return panel
            }

            private fun escapeHtml(s: String): String {
                return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
            }
        }

        resultList.emptyText.text = "Run Analyze to scan project"
        resultList.selectionMode = javax.swing.ListSelectionModel.SINGLE_SELECTION

        // Open file on double-click (keep behavior)
        resultList.addMouseListener(object : java.awt.event.MouseAdapter() {
            override fun mouseClicked(e: java.awt.event.MouseEvent) {
                if (e.clickCount == 2) {
                    val sel = resultList.selectedValue ?: return
                    val vf = com.intellij.openapi.vfs.LocalFileSystem.getInstance().findFileByPath(sel.filePath)
                    if (vf != null) {
                        com.intellij.openapi.fileEditor.OpenFileDescriptor(project, vf).navigate(true)
                    }
                }
            }
        })

        // show message as tooltip for selected/hovered item
        resultList.addListSelectionListener { _ ->
            resultList.toolTipText = resultList.selectedValue?.message
        }

        // Single-pane layout: list only (details shown inline / via tooltip)
        val scroll = com.intellij.ui.components.JBScrollPane(resultList)
        panel.add(toolbar, BorderLayout.NORTH)
        panel.add(scroll, BorderLayout.CENTER)

        // Add listeners for buttons
        analyzeButton.addActionListener {
            analyzeAllCallback?.invoke()
        }

        clearButton.addActionListener {
            listModel.clear()
            val service = project.service<HetuAnalyzerService>()
            service.clearResults()
        }
    }

    fun getContent(): JComponent {
        return panel
    }

    fun updateResults(results: List<AnalysisResult>) {
        // show only problematic results (ERROR/WARNING/UNKNOWN)
        listModel.clear()
        results.filter { it.status != AnalysisStatus.SUCCESS }.forEach { listModel.addElement(it) }
        if (!listModel.isEmpty) {
            resultList.selectedIndex = 0
        } else {
            // nothing to show when list is empty
        }
    }

    // keep backward-compatible method
    fun updateResults(resultsText: String) {
        val parsed = parseAnalysisOutput(resultsText)
        updateResults(parsed)
    }

    fun setAnalyzeAllCallback(callback: () -> Unit) {
        this.analyzeAllCallback = callback
    }
}