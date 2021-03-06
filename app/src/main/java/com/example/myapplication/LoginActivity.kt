package com.example.myapplication

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    lateinit var etEmail: EditText
    lateinit var  etPassword: EditText
    val MIN_PASSWORD_LENGTH = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewInitializations()
    }

    fun viewInitializations() {
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)

        // To show back button in actionbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Checking if the input in form is valid
    fun validateInput(): Boolean {
        if (etEmail.text.toString() == "") {
            etEmail.error = "Please Enter Email"
            return false
        }
        if (etPassword.text.toString() == "") {
            etPassword.error = "Please Enter Password"
            return false
        }

        // checking the proper email format
        if (!isEmailValid(etEmail.text.toString())) {
            etEmail.error = "Please Enter Valid Email"
            return false
        }

        // checking minimum password Length
        if (etPassword.text.length < MIN_PASSWORD_LENGTH) {
            etPassword.error = "Password Length must be more than " + MIN_PASSWORD_LENGTH + "characters"
            return false
        }
        return true
    }


    fun isEmailValid(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Hook Click Event
    fun performSignUp(v: View) {
        if (validateInput()) {

            // Input is valid, here send data to your server
            val email = etEmail!!.text.toString()
            val password = etPassword!!.text.toString()
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
            // Here you can call you API
            // Check this tutorial to call server api through Google Volley Library https://handyopinion.com
            var volleyRequestQueue: RequestQueue? = null
            var dialog: ProgressDialog? = null
            val serverAPIURL: String = "https://handyopinion.com/tutorials/signup.php"
            val TAG = "Handy Opinion Tutorials"

            fun SendSignUpDataToServer(firstName: String, lastName: String, email: String, password: String) {
                volleyRequestQueue = Volley.newRequestQueue(this)
                dialog = ProgressDialog.show(this, "", "Please wait...", true);
                val parameters: MutableMap<String, String> = HashMap()
                // Add your parameters in HashMap
                parameters.put("email",email);
                parameters.put("password",password);

                val strReq: StringRequest = object : StringRequest(
                    Method.POST,serverAPIURL,
                    Response.Listener { response ->
                        Log.e(TAG, "response: " + response)
                        dialog?.dismiss()

                        // Handle Server response here
                        try {
                            val responseObj = JSONObject(response)
                            val isSuccess = responseObj.getBoolean("isSuccess")
                            val code = responseObj.getInt("code")
                            val message = responseObj.getString("message")
                            if (responseObj.has("data")) {
                                val data = responseObj.getJSONObject("data")
                                // Handle your server response data here
                            }
                            Toast.makeText(this,message,Toast.LENGTH_LONG).show()

                        } catch (e: Exception) { // caught while parsing the response
                            Log.e(TAG, "problem occurred")
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { volleyError -> // error occurred
                        Log.e(TAG, "problem occurred, volley error: " + volleyError.message)
                    }) {

                    override fun getParams(): MutableMap<String, String> {
                        return parameters;
                    }

                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {

                        val headers: MutableMap<String, String> = HashMap()
                        // Add your Header paramters here
                        return headers
                    }
                }
                // Adding request to request queue
                volleyRequestQueue?.add(strReq)
                fun goToDashboard() {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    fun goToSignup(v: View) {
        // Open your SignUp Activity if the user wants to signup
        // Visit this article to get SignupActivity code https://handyopinion.com/signup-activity-in-android-studio-kotlin-java/
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }
}