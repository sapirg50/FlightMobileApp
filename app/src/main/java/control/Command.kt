package control

data class Command(
    private var aileron: Double,
    private var elevator: Double,
    private var rudder: Double,
    private var throttle: Double
) {

    fun setAileron(value: Double) {
        this.aileron = value;
    }

    fun setElevator(value: Double) {
        this.elevator = value;
    }

    fun setRudder(value: Double) {
        this.rudder = value;
    }

    fun setThrottle(value: Double) {
        this.throttle = value;
    }

}