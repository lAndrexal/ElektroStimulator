package com.lala.esp8266dirstim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.lala.esp8266dirstim.databinding.ActivitySquareImpulsBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class SquareImpuls : AppCompatActivity() {

    private val ESPip: String = "http://192.168.4.1/"
    private lateinit var request: Request
    private val client1 = OkHttpClient()
    var on_off: String = "0"
    private lateinit var binding: ActivitySquareImpulsBinding
    var flag1: Boolean = false
    var Amp: Int = 0;
    var Timp: Int = 0;
    var T: Int = 0;

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                if (flag1) validChangeRequest(true)
                true
            }

            else -> super.onKeyUp(keyCode, event)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySquareImpulsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AmplitudeTextListener()
        TimpTextListener()
        TTextListener()
    }

    private fun AmplitudeTextListener() {
        binding.edAMP.setOnFocusChangeListener { _, focused ->
            if (!focused && flag1) {
                validChangeRequest(false)
            }
        }
    }

    private fun TimpTextListener() {
        binding.edTimp.setOnFocusChangeListener { _, focused ->
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
            if (edAMP.text.isNullOrEmpty()) edAMP.error = "Поле должно быть заполнено"
            if (edT.text.isNullOrEmpty()) edT.error = "Поле должно быть заполнено"
            if (edTimp.text.isNullOrEmpty()) edTimp.error = "Поле должно быть заполнено"
            return edAMP.text.isNullOrEmpty() || edT.text.isNullOrEmpty() || edTimp.text.isNullOrEmpty()
        }
    }

    private fun createRequest(): String {

        return "parameters?Uamp=${binding.edAMP.text}&T=${binding.edTimp.text}&Timp=${binding.edT.text}&mode="

    }


    private fun post(post: String) {
        Thread {
            request = Request.Builder().url(post).build()

            try {
                var response = client1.newCall(request).execute()
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

        if (binding.edTimp.text.toString().toInt()<binding.edT.text.toString().toInt()) {
            if (!isFieldEmpty()
                && (Amp != binding.edAMP.text.toString().toInt()
                        || Timp != binding.edTimp.text.toString().toInt()
                        || T != binding.edT.text.toString().toInt()
                        || flag)
            ) {
                binding.apply {
                    Amp = edAMP.text.toString().toInt();
                    Timp = edTimp.text.toString().toInt();
                    T = edT.text.toString().toInt();
                }
                post(webpath)
            }
        }

        else
        {
            Toast.makeText(this, "Тимп>Т!",Toast.LENGTH_SHORT).show()
        }

    }

}