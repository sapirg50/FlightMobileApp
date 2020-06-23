package control

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class Client(val url: String) {
    private val defaultBitmap = null
    private var command: Command = Command(0.0, 0.0, 0.0, 0.0)
    private val httpClient = OkHttpClient()/*.Builder()
        .callTimeout(20,TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()*/

    fun setCommand(command: Command) {
        this.command = command
        this.sendCommand()
    }

    fun setFromJoystick(aileron: Double, elevator: Double) {
        this.command.setAileron(aileron)
        this.command.setElevator(elevator)
        this.sendCommand()
    }

    fun setRudder(rudder: Double) {
        this.command.setRudder(rudder)
        this.sendCommand()
    }

    fun setThrottle(throttle: Double) {
        this.command.setThrottle(throttle)
        this.sendCommand()
    }


    private fun sendCommand() {
        val gson = GsonBuilder().create()
        val jsonCommand = gson.toJson(this.command)
        val request = Request.Builder().post(jsonCommand.toRequestBody(MEDIA_TYPE_MARKDOWN))
            .url(this.url + "/api/command").build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                println("good" + response.body)
            }

            override fun onFailure(call: Call, e: IOException) {
                println("failed: ${call.request()}")
                println(e.message)
            }
        })

    }

    fun getImage():Future<Bitmap>{
        val answer:Future<Bitmap> = Future()
        val request = Request.Builder().get()
            .url(this.url + "/api/screenshot").build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    val byteStream = response.body?.byteStream()
                    answer.set(BitmapFactory.decodeStream(byteStream))
                } catch (e:Exception){
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