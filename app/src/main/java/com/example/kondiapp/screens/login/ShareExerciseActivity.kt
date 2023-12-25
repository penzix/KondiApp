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
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
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
import com.example.kondiapp.ExerciseRequest
import com.example.kondiapp.ExerciseViewModel
import com.example.kondiapp.LoginViewModel
import com.example.kondiapp.RetrofitClient
import com.example.kondiapp.SessionManager
import com.example.kondiapp.ui.theme.KondiAppTheme

class ShareExerciseActivity : ComponentActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val vm = ExerciseViewModel()
        val vm2 = CommentViewModel()
        super.onCreate(savedInstanceState)

        setContent {
            KondiAppTheme {
                val ctx = LocalContext.current

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                    ) {
                        shareExerciseHeader(ctx)
                        shareExerciseBody(vm,vm2,SessionManager.currentDay())


                    }
                }
            }
        }
    }

}

@Composable
fun shareExerciseHeader(ctx : Context){

    val activity = (LocalContext.current as? Activity)
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        IconButton(
            onClick = {
                val i = Intent(ctx, ExerciseActivity::class.java)
                ctx.startActivity(i)
                activity?.finish()
            }
        ) {
            Icon(
                Icons.Filled.Backspace,
                contentDescription = "Back To Exercise Activity",
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
fun shareExerciseBody(viewModel: ExerciseViewModel, commentViewModel: CommentViewModel, day: String) {

    val ctx = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    val token = SessionManager.getToken(ctx).toString()
    LaunchedEffect(Unit, block = {
        viewModel.getExercises(ctx,token,day)
        viewModel.getDayData(ctx,token)
    })


    var textInput by remember { mutableStateOf("") }
    var isValidText by remember { mutableStateOf(false) }
    var isExerciseChosen by remember { mutableStateOf(false) }
    var chosenExerciseId by remember { mutableStateOf(0) }
    var choseCounter by remember{
        mutableStateOf(0)
    }

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
    var currDay = SessionManager.currDay
    val pattern = remember{Regex("^\\d+\$")}
    Column(
        Modifier.background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {



        TextField(
            value = textInput,
            onValueChange = { input->
                textInput = input
                isValidText = input.isNotEmpty()
                },
            placeholder = { Text(text = "Enter your comment") },
            isError = !isValidText,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email")
            }
        )
        if(!isValidText){
            Text("Please enter your message", color = Color.Red)
        }


        Column {
            LazyColumn {
                items(viewModel.exerciseList) {
                        exercise ->
                    Row(modifier = Modifier.background(Color.Yellow)){
                        val toShow = buildAnnotatedString {
                                    append(exercise.Set.toString())
                                        append("x")
                                            append(exercise.Repetition.toString())
                                                append("x")
                                                    append(exercise.Weight.toString())
                                                        append(" kg")

                        }
                        Text(
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .weight(.25f) // 1/4
                                .padding(8.dp),
                            text = exercise.Exercise_type
                        )
                        Text(
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .weight(.25f) // 1/4
                                .padding(8.dp),
                            text = toShow
                        )
                        var buttonIcon by remember{
                            mutableStateOf(Icons.Filled.Close)
                        }
                        var buttonColor by remember{
                            mutableStateOf(Color.Red)
                        }

                        IconButton(
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .weight(.20f) // 1/5
                                .padding(8.dp),
                            onClick = {
                                isExerciseChosen = true
                                chosenExerciseId = exercise.id
                                SessionManager.exercise_type = exercise.Exercise_type
                                SessionManager.set=exercise.Set
                                SessionManager.repetition = exercise.Repetition
                                SessionManager.weight = exercise.Weight

                                if(buttonIcon == Icons.Filled.Close){
                                    buttonIcon = Icons.Filled.Done
                                    buttonColor = Color.Green
                                    choseCounter += 1
                                }
                                else{
                                    buttonIcon = Icons.Filled.Close
                                    buttonColor = Color.Red
                                    choseCounter -= 1
                                }
                            }
                        ) {
                            Icon(
                                imageVector = buttonIcon,
                                contentDescription = "Chosen to Share",
                                tint = buttonColor
                            )
                        }
                    }
                }
            }
            if(choseCounter!=1){
                Text("Please chose ONE exercise!", color = Color.Red)
            }


        }



    }

    Row {
        Button(
            onClick = {
                Log.wtf("Naaaaaaa",viewModel.exerciseList.toString())
                if(isValidText && isExerciseChosen && choseCounter ==1){
                    SessionManager.saveExerciseList(viewModel.exerciseList)
                    commentViewModel.sendExercisePlanAsComment(
                        comment = textInput,
                        Exercise_type = SessionManager.exercise_type,
                        Set = SessionManager.set,
                        Repetition = SessionManager.repetition,
                        Weight = SessionManager.weight,
                        token = "Bearer $token"
                    )

                    val i = Intent(ctx, UserActivity::class.java)
                    ctx.startActivity(i)
                    //activity?.finish()
                }
            }
        ) {
            Text("Share exercise")
        }


    }



}
