package control

import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.example.flightmobileapp.R
import kotlinx.android.synthetic.main.activity_control.*
import java.util.*
import kotlin.concurrent.schedule


class ControlActivity : AppCompatActivity() {
    private lateinit var client: Client
    private val timer = Timer("getImages", false).schedule(250, 1000) {
        getScreenshot()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        this.client = Client(intent.getStringExtra("url")!!)
        initSeekBar()
        initJoystick()
        timer.run()
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }


    private fun initJoystick() {
        joystickView.setOnMoveListener({ angle, strength ->
            val aileron = kotlin.math.cos(Math.toRadians(angle.toDouble())) * strength / 100
            val elevator = kotlin.math.sin(Math.toRadians(angle.toDouble())) * strength / 100

            this.client.setFromJoystick(aileron, elevator)
        },30)
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
                client.setThrottle(progress.toDouble() / 100)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        rudderSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                rudderValue.text = ((progress.toFloat() - 50) / 50).toString()
                //TODO: send to server
                client.setRudder((progress.toDouble() - 50) / 50)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

    }

    private fun getScreenshot() {
        val answer = this.client.getImage()
        runOnUiThread {
            screenshot.setImageBitmap(answer.get())
        }
    }
}