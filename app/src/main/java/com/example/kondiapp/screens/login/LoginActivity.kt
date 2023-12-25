package com.example.kondiapp.screens.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
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
import com.example.kondiapp.databinding.ActivityMainBinding

class LoginActivity : ComponentActivity() {
    lateinit var sharedPreferences: SharedPreferences

    var PREFS_KEY = "prefs"
    var EMAIL_KEY = "email"
    var PWD_KEY = "password"
    var e = ""
    var p = ""


    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = SessionManager.getToken(this)

        setContent {
            val ctx = LocalContext.current
            sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
            val email = remember {
                mutableStateOf("")
            }
            val pwd = remember{
                mutableStateOf("")
            }
            email.value = sharedPreferences.getString(EMAIL_KEY,"").toString()
            pwd.value = sharedPreferences.getString(PWD_KEY,"").toString()

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

                        //Text("Login", modifier= Modifier.fillMaxWidth())

                    }
                    viewModel.loginResult.observe(this){
                        when(it){
                            is BaseResponse.Success->{
                                processLogin(it.data, ctx)
                                Log.wtf("observeresult:",it.data.toString())
                            }
                            is BaseResponse.Error->{
                                processError(it.msg)
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
        val activity = (this as? Activity)
        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

        val email = sharedPreferences.getString(EMAIL_KEY, "").toString()
        val pwd = sharedPreferences.getString(PWD_KEY, "").toString()

        if (email != "" && pwd != "") {
            val i = Intent(this, UserActivity::class.java)
            startActivity(i)
            activity?.finish()
        }
    }


    fun processLogin(data: LoginResponse?, ctx:Context){
        showToast("Success:"+ data?.message)
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
    fun processError(msg:String?){
        showToast("Error:" + msg)
    }
    fun showToast(msg: String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
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
                    if(it.data?.token.isNullOrEmpty()){
                        it.data?.token?.let{
                            SessionManager.saveAuthToken(this,it)
                            val i = Intent(ctx, UserActivity::class.java)
                            ctx.startActivity(i)
                        }
                }
            }
                else->{

                }
            }

        }

        // on below line we are creating and
        // initializing email value and pwd value
        val emailValue = remember {
            mutableStateOf(TextFieldValue())
        }
        val pwdValue = remember {
            mutableStateOf(TextFieldValue())
        }
        val UserIdValue = remember {
            mutableStateOf("")
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

                text = "Login to the KondiApp!",
                color = Green,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
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
                    editor.putString("email", emailValue.value.text)
                    editor.putString("pwd", emailValue.value.text)
                    editor.apply()
                    //////////
                    //saveData(emailValue.value.text, pwdValue.value.text, sharedPreferences, ctx)
                    //activity?.finish()

                    viewModel.loginUser(email = emailValue.value.text, pwd = pwdValue.value.text/*,ctx=ctx*/)

                    Log.wtf("Pls2:",viewModel.userId.toString())
                    //val i = Intent(ctx, UserActivity::class.java)
                    //ctx.startActivity(i)
                }
            )

            // on the below line we are adding text as login.
            {
                Text(text = "Login")

            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick={
                    val i = Intent(ctx, RegisterActivity::class.java)
                    ctx.startActivity(i)
                }
            ){
                Text(text="Register")
            }

        }
    }

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
