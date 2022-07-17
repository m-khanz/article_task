package  com.task.nytimesarticles.common
import java.util.*

fun isNight(): Boolean {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return (currentHour <= 7 || currentHour >= 18)// true if time is between 6PM -7AM
}
