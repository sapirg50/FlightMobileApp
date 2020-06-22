package control

import android.content.ClipData.Item
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import architectureexample.URL
import architectureexample.UrlViewModel
import com.example.flightmobileapp.R
import kotlinx.android.synthetic.main.activity_control.*


class ControlActivity : AppCompatActivity() {
    private val STATE_ITEMS = "items"
    private lateinit var urlViewModel:UrlViewModel

    //TODO: Make sure to declare as ArrayList so it's Serializable
    private val mItems: ArrayList<Item>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        initSeekBar()
        initJoystick()
        urlViewModel = ViewModelProviders.of(this).get(UrlViewModel::class.java)
        urlViewModel.getAllUrls()?.observe(this, Observer<List<URL?>?> {
            //onChanged necessary?
            //update RecyclerView
            Toast.makeText(this, "onChanged", Toast.LENGTH_SHORT).show();
        })
    }

    private fun initJoystick() {
        joystickView.setOnMoveListener { angle, strength ->
            val aileron = kotlin.math.cos(Math.toRadians(angle.toDouble())) * strength / 100
            val elevator = kotlin.math.sin(Math.toRadians(angle.toDouble())) * strength / 100
            //TODO: send to server aileron, elevator
        }
    }

    private fun initSeekBar() {
        throttleSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                throttleValue.text = (progress.toFloat() / 100).toString()
                //TODO: send to server (progress.toFloat() / 10).toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {


            }
        })

        rudderSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                rudderValue.text = ((progress.toFloat() - 50) / 50).toString()
                //TODO: send to server
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

    }
}