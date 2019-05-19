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
import unl.fct.smart_grow.http.HttpTask

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setLoginListener()
        onClickShowPassword()
        noAccountYetRegister()
    }

    private fun setLoginListener() {
        val login = findViewById<Button>(R.id.Login)
        login.setOnClickListener {


            val username = findViewById<EditText>(R.id.username_field).text.toString()
            val password = findViewById<EditText>(R.id.password_field).text.toString()

            if ((username == "" || password == "")) {

                Toast.makeText(this, "Please fill all fields and Try Again!", Toast.LENGTH_LONG).show()
            }else{
                //val auth = HttpTask.loginToApi(username,password)
                val json = JSONObject()
                json.put("username", username)
                json.put("password", password)
                HttpTask {
                    if (it == null) {
                        Toast.makeText(this, "Wrong username or password", Toast.LENGTH_LONG).show()
                        return@HttpTask
                    }else{
                        Toast.makeText(this, "Login Successfully", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, DashboardActivity::class.java))
                        println(it)
                    }

                }.execute("POST", "https://api.smartgrow.space/login", json.toString())
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
}
