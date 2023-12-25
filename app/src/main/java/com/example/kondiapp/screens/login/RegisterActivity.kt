package com.example.kondiapp.screens.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kondiapp.ui.theme.KondiAppTheme
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.kondiapp.ApiInterface
import com.example.kondiapp.RetrofitClient
import androidx.lifecycle.lifecycleScope
import com.example.kondiapp.BaseResponse
import com.example.kondiapp.LoginResponse
import com.example.kondiapp.LoginViewModel
import com.example.kondiapp.SessionManager
import com.example.kondiapp.RegisterViewModel

class RegisterActivity : ComponentActivity() {
    lateinit var sharedPreferences: SharedPreferences

    var PREFS_KEY = "prefs"
    var NAME_KEY = "name"
    var EMAIL_KEY = "email"
    var PWD_KEY = "password"
    var n = ""
    var e = ""
    var p = ""


    private val viewModel by viewModels<LoginViewModel>()
    private val viewModel2 by viewModels<RegisterViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = SessionManager.getToken(this)

        setContent {
            val ctx = LocalContext.current
            sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
            val name = remember {
                mutableStateOf("")
            }
            val email = remember {
                mutableStateOf("")
            }
            val pwd = remember{
                mutableStateOf("")
            }
            name.value = sharedPreferences.getString(NAME_KEY,"").toString()
            email.value = sharedPreferences.getString(EMAIL_KEY,"").toString()
            pwd.value = sharedPreferences.getString(PWD_KEY,"").toString()

            n = name.value
            e = email.value
            p = pwd.value
            KondiAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    Column(
                        Modifier.background(Color.White),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center

                    ) {

                    }
                    viewModel.loginResult.observe(this){
                        when(it){
                            is BaseResponse.Success->{
                                processLogin(it.data, ctx)
                                Log.wtf("observeresult:",it.data.toString())
                            }
                            else->{

                            }
                        }
                    }
                    SessionManagement(sharedPreferences = sharedPreferences)


                }
            }
        }


    }
    override fun onStart(){
        super.onStart()
        // on below line we are creating a variable for activity.
        val activity = (this as? Activity)

        // on below line we are initializing our shared preferences.
        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

        // on below line we are initializing our email and pwd
        // variable setting values from shared preferences.
        val name = sharedPreferences.getString(NAME_KEY,"").toString()
        val email = sharedPreferences.getString(EMAIL_KEY, "").toString()
        val pwd = sharedPreferences.getString(PWD_KEY, "").toString()

        // on below line we are checking if email and pwd are empty or not.
        if (name != "" && email != "" && pwd != "") {
            // if email and pwd are not empty we are opening
            // a new activity on below line.
            val i = Intent(this, LoginActivity::class.java)

            // on below line we are starting our new activity
            // and finishing our current activity.
            startActivity(i)
            activity?.finish()
        }
    }


    fun processLogin(data: LoginResponse?, ctx:Context){
        if(!data?.token.isNullOrEmpty()){
            data?.token?.let{
                SessionManager.saveAuthToken(this,it)
                SessionManager.saveCurrentUserId(this,data.id)
                Log.wtf("SessionManagerIdMentés:",data.id.toString())
                Log.wtf("SessionManagerIdLekérés:",SessionManager.currentUserId(this).toString())
                val i = Intent(ctx, UserActivity::class.java)
                ctx.startActivity(i)
            }

        }
    }
    @Composable
    fun SessionManagement(sharedPreferences: SharedPreferences) {

        // on below line we are creating a variable for
        // context and activity and initializing it.
        val ctx = LocalContext.current
        val activity = (LocalContext.current as? Activity)

        viewModel.loginResult.observe(this){

            when (it){
                is BaseResponse.Success->{
                    processLogin(it.data, ctx)
                    Log.wtf("observeresult:",it.data.toString())
                }
                else->{

                }
            }

        }

        // on below line we are creating and
        // initializing email value and pwd value

        val nameValue = remember {
            mutableStateOf(TextFieldValue())
        }
        val emailValue = remember {
            mutableStateOf(TextFieldValue())
        }
        val pwdValue = remember {
            mutableStateOf(TextFieldValue())
        }

        // on the below line we are creating a column.
        Column(
            // on below line we are adding a modifier to it
            // and setting max size, max height and max width
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight()
                .fillMaxWidth(),

            // on below line we are adding vertical
            // arrangement and horizontal alignment.
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // on below line we are creating a text
            Text(
                text = "Register to the KondiApp!",
                color = Green,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
            )

            TextField(
                value = nameValue.value,
                onValueChange = { nameValue.value = it },
                placeholder = { Text(text = "Enter your name") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Badge, contentDescription = "Name")
                }
            )

            TextField(
                value = emailValue.value,
                onValueChange = { emailValue.value = it },
                placeholder = { Text(text = "Enter your email") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "Email")
                }
            )


            // on below line we are creating a text field for password.
            TextField(

                value = pwdValue.value,
                onValueChange = { pwdValue.value = it },
                placeholder = { Text(text = "Enter your password") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Password, contentDescription = "Email")
                }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    /////////
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("name",nameValue.value.text)
                    editor.putString("email", emailValue.value.text)
                    editor.putString("pwd", emailValue.value.text)
                    editor.apply()
                    //////////
                    //saveData(emailValue.value.text, pwdValue.value.text, sharedPreferences, ctx)
                    //activity?.finish()

                    viewModel2.registerUser(name = nameValue.value.text, email = emailValue.value.text, pwd = pwdValue.value.text,ctx=ctx)
                    //val i = Intent(ctx, UserActivity::class.java)
                    //ctx.startActivity(i)
                }
            )
            // on the below line we are adding text as login.
            {
                Text(text = "Register")

            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick={
                    val i = Intent(ctx, LoginActivity::class.java)
                    ctx.startActivity(i)
                }
            ){
                Text(text="Back to login")
            }

        }
    }

    // on below line we are creating a function as save data
// to save data in our shared preferences.
    fun saveData(email: String, pwd: String, sharedPreferences: SharedPreferences, context: Context) {
        // on below line we are creating an editor and initializing
        // it with shared preferences.
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        // on below line we are setting email and pwd value with key.
        editor.putString("email", email)
        editor.putString("pwd", pwd)

        // on the below line we are applying
        // changes to our shared prefs.
        editor.apply()

        // on below line we are opening a new intent.
        val i = Intent(context, UserActivity::class.java)
        context.startActivity(i)
    }

}
