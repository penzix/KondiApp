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
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.kondiapp.ApiInterface
import com.example.kondiapp.CommentList
import com.example.kondiapp.CommentViewModel
import com.example.kondiapp.ExerciseViewModel
import com.example.kondiapp.LoginViewModel
import com.example.kondiapp.RetrofitClient
import com.example.kondiapp.SessionManager
import com.example.kondiapp.ui.theme.KondiAppTheme

class AddExerciseActivity : ComponentActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val vm = ExerciseViewModel()
        super.onCreate(savedInstanceState)

        setContent {
            KondiAppTheme {
                val ctx = LocalContext.current

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                    ) {
                        addExerciseHeader(ctx)
                        getExercisesToAdd(vm)


                    }
                }
            }
        }
    }

}

@Composable
fun addExerciseHeader(ctx : Context){
    val activity = (LocalContext.current as? Activity)
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        IconButton(
            onClick = {
                val i = Intent(ctx, EditExerciseActivity::class.java)
                ctx.startActivity(i)
                activity?.finish()
            }
        ) {
            Icon(
                Icons.Filled.Backspace,
                contentDescription = "Back To Edit Exercises",
                tint = Color.Blue
            )
        }
        IconButton(
            onClick = {
                val i = Intent(ctx, DietPlanActivity::class.java)
                ctx.startActivity(i)
                activity?.finish()
            }
        ) {
            Icon(
                Icons.Filled.Fastfood,
                contentDescription = "To Diet Plan",
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

@Composable
fun getExercisesToAdd(viewModel: ExerciseViewModel) {

    val ctx = LocalContext.current
    val token = SessionManager.getToken(ctx).toString()

    val exercise_type = remember {
        mutableStateOf(TextFieldValue())
    }
    val set = remember {
        mutableStateOf("")
    }
    val repetition = remember {
        mutableStateOf("")
    }
    val weight = remember {
        mutableStateOf("")
    }

    var isValidText by remember { mutableStateOf(true) }
    var textInput by remember { mutableStateOf("") }
    var isValidSet by remember { mutableStateOf(true) }
    var setInput by remember { mutableStateOf("") }
    var isValidRepetition by remember { mutableStateOf(true) }
    var repetitionInput by remember { mutableStateOf("") }
    var isValidWeight by remember { mutableStateOf(true) }
    var weightInput by remember { mutableStateOf("") }

    Column(
        Modifier.background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

            TextField(
                value = exercise_type.value,
                onValueChange = { input->
                    exercise_type.value = input
                    isValidText = exercise_type.value.text.isNotEmpty()
                },
                //onValueChange = { exercise_type.value = it },
                placeholder = { Text(text = "Enter your exercise's name") },
                isError = !isValidText,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Comment, contentDescription = "Comment")
                }
            )
        if(!isValidText){
            Text("Please enter your exercise's name", color = Color.Red)
        }



            TextField(
                value = set.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { input->
                    if(input.isDigitsOnly()){
                        set.value = input
                        isValidSet = set.value.isDigitsOnly() && set.value.isNotEmpty()
                    }
                },
                //onValueChange = {if(it.isDigitsOnly()) set.value = it },
                placeholder = { Text(text = "Enter your set's number") },
                isError = !isValidSet,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Numbers, contentDescription = "Set number")
                }
            )
        if(!isValidSet){
            Text("Please enter your set's valid number", color = Color.Red)
        }


            TextField(
                value = repetition.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { input->
                    if(input.isDigitsOnly()){
                        repetition.value = input
                        isValidRepetition = repetition.value.isDigitsOnly() && repetition.value.isNotEmpty()
                    }
                },
                //onValueChange = { if(it.isDigitsOnly()) repetition.value = it },
                placeholder = { Text(text = "Enter your repetition's number") },
                isError = !isValidRepetition,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Numbers, contentDescription = "Repetition number")
                }
            )
        if(!isValidRepetition){
            Text("Please enter your repetition's valid number", color = Color.Red)
        }


            TextField(
                value = weight.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { input->
                    if(input.isDigitsOnly()){
                        weight.value = input
                        isValidWeight = weight.value.isDigitsOnly() && weight.value.isNotEmpty()
                    }

                },

                //onValueChange = {if(it.isDigitsOnly()) weight.value = it },
                placeholder = { Text(text = "Enter your weight's number") },
                isError = !isValidWeight,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Numbers, contentDescription = "Weight number")
                }
            )
        if(!isValidWeight){
            Text("Please enter your weight's valid number", color = Color.Red)
        }
        }


        Row {
            Button(
                onClick = {

                    isValidText = exercise_type.value.text.isNotEmpty()
                    isValidSet = set.value.isDigitsOnly() && set.value.isNotEmpty()
                    isValidRepetition = repetition.value.isDigitsOnly() && repetition.value.isNotEmpty()
                    isValidWeight = weight.value.isDigitsOnly() && weight.value.isNotEmpty()

                    if(isValidText && isValidSet && isValidRepetition && isValidWeight){
                        viewModel.sendExercise(
                            SessionManager.getCurrentExercisePlanId(),
                            SessionManager.currendDayIdFromDay(),
                            exercise_type.value.text,
                            set.value.toInt(),
                            repetition.value.toInt(),
                            weight.value.toInt(),
                            "Bearer $token"
                        )
                        val i = Intent(ctx, EditExerciseActivity::class.java)
                        ctx.startActivity(i)
                    }


                    //activity?.finish()


                }
            ) {
                Text("Save exercise")
            }


        }



}
