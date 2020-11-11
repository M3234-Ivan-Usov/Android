package weather.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import java.text.DateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val daysTitle = arrayOf(
        R.string.sunday,
        R.string.monday,
        R.string.tuesday,
        R.string.wednesday,
        R.string.thursday,
        R.string.friday,
        R.string.saturday
    )

    private lateinit var changeTheme: Button
    private lateinit var date: TextView

    private lateinit var nextDays: Array<TextView>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        initialiseFields()
    }

    private fun initialiseFields() {
        date = findViewById(R.id.date)
        changeTheme = findViewById(R.id.weather)
        nextDays = arrayOf(
            findViewById(R.id.day1),
            findViewById(R.id.day2),
            findViewById(R.id.day3),
            findViewById(R.id.day4),
            findViewById(R.id.day5),
            findViewById(R.id.day6)
        )
        changeTheme = findViewById(R.id.weather)
        fillDays()
    }

    private fun fillDays() {
        val now = Date()
        date.text = DateFormat.getDateInstance(DateFormat.FULL, Locale("en")).format(now)
        var day = Calendar.getInstance(Locale("en")).get(Calendar.DAY_OF_WEEK)
        for (d in 0..5) {
            day += 1
            if (day == Calendar.SATURDAY + 1) {
                day = Calendar.SUNDAY
            }
            nextDays[d].text = resources.getText(daysTitle[day - 1])
        }
    }

}