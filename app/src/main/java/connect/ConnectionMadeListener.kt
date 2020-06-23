package connect

import okhttp3.Call
import okhttp3.Connection
import okhttp3.EventListener

class ConnectionMadeListener(private var openConnectionsList:java.util.ArrayList<String>):EventListener() {
    override fun connectionAcquired(call: Call, connection: Connection) {
        super.connectionAcquired(call, connection)
        openConnectionsList.add(connection.route().address.url.host)
    }

}