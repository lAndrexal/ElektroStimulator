package com.lala.esp8266dirstim

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lala.esp8266dirstim.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val ESPip: String = "http://192.168.4.1"
    private lateinit var request: Request
    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: SharedPreferences
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = getSharedPreferences("MyPref", MODE_PRIVATE);

        binding.apply {
            b1.setOnClickListener(onClickListener())
            b2.setOnClickListener(onClickListener())
            b3.setOnClickListener(onClickListener())
            b4.setOnClickListener(onClickListener())
        }
    }

    override fun onResume() {
        super.onResume()
        post("resume")
    }


    private fun onClickListener(): View.OnClickListener {
        return View.OnClickListener {
            when (it.id) {
                R.id.b1 -> {
                    post("square")
                    val intent1 = Intent(this, SquareImpuls::class.java)
                    startActivity(intent1)
                }
                R.id.b2 -> {
                    post("Const")
                    val intent1 = Intent(this, ConstAmp::class.java)
                    startActivity(intent1)
                }
                R.id.b3 -> {
                    post("single")
                    val intent1 = Intent(this, Single::class.java)
                    startActivity(intent1)
                }
                R.id.b4 -> {
                    post("rise")
                    val intent1 = Intent(this, RiseSignal::class.java)
                    startActivity(intent1)
                }
            }
        }

    }


    private fun post(post: String) {
        Thread {
            request = Request.Builder().url("$ESPip/$post").build()

            try {
                var response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val resultText = response.body()?.string()
                    runOnUiThread {
                        //посылка от ESP
                    }
                }
            } catch (i: IOException) {
            }
        }.start()
    }
}