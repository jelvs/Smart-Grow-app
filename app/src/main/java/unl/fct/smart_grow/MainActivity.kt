package unl.fct.smart_grow

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setLoginListener()
    }

    fun setLoginListener() {
        val login = findViewById<Button>(R.id.Login)
        login.setOnClickListener {

            val username = findViewById<EditText>(R.id.username_field).text.toString()
            val password = findViewById<EditText>(R.id.password_field).text.toString()

           Toast.makeText(this, "username: $username, password: $password", Toast.LENGTH_LONG).show()
        }

    }
}
