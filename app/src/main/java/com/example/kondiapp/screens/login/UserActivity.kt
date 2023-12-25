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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.kondiapp.ApiInterface
import com.example.kondiapp.BaseResponse
import com.example.kondiapp.CommentList
import com.example.kondiapp.CommentResponse
import com.example.kondiapp.CommentViewModel
import com.example.kondiapp.LoginResponse
import com.example.kondiapp.LoginViewModel
import com.example.kondiapp.RetrofitClient
import com.example.kondiapp.SessionManager
import com.example.kondiapp.UserList
import com.example.kondiapp.ui.theme.KondiAppTheme

class UserActivity : ComponentActivity() {

    // on below line we are creating a variable for our shared preferences.
    lateinit var sharedPreferences: SharedPreferences

    // on below line we are creating a variable
    // for prefs key and email key and pwd key.
    var PREFS_KEY = "prefs"
    var EMAIL_KEY = "email"

    // on below line we are creating a variable for email
    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        val owner = this
        val vm = CommentViewModel()
        val vm2 = LoginViewModel()
        val userData by viewModels<LoginViewModel>()
        super.onCreate(savedInstanceState)

        // on below line we are initializing our shared preferences.
        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

        // on below line we are initializing our email from shared preferences.
        email = sharedPreferences.getString(EMAIL_KEY, "").toString()


        setContent {
            KondiAppTheme {
                val ctx = LocalContext.current
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    Column(
                    ) {
                        val ctx = LocalContext.current
                        val activity = (LocalContext.current as? Activity)

                        header(ctx)
                        displayFeed(vm.kommentList, vm = vm, vm2 = vm2)

                    }
                }
            }
        }
    }

}

@Composable
fun header(ctx: Context){
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
fun displayFeed(messages: List<CommentList>, vm : CommentViewModel, vm2 : LoginViewModel){

    val ctx = LocalContext.current
    LaunchedEffect(Unit, block = {
        //vm.commentRepo.fetchComments()
        vm.getComments()
        vm2.getUsers()
        Log.wtf("UserTOKEN",SessionManager.getToken(ctx))
        vm.getUserComments(ctx, SessionManager.getToken(ctx).toString())
    })

    var mutableMessages = remember {mutableStateListOf<CommentList>()}
    mutableMessages.addAll(messages)
    //mutableMessages.add(CommentList(100,"yes","",0,0,0,SessionManager.currentUserId(ctx)))

    val commentValue = remember {
        mutableStateOf(TextFieldValue())
    }

    Column (
        modifier = Modifier.padding(16.dp)
    ){

        var newComment = remember{ mutableStateOf(CommentList(0,"","",0,0,0,SessionManager.currentUserId(ctx))) }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = {
                val access_token = SessionManager.getToken(ctx);
                val i = Intent(ctx, AddCommentActivity::class.java)
                ctx.startActivity(i)
            }
        ) {
            Text(text = "Send message")
        }


    val deletedItem = remember{ mutableStateListOf<CommentList>() }
    LazyColumn {
        itemsIndexed(
            items = mutableMessages.reversed(),
            itemContent = { _, message ->
                AnimatedVisibility(
                    visible = !deletedItem.contains(message),
                    enter = expandVertically(),
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 1000))
                )
                {
                    Row(modifier = Modifier.padding(all = 8.dp)) {
                        /*
                Image(
                    painter = painterResource(R.drawable.Profile_pic_demo),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.5.dp,MaterialTheme.colorScheme.primary, CircleShape)
                )
                 */

                        Spacer(modifier = Modifier.width(8.dp))
                        var isExpanded by remember { mutableStateOf(false) }
                        val ctx = LocalContext.current
                        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
                            for (user in vm2.userList) {
                                if (user.id!!.toInt() == message.user_id) {
                                    Text(
                                        text = user.name.toString(),
                                        color = MaterialTheme.colorScheme.secondary,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            var checkedMessage = message.text
                            if (message.Exercise_type != null) {
                                checkedMessage =
                                    checkedMessage + "\n" + message.Exercise_type + ": " + message.Set.toString() + "x" + message.Repetition.toString() + "x" + message.Weight + " kg"
                            }

                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                shadowElevation = 1.dp,
                                color = if (message.user_id == SessionManager.currentUserId(ctx)) Color.Cyan else Color.Magenta,
                                modifier = Modifier
                                    .animateContentSize()
                                    .padding(1.dp)
                            ) {
                                Text(
                                    text = checkedMessage,
                                    modifier = Modifier.padding(all = 4.dp),
                                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            if (message.user_id == SessionManager.currentUserId(ctx)) {
                                Row {
                                    IconButton(
                                        onClick = {
                                            //TODO
                                            //EditCommentActivity
                                            Log.wtf("Saved Comment id:",message.id.toString())

                                            SessionManager.saveCommentId(message.id)
                                            val i = Intent(ctx, EditCommentActivity::class.java)
                                            ctx.startActivity(i)
                                            //activity?.finish()
                                        }
                                    ) {
                                        Icon(
                                            Icons.Filled.Edit,
                                            contentDescription = "Edit Comment",
                                            tint = Color.Red
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            //TODO
                                            val access_token = SessionManager.getToken(ctx)
                                            deletedItem.add(message)
                                            vm.deleteComment(message.id, "Bearer $access_token")
                                        }
                                    ) {
                                        Icon(
                                            Icons.Filled.Delete,
                                            contentDescription = "Delete Comment",
                                            tint = Color.Red
                                        )
                                    }
                                }

                            }
                        }
                    }


                }
            })
    }
    }
}

