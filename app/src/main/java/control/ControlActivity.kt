package control

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Gravity
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flightmobileapp.R
import kotlinx.android.synthetic.main.activity_control.*
import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.schedule
import kotlin.concurrent.thread


class ControlActivity : AppCompatActivity() {
    private lateinit var client: Client
    private val timer: Timer = Timer("getImages", false)
    private val imageQueue: BlockingQueue<Bitmap> = LinkedBlockingQueue()
    private var stop: Boolean = false
    private var numOfNullImages = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        this.client = Client(intent.getStringExtra("url")!!)
        initSeekBar()
        initJoystick()
        this.timer.schedule(0, 300) {
            if (imageQueue.size <= 30) {
                getScreenshot()
            } else {
                sleep(2500)
            }
        }
        thread(start = true, name = "imageDisplay") {
            displayScreenshots()
        }

    }

    override fun onStop() {
        super.onStop()
        this.stop = true
        timer.cancel()
    }

    override fun onPause() {
        super.onPause()
        this.stop = true
        timer.cancel()

    }

    override fun onDestroy() {
        super.onDestroy()
        this.stop = true
        timer.cancel()
    }


    private fun initJoystick() {
        joystickView.setOnMoveListener({ angle, strength ->
            val aileron = kotlin.math.cos(Math.toRadians(angle.toDouble())) * strength / 100
            val elevator = kotlin.math.sin(Math.toRadians(angle.toDouble())) * strength / 100
            if (!this.client.setFromJoystick(aileron, elevator)){
                this.errorPopup()
            }

        }, 30)
    }

    private fun errorPopup() {
        runOnUiThread {
        val error = Toast.makeText(this, "Failed to send data to server" +
                "\nGo back and try inserting another host address.", Toast.LENGTH_SHORT)
        error.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM, 0, 300)
        error.show()
        }
    }

    private fun initSeekBar() {
        throttleSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                val throttle = progress.toDouble() / 100
                throttleValue.text = throttle.toString()
                //TODO: send to server (progress.toFloat() / 10).toString()
                if (!client.setThrottle(throttle)){
                    errorPopup()
                }
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
                val rudder = (progress.toDouble() - 50) / 50
                rudderValue.text = rudder.toString()
                //TODO: send to server
                if(client.setRudder(rudder)){
                    errorPopup()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

    }

    private fun getScreenshot() {
        val answer = this.client.getImage()
        if (answer.get() != null) {
            this.imageQueue.offer(answer.get())
            this.numOfNullImages = 0
        } else {
            if (this.numOfNullImages >= 1 && this.imageQueue.isEmpty()) {
                runOnUiThread {
                    val error = Toast.makeText(
                        this,
                        "Failed to get screenshots from server" +
                                "\nGo back and try inserting another host address.",
                        Toast.LENGTH_SHORT
                    )
                    error.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM, 0, 300)
                    error.show()
                }
                this.numOfNullImages = 0
            }
            this.numOfNullImages++
        }
    }

    private fun displayScreenshots() {
        while (!this.stop) {
            if (!this.imageQueue.isEmpty()) {
                runOnUiThread {
                    val image = this.imageQueue.poll()
                    screenshot.setImageBitmap(image)
                }
                sleep(400)
            } else {
                sleep(1200)
            }
        }
    }
}