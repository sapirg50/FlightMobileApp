package connect

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import architectureexample.URL
import architectureexample.UrlViewModel
import com.example.flightmobileapp.R
import com.example.flightmobileapp.databinding.ActivityConnectBinding
import control.ControlActivity
import kotlinx.android.synthetic.main.activity_connect.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern


class ConnectActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityConnectBinding
    private lateinit var urlViewModel: UrlViewModel
    private lateinit var buttons: List<Button>
    private val httpClient = OkHttpClient().newBuilder()
        .retryOnConnectionFailure(false)
        .connectTimeout(8, TimeUnit.SECONDS).build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_connect)
        initButtons()
        initConnectButton()
        initUrlViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu)
    }

    private fun initButtons() {
        val button1 = findViewById<Button>(R.id.local_host1)
        val button2 = findViewById<Button>(R.id.local_host2)
        val button3 = findViewById<Button>(R.id.local_host3)
        val button4 = findViewById<Button>(R.id.local_host4)
        val button5 = findViewById<Button>(R.id.local_host5)
        button1.isVisible = button1.text.isNotEmpty()
        button2.isVisible = button2.text.isNotEmpty()
        button3.isVisible = button3.text.isNotEmpty()
        button4.isVisible = button4.text.isNotEmpty()
        button5.isVisible = button5.text.isNotEmpty()
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        buttons = listOf(button1, button2, button3, button4, button5)
    }

    private fun initConnectButton() {
        val editText = findViewById<EditText>(R.id.url)
        val connectButton = findViewById<Button>(R.id.connect)
        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                connectButton.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    private fun initUrlViewModel() {
        urlViewModel = UrlViewModel(application, applicationContext)
        restartButtons(urlViewModel.getAllUrls())
    }

    fun deleteAll(item: MenuItem) {
        urlViewModel.deleteAll()
        val allUrls = urlViewModel.getAllUrls()
        restartButtons(allUrls)
    }

    fun connect(view: View) {
        var urlText: String = url.text.toString()
        if (isURLValid(urlText)) {
            if (!urlText.startsWith("http://")) {
                urlText = "http:// ${urlText}"
            }
            if (hostUrlReachable(urlText)) {
                val intent = Intent(this, ControlActivity::class.java)
                addUrl(url.text.toString())
                intent.putExtra("url", urlText)
                startActivity(intent)
            } else {
                val error =
                    Toast.makeText(this, "connection failed, please try again", Toast.LENGTH_SHORT)
                error.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM, 0, 300)
                error.show()
            }
        } else {
            val error = Toast.makeText(this, "invalid url, please try again", Toast.LENGTH_SHORT)
            error.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM, 0, 1000)
            error.show()
        }
    }

    private fun addUrl(urlString: String) {
        val urlNode = URL(urlString)
        urlViewModel.insert(urlNode)
        val allUrls = urlViewModel.getAllUrls()
        restartButtons(allUrls)
    }

    private fun restartButtons(urls: List<URL>?) {
        if (urls != null) {
            for ((i, url) in urls.withIndex()) {
                buttons[i].text = url.path
                buttons[i].isVisible = true
            }
            var i = 5
            while (i > urls.count()) {
                buttons[i - 1].isVisible = false
                i--;
            }
        }
    }

    override fun onClick(view: View) {
        val button = view as Button
        val buttonText = button.text as String
        if (buttonText.isNotEmpty()) {
            val textURL = findViewById<EditText>(R.id.url)
            textURL.setText(buttonText)
        }
    }

    private fun hostUrlReachable(url: String): Boolean {
        val oldPolicy = StrictMode.getThreadPolicy()
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val request =
            Request.Builder().get().url(url).build()
        return try {
            this.httpClient.newCall(request).execute()
            true
        } catch (e: Exception) {
            false
        } finally {
            StrictMode.setThreadPolicy(oldPolicy)
        }


    }

    private fun isURLValid(urlSyntax: String): Boolean {
        val expression = "(http://)?[^:, ]*:[1-9][0-9]{0,4}"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(urlSyntax)
        return matcher.matches()
    }
}