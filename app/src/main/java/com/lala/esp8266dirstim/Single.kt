package com.lala.esp8266dirstim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lala.esp8266dirstim.databinding.ActivitySingleBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class Single : AppCompatActivity() {

    private val ESPip: String = "http://192.168.4.1/"
    private lateinit var request: Request
    private val client3 = OkHttpClient()
    var on_off: String = "0"
    private lateinit var binding: ActivitySingleBinding
    var flag1: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun onClickResult(view: View) {

        flag1 = !flag1

        if (flag1) on_off = "1"
        if (!flag1) on_off = "0"

        val webpath: String = ESPip + createRequest() + on_off
        if (!isFieldEmpty()) {
            post(webpath)
        }


    }

    private fun isFieldEmpty(): Boolean {
        binding.apply {
            if (edAMP.text.isNullOrEmpty()) edAMP.error = "Поле должно быть заполнено"
            if (edTimp.text.isNullOrEmpty()) edTimp.error = "Поле должно быть заполнено"
            return edAMP.text.isNullOrEmpty() || edTimp.text.isNullOrEmpty()
        }
    }

    private fun createRequest(): String {

        return "parameters?Uamp=${binding.edAMP.text}&Timp=${binding.edTimp.text}&mode="

    }


    private fun post(post: String) {
        Thread {
            request = Request.Builder().url(post).build()

            try {
                var response = client3.newCall(request).execute()
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