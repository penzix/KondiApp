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
import androidx.compose.material.icons.filled.DirectionsRun
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

class EditCommentActivity : ComponentActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val vm = CommentViewModel()
        super.onCreate(savedInstanceState)

        setContent {
            KondiAppTheme {
                val ctx = LocalContext.current
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                    ) {
                        editCommentHeader(ctx)
                        getCommentToEdit(vm)

                    }
                }
            }
        }
    }

}

@Composable
fun editCommentHeader(ctx : Context){
    val activity = (LocalContext.current as? Activity)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        IconButton(
            onClick = {
                val i = Intent(ctx, UserActivity::class.java)
                ctx.startActivity(i)
                activity?.finish()
            }
        ) {
            Icon(
                Icons.Filled.Backspace,
                contentDescription = "Back To User Activity",
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
fun getCommentToEdit(viewModel: CommentViewModel) {

    val ctx = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    val token = SessionManager.getToken(ctx).toString()


    LaunchedEffect(Unit, block = {
        viewModel.getComments(
            ctx,
            SessionManager.currentUserId(ctx),
            SessionManager.getCommentId(),
            "Bearer $token")
    })

    Log.wtf("TheOneExercise:",viewModel.myComment.toString())

    val comment = remember {
        mutableStateOf(viewModel.myComment.value?.text.toString())
    }

    val exercise_type = remember {
        mutableStateOf(viewModel.myComment.value?.Exercise_type.toString())
    }
    val set = remember {
        mutableStateOf(viewModel.myComment.value?.Set.toString())
    }
    val repetition = remember {
        mutableStateOf(viewModel.myComment.value?.Repetition.toString())
    }
    val weight = remember {
        mutableStateOf(viewModel.myComment.value?.Weight.toString())
    }
    var isValidComment by remember { mutableStateOf(true) }
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
        LazyColumn(){

            items(viewModel.commentEditList){
                    edit->
                val message = remember {
                    mutableStateOf(edit.text)
                }
                val exercise_type = remember {
                    mutableStateOf(edit.Exercise_type)
                }
                val set = remember {
                    mutableStateOf(edit.Set.toString())
                }
                val repetition = remember {
                    mutableStateOf(edit.Repetition.toString())
                }
                val weight = remember {
                    mutableStateOf(edit.Weight.toString())
                }

                TextField(
                    value = message.value,
                    onValueChange = { input->
                        message.value = input
                        isValidComment = message.value.isNotEmpty()
                    },
                    placeholder = { Text(text = "Enter your comment!") },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Comment, contentDescription = "Comment")
                    }
                )
                if(!isValidComment){
                    Text("Please enter your comment", color = Color.Red)
                }

                if(edit.Exercise_type!=null){

                    TextField(
                        value = exercise_type.value,
                        onValueChange = { input->
                            exercise_type.value = input
                            isValidText = exercise_type.value.isNotEmpty()
                        },
                        placeholder = { Text(text = "Enter your exercise type!") },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                        isError = !isValidText,
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.DirectionsRun, contentDescription = "Exercise name")
                        }
                    )
                    if(!isValidText){
                        Text("Please enter your exercise's name", color = Color.Red)
                    }

                    TextField(
                        value = set.value,
                        onValueChange = { input ->
                            if (input.isDigitsOnly()) {
                                set.value = input
                                isValidSet = set.value.isDigitsOnly() && set.value.isNotEmpty()
                            }
                        },
                        placeholder = { Text(text = "Enter your set's value!") },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                        isError = !isValidSet,
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
                        onValueChange = { input ->
                            if (input.isDigitsOnly()) {
                                repetition.value = input
                                isValidRepetition =
                                    repetition.value.isDigitsOnly() && repetition.value.isNotEmpty()
                            }
                        },
                        placeholder = { Text(text = "Enter your repetition's value!") },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                        isError = !isValidRepetition,
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
                        onValueChange = { input ->
                            if (input.isDigitsOnly()) {
                                weight.value = input
                                isValidWeight = weight.value.isDigitsOnly() && weight.value.isNotEmpty()
                            }

                        },
                        placeholder = { Text(text = "Enter your weight's value!") },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                        isError = !isValidWeight,
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Numbers, contentDescription = "Weight number")
                        }
                    )
                    if(!isValidWeight){
                        Text("Please enter your weight's valid number", color = Color.Red)
                    }
                }

                SessionManager.editedComment = message.value

                if(edit.Exercise_type!=null){
                    /*
                    if(set.value == ""){
                        set.value = "0"
                    }
                    if(repetition.value == ""){
                        repetition.value = "0"
                    }
                    if(weight.value == ""){
                        weight.value = "0"
                    }
                    */
                    if(isValidText)
                        SessionManager.exercise_type= exercise_type.value
                    if(isValidSet)
                        SessionManager.set = set.value.toInt()
                    if(isValidRepetition)
                        SessionManager.repetition = repetition.value.toInt()
                    if(isValidWeight)
                        SessionManager.weight = weight.value.toInt()
                }
            }
        }
    }

    Row {
        Button(
            onClick = {
                if(isValidComment && isValidText && isValidSet && isValidRepetition && isValidWeight) {
                    viewModel.editComment(
                        commentId = SessionManager.getCommentId(),
                        comment = SessionManager.editedComment,
                        exercise_type = SessionManager.exercise_type,
                        set = SessionManager.set,
                        repetition = SessionManager.repetition,
                        weight = SessionManager.weight,
                        token = "Bearer $token"
                    )
                    val i = Intent(ctx, UserActivity::class.java)
                    ctx.startActivity(i)
                }

                //activity?.finish()

            }
        ) {
            Text("Save Comment")
        }


    }



}
