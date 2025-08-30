package com.sayed.bandhan.UI

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.sayed.bandhan.Data.AppDatabase
import com.sayed.bandhan.Data.User
import com.sayed.bandhan.UI.MainActivity
import com.sayed.bandhan.R
import com.sayed.bandhan.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.getDatabase(this) //getting the database instance

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val passwordHash = hashPassword(password)
                val user = db.userDao().login(username, passwordHash)

                if (user != null) {
                    Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.signupLink.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please enter a username and password to sign up", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Perform user registration in a coroutine
            lifecycleScope.launch {
                val existingUser = db.userDao().getUserByUsername(username)
                if (existingUser != null) {
                    Toast.makeText(this@LoginActivity, "User already exists. Please login.", Toast.LENGTH_SHORT).show()
                } else {
                    val passwordHash = hashPassword(password)
                    val newUser = User(username, passwordHash)
                    db.userDao().insertUser(newUser)
                    Toast.makeText(this@LoginActivity, "Account created successfully! Please log in.", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
    private fun hashPassword(password: String): String {
        return password.hashCode().toString()
    }
}