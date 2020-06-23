package control

import okhttp3.internal.notifyAll
import okhttp3.internal.wait

class Future<V> {
    private var value: V? = null
    @Synchronized
    fun set(v: V) {
        this.value = v
        notifyAll()
    }

    @Synchronized
    fun get(): V {
        if (value == null) {
            try{
                wait()
            }catch (e:InterruptedException){}
        }
        return value!!
    }
}