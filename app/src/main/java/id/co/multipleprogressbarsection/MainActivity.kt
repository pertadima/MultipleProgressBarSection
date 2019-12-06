package id.co.multipleprogressbarsection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val dummyData by lazy {
        hashMapOf(
            android.R.color.holo_blue_light to 20F,
            android.R.color.holo_orange_light to 30F,
            android.R.color.holo_red_light to 10F,
            android.R.color.holo_purple to 15F,
            android.R.color.holo_green_light to 25F
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        with(progress_section) {
            thumb.mutate().alpha = 0
            initData(dummyData)
            invalidate()
        }
    }
}
