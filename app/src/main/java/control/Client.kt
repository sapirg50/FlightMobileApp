package control

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

class Client(val url: String) {
    private var unsuccessfulRequests: Int = 0
    private var command: Command = Command(0.0, 0.0, 0.0, 0.0)
    private val httpClient = OkHttpClient().newBuilder().callTimeout(5, TimeUnit.SECONDS).build()

    fun setCommand(command: Command): Boolean {
        this.command = command
        return this.sendCommand()
    }

    fun setFromJoystick(aileron: Double, elevator: Double): Boolean {
        this.command.setAileron(aileron)
        this.command.setElevator(elevator)
        return this.sendCommand()
    }

    fun setRudder(rudder: Double): Boolean {
        this.command.setRudder(rudder)
        return this.sendCommand()
    }

    fun setThrottle(throttle: Double): Boolean {
        this.command.setThrottle(throttle)
        return this.sendCommand()
    }


    private fun sendCommand(): Boolean {
        val gson = GsonBuilder().create()
        val jsonCommand = gson.toJson(this.command)
        val request = Request.Builder().post(jsonCommand.toRequestBody(MEDIA_TYPE_MARKDOWN))
            .url(this.url + "/api/command").build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200 && response.code==409) {
                    unsuccessfulRequests = 0
                } else {
                    unsuccessfulRequests++
                }
                println("good" + response.body)
            }

            override fun onFailure(call: Call, e: IOException) {
                println("failed: ${call.request()}")
                println(e.message)
                unsuccessfulRequests++
            }
        })
        val bool = unsuccessfulRequests < 50
        if (!bool){
            unsuccessfulRequests=0
        }
        return bool

    }

    fun getImage(): Future<Bitmap> {
        val answer: Future<Bitmap> = Future()
        val request = Request.Builder().get()
            .url(this.url + "/api/screenshot").build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    val byteStream = response.body?.byteStream()
                    answer.set(BitmapFactory.decodeStream(byteStream))
                } catch (e: Exception) {
                    answer.set(null)
                    println(e.message)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("failed: ${call.request()}")
                println(e.message)
                answer.set(null)
            }
        })
        return answer
    }

    companion object {
        val MEDIA_TYPE_MARKDOWN = "application/json; charset=utf-8".toMediaType()
    }

}