package com.lala.esp8266dirstim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.lala.esp8266dirstim.databinding.ActivityRiseSignalBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class RiseSignal : AppCompatActivity() {

    private val ESPip: String = "http://192.168.4.1/"
    private lateinit var request: Request
    private val client4 = OkHttpClient()
    var on_off: String = "0"
    private lateinit var binding: ActivityRiseSignalBinding
    var flag1: Boolean = false
    var Amp1: Int = 0;
    var Amp2: Int = 0;
    var T: Int = 0;

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                if(flag1) validChangeRequest(true)
                true
            }

            else -> super.onKeyUp(keyCode, event)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiseSignalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Amplitude1TextListener()
        Amplitude2TextListener()
        TTextListener()
    }

    private fun Amplitude1TextListener() {
        binding.edAMP1.setOnFocusChangeListener { _, focused ->
            if (!focused && flag1) {
                validChangeRequest(false)
            }
        }
    }

    private fun Amplitude2TextListener() {
        binding.edAMP2.setOnFocusChangeListener { _, focused ->
            if (!focused && flag1) {
                validChangeRequest(false)
            }
        }
    }

    private fun TTextListener() {
        binding.edT.setOnFocusChangeListener { _, focused ->
            if (!focused && flag1) {
                validChangeRequest(false)
            }
        }
    }



    fun onClickResult(view: View) {

        flag1 = !flag1

        if (flag1) on_off = "1"
        if (!flag1) on_off = "0"
        validChangeRequest(true)

    }

    private fun isFieldEmpty(): Boolean {
        binding.apply {
            if (edAMP1.text.isNullOrEmpty()) edAMP1.error = "Поле должно быть заполнено"
            if (edT.text.isNullOrEmpty()) edT.error = "Поле должно быть заполнено"
            if (edAMP2.text.isNullOrEmpty()) edAMP2.error = "Поле должно быть заполнено"
            return edAMP1.text.isNullOrEmpty() || edT.text.isNullOrEmpty() || edAMP2.text.isNullOrEmpty()
        }
    }

    private fun createRequest(): String {

        return "parameters?Uamp1=${binding.edAMP1.text}&Uamp2=${binding.edAMP2.text}&T=${binding.edT.text}&mode="

    }


    private fun post(post: String) {
        Thread {
            request = Request.Builder().url(post).build()

            try {
                var response = client4.newCall(request).execute()
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


    private fun validChangeRequest(flag: Boolean) {
        val webpath: String = ESPip + createRequest() + on_off

        if (!isFieldEmpty()
            && (Amp1 != binding.edAMP1.text.toString().toInt()
                    || Amp2 != binding.edAMP2.text.toString().toInt()
                    || T != binding.edT.text.toString().toInt()
                    || flag)
        ) {
            binding.apply {
                Amp1 = edAMP1.text.toString().toInt();
                Amp2 = edAMP2.text.toString().toInt();
                T = edT.text.toString().toInt();
            }
            post(webpath)
        }
    }
}