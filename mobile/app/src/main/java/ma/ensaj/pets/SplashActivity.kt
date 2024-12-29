package ma.ensaj.pets

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var logo1: ImageView
    private lateinit var logo2: ImageView
    private lateinit var logo3: ImageView
    private lateinit var logo4: ImageView
    private lateinit var logo5: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        logo1 = findViewById(R.id.logo1)
        logo2 = findViewById(R.id.logo2)
        logo3 = findViewById(R.id.logo3)
        logo4 = findViewById(R.id.logo4)
        logo5 = findViewById(R.id.logo5)

        //L'opacité
        logo1.alpha = 0f
        logo2.alpha = 0f
        logo3.alpha = 0f
        logo4.alpha = 0f
        logo5.alpha = 0f

        //LOGO1
        logo1.animate().translationYBy(200f).setDuration(2000)
        logo1.animate().alpha(1f).setDuration(3000)

        //LOGO2
        logo2.animate().rotation(720f).setDuration(200)
        logo2.animate().translationYBy(200f).setDuration(2000)
        logo2.animate().alpha(1f).setDuration(3000)

        //LOGO3
        logo3.animate().rotation(720f).setDuration(200)
        logo3.animate().translationYBy(200f).setDuration(2000)
        logo3.animate().alpha(1f).setDuration(3000)

        //LOGO4
        logo4.animate().rotation(720f).setDuration(200)
        logo4.animate().translationYBy(200f).setDuration(2000)
        logo4.animate().alpha(1f).setDuration(3000)

        //LOGO5
        logo5.animate().rotation(720f).setDuration(200)
        logo5.animate().translationYBy(200f).setDuration(2000)
        logo5.animate().alpha(1f).setDuration(3000)


        Thread {
            try {
                Thread.sleep(5000)
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish() // Ferme l'activité de splash
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()
    }
}