package id.co.multipleprogressbarsection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val dummyData by lazy {
        mutableListOf(
            ProgressItem("Video", android.R.color.holo_blue_light, 20F),
            ProgressItem("Topic Exercise", android.R.color.holo_orange_light, 30F),
            ProgressItem("Bank Soal", android.R.color.holo_red_light, 10F),
            ProgressItem("Quiz", android.R.color.holo_purple, 15F),
            ProgressItem("Other", android.R.color.holo_green_light, 25F)
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
