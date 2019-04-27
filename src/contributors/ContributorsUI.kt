package contributors

import contributors.ContributorsUI.LoadingStatus.*
import contributors.Variant.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import tasks.*
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.ActionListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.prefs.Preferences
import javax.swing.*
import javax.swing.table.DefaultTableModel
import kotlin.coroutines.CoroutineContext

fun main() {
    setDefaultFontSize(18f)
    ContributorsUI().apply {
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }
}

enum class Variant {
    BLOCKING,    // Request1Blocking
    BACKGROUND,  // Request2Background
    CALLBACKS,   // Request3Callbacks
    COROUTINE,   // Request4Coroutine
    CONCURRENT,  // Request6Concurrent
    PROGRESS,    // Request6Progress
    CHANNELS,    // Request7Channels
}

private val INSETS = Insets(3, 10, 3, 10)
private val COLUMNS = arrayOf("Login", "Contributions")

@Suppress("CONFLICTING_INHERITED_JVM_DECLARATIONS")
class ContributorsUI : JFrame("GitHub Contributors"), CoroutineScope {
    private val username = JTextField(20)
    private val password = JPasswordField(20)
    private val org = JTextField(20)
    private val variant = JComboBox<Variant>(Variant.values())
    private val load = JButton("Load contributors")
    private val cancel = JButton("Cancel").apply { isEnabled = false }

    private val resultsModel = DefaultTableModel(COLUMNS, 0)
    private val results = JTable(resultsModel)
    private val resultsScroll = JScrollPane(results).apply {
        preferredSize = Dimension(200, 200)
    }

    private val loadingIcon = ImageIcon(javaClass.classLoader.getResource("ajax-loader.gif"))
    private val loadingStatus = JLabel("Start new loading", loadingIcon, SwingConstants.CENTER)

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Swing

    init {
        // Create UI
        rootPane.contentPane = JPanel(GridBagLayout()).apply {
            addLabeled("GitHub Username", username)
            addLabeled("Password/Token", password)
            addWideSeparator()
            addLabeled("Organization", org)
            addLabeled("Variant", variant)
            addWideSeparator()
            addWide(JPanel().apply {
                add(load)
                add(cancel)
            })
            addWide(resultsScroll) {
                weightx = 1.0
                weighty = 1.0
                fill = GridBagConstraints.BOTH
            }
            addWide(loadingStatus)
        }
        // Add button listener
        load.addActionListener {
            savePrefs()
            doLoad()
        }
        // Install window close listener to save preferences and exit
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                job.cancel()
                savePrefs()
                System.exit(0)
            }
        })
        // Load initial preferences
        loadPrefs()
    }

    private fun selectedVariant(): Variant = variant.getItemAt(variant.selectedIndex)

    private fun doLoad() {
        clearResults()
        val req = RequestData(username.text, password.password.joinToString(""), org.text)
        val startTime = System.currentTimeMillis()
        when (selectedVariant()) {
            BLOCKING -> { // Blocking UI thread
                val users = loadContributorsBlocking(req)
                updateResults(users, startTime)
            }
            BACKGROUND -> { // Blocking a background thread
                loadContributorsBackground(req) { users ->
                    SwingUtilities.invokeLater {
                        updateResults(users, startTime)
                    }
                }
            }
            CALLBACKS -> { // Using callbacks
                loadContributorsCallbacks(req) { users ->
                    SwingUtilities.invokeLater {
                        updateResults(users, startTime)
                    }
                }
            }
            COROUTINE -> { // Using coroutines
                launch {
                    val users = loadContributors(req)
                    updateResults(users, startTime)
                }.updateCancelJob()
            }
            CONCURRENT -> { // Performing requests concurrently
                launch {
                    updateResults(loadContributorsConcurrent(req), startTime)
                }.updateCancelJob()
            }
            PROGRESS -> { // Showing progress
                launch {
                    loadContributorsProgress(req) { users, completed ->
                        updateResults(users, startTime, completed)
                    }
                }.updateCancelJob()
            }
            CHANNELS -> {  // Performing requests concurrently and showing progress
                launch {
                    loadContributorsChannels(req) { users, completed ->
                        updateResults(users, startTime, completed)
                    }
                }.updateCancelJob()
            }
        }
    }

    private fun clearResults() {
        updateContributors(listOf())
        updateLoadingStatus(IN_PROGRESS)
        enableNewLoading(false)
    }

    private fun updateResults(
        users: List<User>,
        startTime: Long? = null,
        completed: Boolean = true
    ) {
        updateContributors(users)
        updateLoadingStatus(if (completed) COMPLETED else IN_PROGRESS, startTime)
        if (completed) {
            enableNewLoading(true)
        }
    }

    private fun updateContributors(users: List<User>) {
        log.info("Updating result with ${users.size} rows")
        resultsModel.setDataVector(users.map {
            arrayOf(it.login, it.contributions)
        }.toTypedArray(), COLUMNS)
    }

    enum class LoadingStatus { COMPLETED, CANCELED, IN_PROGRESS }

    private fun updateLoadingStatus(
        status: LoadingStatus,
        startTime: Long? = null
    ) {
        val secText = if (startTime != null) {
            val time = System.currentTimeMillis() - startTime
            "${(time / 1000)}.${time % 1000 / 100} sec"
        } else ""

        loadingStatus.text = "Loading status: " +
                when (status) {
                    COMPLETED -> "completed in $secText"
                    IN_PROGRESS -> "in progress $secText"
                    CANCELED -> "canceled"
                }

        loadingStatus.icon = if (status == IN_PROGRESS) loadingIcon else null
    }

    private fun Job.updateCancelJob() {
        enableNewLoading(newLoading = false, cancellable = true)
        val listener = ActionListener {
            cancel()
            updateLoadingStatus(CANCELED)
        }
        cancel.addActionListener(listener)
        launch {
            join()
            enableNewLoading(newLoading = true)
            cancel.removeActionListener(listener)
        }
    }

    private fun enableNewLoading(newLoading: Boolean, cancellable: Boolean = false) {
        load.isEnabled = newLoading
        cancel.isEnabled = cancellable
    }

    private fun prefNode(): Preferences = Preferences.userRoot().node("ContributorsUI")

    private fun loadPrefs() {
        prefNode().apply {
            username.text = get("username", "")
            password.text = get("password", "")
            org.text = get("org", "americanexpress")
            variant.selectedIndex = variantOf(get("variant", "")).ordinal
        }
    }

    private fun savePrefs() {
        prefNode().apply {
            if (username.text.isNullOrEmpty()) {
                removeNode()
            } else {
                put("username", username.text)
                put("password", password.password.joinToString(""))
                put("org", org.text)
                put("variant", selectedVariant().name)
                sync()
            }
        }
    }
}

fun variantOf(str: String): Variant =
    try {
        Variant.valueOf(str)
    } catch (e: IllegalArgumentException) {
        Variant.values()[0]
    }

fun JPanel.addLabeled(label: String, component: JComponent) {
    add(JLabel(label), GridBagConstraints().apply {
        gridx = 0
        insets = INSETS
    })
    add(component, GridBagConstraints().apply {
        gridx = 1
        insets = INSETS
        anchor = GridBagConstraints.WEST
        fill = GridBagConstraints.HORIZONTAL
        weightx = 1.0
    })
}

fun JPanel.addWide(component: JComponent, constraints: GridBagConstraints.() -> Unit = {}) {
    add(component, GridBagConstraints().apply {
        gridx = 0
        gridwidth = 2
        insets = INSETS
        constraints()
    })
}

fun JPanel.addWideSeparator() {
    addWide(JSeparator()) {
        fill = GridBagConstraints.HORIZONTAL
    }
}