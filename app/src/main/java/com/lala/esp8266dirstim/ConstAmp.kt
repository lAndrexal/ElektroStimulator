package com.lala.esp8266dirstim

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.lala.esp8266dirstim.databinding.ActivityConstAmpBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import android.text.InputFilter
import android.text.Spanned
import android.view.KeyEvent
import com.google.android.material.textfield.TextInputEditText

class ConstAmp : AppCompatActivity() {

    private val ESPip: String = "http://192.168.4.1/"
    private lateinit var request: Request
    private val client2 = OkHttpClient()
    var on_off: String = "0"
    private lateinit var binding: ActivityConstAmpBinding
    var flag1: Boolean = false


    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                if(flag1) validChangeRequest()
                true
            }

            else -> super.onKeyUp(keyCode, event)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConstAmpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AmplitudeTextListener()


    }


    private fun AmplitudeTextListener() {
        binding.edAMP.setOnFocusChangeListener { _, focused ->
            if (!focused && flag1) {
                validChangeRequest()
            }
        }
    }

    fun onClickResult(view: View) {

        flag1 = !flag1

        if (flag1) on_off = "1"
        if (!flag1) on_off = "0"
        validChangeRequest()
    }

    private fun isFieldEmpty(): Boolean {
        binding.apply {
            if (edAMP.text.isNullOrEmpty()) edAMP.error = "Поле должно быть заполнено"
            return edAMP.text.isNullOrEmpty()
        }
    }


    private fun createRequest(): String {

        return "parameters?Uamp=${binding.edAMP.text}&mode="

    }


    private fun post(post: String) {
        Thread {
            request = Request.Builder().url(post).build()

            try {
                var response = client2.newCall(request).execute()
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

    private fun validChangeRequest() {
        val webpath: String = ESPip + createRequest() + on_off

        if (!isFieldEmpty()) {
            post(webpath)
        }
    }


}