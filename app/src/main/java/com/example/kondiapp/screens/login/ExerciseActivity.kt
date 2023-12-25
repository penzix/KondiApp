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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.kondiapp.ExerciseData
import com.example.kondiapp.ExerciseViewModel
import com.example.kondiapp.LoginViewModel
import com.example.kondiapp.RetrofitClient
import com.example.kondiapp.SessionManager
import com.example.kondiapp.ui.theme.KondiAppTheme

class ExerciseActivity : ComponentActivity() {

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
                        exerciseHeader(ctx)
                        exerciseBody(vm,SessionManager.currentDay())
                    }
                }
            }
        }
    }
}

@Composable
fun exerciseHeader(ctx:Context){
    val ctx = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        IconButton(
            onClick = {
                val i = Intent(ctx, ExercisePlanActivity::class.java)
                ctx.startActivity(i)
                activity?.finish()
            }
        ) {
            Icon(
                Icons.Filled.Backspace,
                contentDescription = "Back To Exercise Plan",
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
fun exerciseBody(viewModel: ExerciseViewModel, day: String) {

    val ctx = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    val token = SessionManager.getToken(ctx).toString()
    LaunchedEffect(Unit, block = {
        viewModel.getExercises(ctx,token,day)
        viewModel.getDayData(ctx,token)
    })

    val exerciseNames : MutableList<String> = mutableListOf<String>()
    val exerciseSets : MutableList<Int> = mutableListOf<Int>()
    val exerciseReps : MutableList<Int> = mutableListOf<Int>()
    val exerciseWeights : MutableList<Int> = mutableListOf<Int>()

    for(i in viewModel.exerciseList!!.indices){
        exerciseNames.add(viewModel.exerciseList[i].Exercise_type)
        exerciseSets.add(viewModel.exerciseList[i].Set)
        exerciseReps.add(viewModel.exerciseList[i].Repetition)
        exerciseWeights.add(viewModel.exerciseList[i].Weight)
    }
    Log.wtf("edzesnevek:",exerciseNames.toString())
    Log.wtf("edzesnevek:",exerciseSets.toString())
    Log.wtf("edzesnevek:",exerciseReps.toString())
    Log.wtf("edzesnevek:",exerciseWeights.toString())

    Column {
        Row(Modifier.background(Color.Yellow)){
            Text(
                modifier = Modifier
                    .border(1.dp, Color.Black)
                    .weight(.25f) // 1/4
                    .padding(8.dp),
                text  = "Name")
            Text(
                modifier = Modifier
                    .border(1.dp, Color.Black)
                    .weight(.25f) // 1/4
                    .padding(8.dp),
                text = "sets")
            Text(
                modifier = Modifier
                    .border(1.dp, Color.Black)
                    .weight(.25f) // 1/4
                    .padding(8.dp),
                text = "reps")
            Text(
                modifier = Modifier
                    .border(1.dp, Color.Black)
                    .weight(.25f) // 1/4
                    .padding(8.dp),
                text = "weight(kg)")
        }
        LazyColumn {
            items(viewModel.exerciseList) {
                    exercise ->
                Row(Modifier.background(Color.Yellow)) {
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
                            .weight(.20f) // 1/5
                            .padding(8.dp),
                        text = exercise.Set.toString()
                    )

                    Text(
                        modifier = Modifier
                            .border(1.dp, Color.Black)
                            .weight(.20f) // 1/5
                            .padding(8.dp),
                        text = exercise.Repetition.toString()
                    )
                    Text(
                        modifier = Modifier
                            .border(1.dp, Color.Black)
                            .weight(.20f) // 1/5
                            .padding(8.dp),
                        text = exercise.Weight.toString()
                    )

                    var isPressed by remember{
                        mutableStateOf(false)
                    }
                    var buttonColor by remember{
                        mutableStateOf(Color.Red)
                    }
                    var buttonIcon by remember{
                        mutableStateOf(Icons.Filled.Close)
                    }
                    IconButton(
                        modifier = Modifier
                            .border(1.dp, Color.Black)
                            .weight(.20f) // 1/5
                            .padding(8.dp),
                        onClick = {
                            isPressed = !isPressed
                            if(isPressed) {
                                buttonColor = Color.Green
                                buttonIcon = Icons.Filled.Done
                            }
                            else{
                                buttonColor=Color.Red
                                buttonIcon = Icons.Filled.Close
                            }

                        }
                    ) {
                        Icon(
                            buttonIcon,
                            contentDescription = "Edit",
                            tint = buttonColor
                        )
                    }

                }
            }

        }

    }


    Column(){
        Row(){
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {

                    val i = Intent(ctx, EditExerciseActivity::class.java)
                    ctx.startActivity(i)
                }
            ){
                Text("Edit Exercise Plan")
            }
        }
        Row(){
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    val i = Intent(ctx, ShareExerciseActivity::class.java)
                    ctx.startActivity(i)
                    activity?.finish()
                }
            ) {
                Text("Share exercise")
            }
        }
    }

    Column(){
        Text("Data of the current exercise plan")
        Row{
            Text("Number of exercises: ${exerciseNames.size}")
        }
        Row{
            Text("Total sets: ${exerciseSets.sum()}")
        }
        Row{
            Text("Total repetitions: ${exerciseReps.sum()}")
        }
        Row{
            Text("Total weight: ${exerciseWeights.sum()} kg")
        }

    }
}
