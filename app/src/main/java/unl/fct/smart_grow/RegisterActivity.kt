package unl.fct.smart_grow

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*

class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setRegisterListener()
        alreadyAccount()
        onClickShowPassword()


    }

    fun setRegisterListener() {
        val login = findViewById<Button>(R.id.Register)
        login.setOnClickListener {


            val username = findViewById<EditText>(R.id.username_field).text.toString()
            val password = findViewById<EditText>(R.id.password_field).text.toString()
            val repeatPassword = findViewById<EditText>(R.id.password_repeat).text.toString()

            if ((username == "" || password == "" || repeatPassword == "")) {
                Toast.makeText(this, "Fields Missing", Toast.LENGTH_LONG).show()
            } else if (password != repeatPassword) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_LONG).show()
            } else {


                val auth = HttpTask.loginToApi(username, password)

                if (auth) {
                    Toast.makeText(this, "Register Successfully", Toast.LENGTH_LONG).show()
                    //  startActivity(Intent(this, RegisterActivity::class.java))
                } else {
                    Toast.makeText(this, "Wrong username or password", Toast.LENGTH_LONG).show()
                }

            }

        }
    }

    private fun alreadyAccount() {
        val registerJump = findViewById<TextView>(R.id.login_redirect)
        registerJump.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun onClickShowPassword() {
        val showHidePassword = findViewById<CheckBox>(R.id.show_hide_password)

        showHidePassword.setOnClickListener {
            val passwordField = findViewById<EditText>(R.id.password_field)
            val repeatPassword = findViewById<EditText>(R.id.password_repeat)

            if ((passwordField.transformationMethod is PasswordTransformationMethod)
                && (repeatPassword.transformationMethod is PasswordTransformationMethod)
            ) {
                passwordField.transformationMethod = HideReturnsTransformationMethod.getInstance()
                repeatPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()

            } else {
                passwordField.transformationMethod = PasswordTransformationMethod.getInstance()
                repeatPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                // showHidePassword.isChecked()
            }
        }
    }
}