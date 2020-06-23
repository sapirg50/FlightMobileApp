package connect

import android.content.Intent
import android.os.Bundle
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
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class ConnectActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityConnectBinding
    private lateinit var urlViewModel: UrlViewModel
    private lateinit var buttons: List<Button>


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
        /*urlViewModel.getAllUrls().observe(this, Observer<List<URL?>?> {
            fun onConfigurationChanged(newConfig: Configuration) {
                super.onConfigurationChanged(newConfig)
                Toast.makeText(this, "onChanged", Toast.LENGTH_SHORT).show();
            }
            //onChanged necessary?*/
        restartButtons(urlViewModel.getAllUrls())

        //TODO: restartButtons
    }

    fun deleteAll(item: MenuItem) {
        urlViewModel.deleteAll()
        val allUrls = urlViewModel.getAllUrls()
        restartButtons(allUrls)
    }

    fun connect(view: View) {
        //TODO: update cache - url.text to top (even if url connect not succeed)
        //TODO: Get image from simulator. navigate only if GET was successful

        if (true || hostUrlReachable(parseUrl(url.text.toString()), 2)) {
            val intent = Intent(this, ControlActivity::class.java)
            addUrl(url.text.toString())
            intent.putExtra("url", url.text.toString())
            startActivity(intent)
        } else {
            val error =
                Toast.makeText(this, "connection failed, please try again", Toast.LENGTH_SHORT)
            error.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM, 0, 300)
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

    fun hostUrlReachable(values: Pair<String?, Int?>, timeout: Int): Boolean {
        return true
        if (values.first == null || values.second == null) {
            return false
        }
        try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(values.first, values.second!!), timeout)
                return true
            }
        } catch (e: IOException) {
            return false // Either timeout or unreachable or failed DNS lookup.
        }
    }

    fun parseUrl(url: String): Pair<String?, Int?> {
        var type = "http"
        if (url.contains("https")) {
            type += "s"
        }
        return try {
            val host = url.substring(0, url.lastIndexOf(':')).replace("$type://", "")
            val port = Integer.parseInt(
                url.substring(
                    url.lastIndexOf(':') + 1,
                    url.length
                )
            )
            Pair(host, port)
        } catch (e: Exception) {
            Pair(null, null)
        }

    }
}