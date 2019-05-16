package unl.fct.smart_grow

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import android.support.v4.os.HandlerCompat.postDelayed
import kotlinx.android.synthetic.main.activity_register.*


class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setLoginListener()
        onClickShowPassword()
        noAccountYetRegister()
    }

    fun setLoginListener() {
        val login = findViewById<Button>(R.id.Login)
        login.setOnClickListener {


            val username = findViewById<EditText>(R.id.username_field).text.toString()
            val password = findViewById<EditText>(R.id.password_field).text.toString()

            if ((username == "" || password == "")) {

                Toast.makeText(this, "Please fill all fields and Try Again!", Toast.LENGTH_LONG).show()
            }else{
                val auth = HttpTask.loginToApi(username,password)

                if (auth) {
                    Toast.makeText(this, "Login Successfully", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, RegisterActivity::class.java))
                }
                else{
                    Toast.makeText(this, "Wrong username or password", Toast.LENGTH_LONG).show()
                }




            }
        }

    }

    private fun onClickShowPassword() {
        val showHidePassword = findViewById<CheckBox>(R.id.show_hide_password)
        showHidePassword.setOnClickListener {
            val passwordField = findViewById<EditText>(R.id.password_field)

            if (passwordField.transformationMethod is PasswordTransformationMethod) {
                passwordField.transformationMethod = HideReturnsTransformationMethod.getInstance();

            } else {
                passwordField.transformationMethod = PasswordTransformationMethod.getInstance();
               // showHidePassword.isChecked()
            }
        }
    }

    private fun noAccountYetRegister() {
        val registerJump = findViewById<TextView>(R.id.register_text)
        registerJump.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

     /*private fun loginToApi() : Boolean{

        val url = URL("https://api.smartgrow.space/login")
        val httpClient = url.openConnection() as HttpURLConnection

        httpClient.requestMethod = "POST"
        httpClient.instanceFollowRedirects = false
        httpClient.doOutput = true
        httpClient.doInput = true
        httpClient.useCaches = false
        httpClient.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        try{
            val username = findViewById<EditText>(R.id.username_field).text.toString()
            val password = findViewById<EditText>(R.id.password_field).text.toString()
            val json = JSONObject()
            json.put("username", username)
            json.put("password", password)
            httpClient.connect()
            val os = httpClient.getOutputStream()
            val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
            writer.write(json.toString())
            writer.flush()
            writer.close()
            os.close()

            if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                Toast.makeText(this, "Hello bitch", Toast.LENGTH_LONG).show()
                return true
        } else {
            println("ERROR ${httpClient.responseCode}")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        httpClient.disconnect()
    }

        return false

    }*/


}
    object HttpTask {


        fun loginToApi(username: String, password: String) : Boolean{

            val url = URL("https://api.smartgrow.space/login")
            val httpClient = url.openConnection() as HttpURLConnection

            httpClient.requestMethod = "POST"
            httpClient.instanceFollowRedirects = false
            httpClient.doOutput = true
            httpClient.doInput = true
            httpClient.useCaches = false
            httpClient.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            try{

                val json = JSONObject()
                json.put("username", username)
                json.put("password", password)
                httpClient.connect()
                val os = httpClient.getOutputStream()
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(json.toString())
                writer.flush()
                writer.close()
                os.close()

                if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                    return true
                } else {
                    println("ERROR ${httpClient.responseCode}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }

            return false

        }

    }
