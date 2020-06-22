package control

import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class Client(val url: String) {
    private var command: Command = Command(0.0, 0.0, 0.0, 0.0)
    private val httpClient = OkHttpClient()

    fun setCommand(command: Command) {
        this.command = command
    }

    fun setFromJoystick(aileron: Double, elevator: Double) {
        this.command.setAileron(aileron)
        this.command.setElevator(elevator)
        this.sendCommand()
    }

    fun setFromSliders(rudder: Double, throttle: Double) {
        this.command.setRudder(rudder)
        this.command.setThrottle(throttle)
        this.sendCommand()
    }

    private fun sendCommand() {
        val gson = GsonBuilder().create()
        val jsonCommand = gson.toJson(this.command)
        val request = Request.Builder().post(jsonCommand.toRequestBody(MEDIA_TYPE_MARKDOWN))
            .url(this.url + "/command").build()
        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                println(response.message)
            } else {
                println(response.body)
            }
        }
    }

    companion object {
        val MEDIA_TYPE_MARKDOWN = "text/x-markdown; charset=utf-8".toMediaType()
    }

}