package com.example.kondiapp.screens.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.kondiapp.ApiInterface
import com.example.kondiapp.CommentList
import com.example.kondiapp.CommentViewModel
import com.example.kondiapp.DietPlanViewModel
import com.example.kondiapp.LoginViewModel
import com.example.kondiapp.RetrofitClient
import com.example.kondiapp.SessionManager
import com.example.kondiapp.ui.theme.KondiAppTheme

class DietPlanActivity : ComponentActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val vm = DietPlanViewModel()
        super.onCreate(savedInstanceState)

        setContent {
            KondiAppTheme {
                val ctx = LocalContext.current
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                    ) {
                        dietPlanHeader(ctx)
                        dietPlanBody(vm)
                    }
                }
            }
        }
    }

}


@Composable
fun dietPlanHeader(ctx : Context){
    val activity = (LocalContext.current as? Activity)
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            IconButton(
                onClick = {
                    val i = Intent(ctx, UserActivity::class.java)
                    ctx.startActivity(i)
                    activity?.finish()
                }
            ) {
                Icon(
                    Icons.Filled.Backspace,
                    contentDescription = "Back To Comment Plan",
                    tint = Color.Blue
                )
            }
            IconButton(
                onClick = {
                    val i = Intent(ctx, ExercisePlanActivity::class.java)
                    ctx.startActivity(i)
                    activity?.finish()
                }
            ) {
                Icon(
                    Icons.Filled.DirectionsRun,
                    contentDescription = "To Exercise Plan",
                    tint = Color.Blue
                )
            }

            IconButton(
                onClick = {
                    val i = Intent(ctx, UserActivity::class.java)
                    ctx.startActivity(i)
                    activity?.finish()
                }
            ) {
                Icon(
                    Icons.Filled.Comment,
                    contentDescription = "To Comment Feed",
                    tint = Color.Blue
                )
            }
            IconButton(
                onClick = {
                    val i = Intent(ctx, LoginActivity::class.java)
                    ctx.startActivity(i)
                    activity?.finish()
                }
            ) {
                Icon(
                    Icons.Filled.ExitToApp,
                    contentDescription = "Log Out",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun dietPlanBody(viewModel: DietPlanViewModel) {

    val ctx = LocalContext.current
    val activity = (LocalContext.current as? Activity)

    val currentUserId = SessionManager.currentUserId(ctx)
    val token = SessionManager.getToken(ctx);

    val dietplan1 = mutableSetOf<Unit>()

    Log.wtf("DietPlanToken",token.toString())
    Log.wtf("DietPlanuser",currentUserId.toString())


    LaunchedEffect(Unit, block = {
        //viewModel.getDietPlan(currentUserId,1,"Monday","Bearer $token")
    })


    Column(){
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    SessionManager.saveDietPlanId(1)
                    val i = Intent(ctx, ChosenDietPlanActivity::class.java)
                    ctx.startActivity(i)
                }
            )
            {
                Text(text = "To regular diet plan")
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    SessionManager.saveDietPlanId(2)
                    val i = Intent(ctx, ChosenDietPlanActivity::class.java)
                    ctx.startActivity(i)
                }
            )
            {
                Text(text = "To vegan diet plan")
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    SessionManager.saveDietPlanId(3)
                    val i = Intent(ctx, ChosenDietPlanActivity::class.java)
                    ctx.startActivity(i)
                }
            )
            {
                Text(text = "To lactose free plan")
            }

    }

}
