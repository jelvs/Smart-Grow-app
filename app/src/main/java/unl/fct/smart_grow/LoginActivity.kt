package unl.fct.smart_grow

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import org.json.JSONObject
import unl.fct.smart_grow.http.HttpTask
import unl.fct.smart_grow.security.MockJwtStorage
import unl.fct.smart_grow.utils.ApiConfig

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
            } else {
                val spinner = findViewById<ProgressBar>(R.id.loginLoading)
                spinner.visibility = View.VISIBLE

                val json = JSONObject()
                json.put("username", username)
                json.put("password", password)

                HttpTask (this) {
                    if (it == null) {
                        Toast.makeText(this, "Wrong username or password", Toast.LENGTH_LONG).show()

                        val spinner = findViewById<ProgressBar>(R.id.loginLoading)
                        spinner.visibility = View.GONE

                        return@HttpTask
                    } else {
                        Toast.makeText(this, "Login Successfully", Toast.LENGTH_LONG).show()

                        val spinner = findViewById<ProgressBar>(R.id.loginLoading)
                        spinner.visibility = View.GONE

                        println(it)
                        MockJwtStorage.login(it)
                        startActivity(Intent(this, DashboardActivity::class.java))
                    }

                }.execute("POST", "${ApiConfig.smartGrowApi}/login", json.toString())
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

    override fun onBackPressed() {
        // do nothing
    }
}
