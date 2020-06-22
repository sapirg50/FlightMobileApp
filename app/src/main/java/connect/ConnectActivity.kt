package connect

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import architectureexample.URL
import architectureexample.UrlViewModel
import com.example.flightmobileapp.R
import com.example.flightmobileapp.databinding.ActivityConnectBinding
import control.ControlActivity


class ConnectActivity : AppCompatActivity() , View.OnClickListener {
    private lateinit var binding: ActivityConnectBinding
    private lateinit var urlViewModel:UrlViewModel
    private lateinit var buttons: List<Button>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_connect)
        initButtons()
        initConnectButton()
        initUrlViewModel()
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
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub -- check if valid URL
            }
        })
    }

    private fun initUrlViewModel() {
        urlViewModel = ViewModelProviders.of(this).get(UrlViewModel::class.java)
        urlViewModel.getAllUrls()?.observe(this, Observer<List<URL?>?> {
            fun onConfigurationChanged(newConfig: Configuration) {
                super.onConfigurationChanged(newConfig)
                Toast.makeText(this, "onChanged", Toast.LENGTH_SHORT).show();
            }
            //onChanged necessary?

            //TODO: restartButtons
        })
    }

    fun connect(view: View) {
        //TODO: update cache - url.text to top (even if url connect not succeed)
        //TODO: Get image from simulator. navigate only if GET was successful
        val error = Toast.makeText(this,"connection failed, please try again", Toast.LENGTH_SHORT)
        error.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM, 0, 300)
        //error.show()
        addUrl(url.text.toString())
        val intent = Intent(this, ControlActivity::class.java)
        intent.putExtra("url", "http//:127.0.0.1:5000")
        startActivity(intent)
    }

    private fun addUrl(urlString: String) {
        val urlNode = URL(urlString)
        urlViewModel.insert(urlNode)
        val allUrls = urlViewModel.getAllUrls()?.value
        restartButtons(allUrls)
    }

    private fun restartButtons(urls: List<URL?>?) {
        if (urls != null) {
            for((i, url) in urls.withIndex()) {
                if (url != null) {
                    buttons[i].text = url.url_path
                }
                buttons[i].isVisible = true
            }
        }
        var j = urls?.count() ?: -1
        while(j < 5) {
            buttons[j].isVisible = false
            j++
        }
    }

    override fun onClick(view: View) {
        val button = view as Button
        val buttonText = button.text as String
        if(buttonText.isNotEmpty()) {
            val textURL = findViewById<EditText>(R.id.url)
            textURL.setText(buttonText)
        }
    }
}