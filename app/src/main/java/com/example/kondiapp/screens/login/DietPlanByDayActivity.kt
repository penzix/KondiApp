package com.example.kondiapp.screens.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.kondiapp.DietPlanViewModel
import com.example.kondiapp.ExerciseViewModel
import com.example.kondiapp.SessionManager
import com.example.kondiapp.ui.theme.KondiAppTheme

class DietPlanByDayActivity : ComponentActivity() {

    // on below line we are creating a variable for our shared preferences.
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val vm = DietPlanViewModel()
        super.onCreate(savedInstanceState)

        setContent {
            KondiAppTheme {
                val ctx = LocalContext.current
                // on below line we are specifying
                // background color for our application
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // on below line we are specifying theme as scaffold.
                    Column(
                    ) {
                        dietPlanByDayHeader(ctx)
                        dietPlanByDayBody(vm)
                    }
                }
            }
        }
    }

}

@Composable
fun dietPlanByDayHeader(ctx: Context){
    val activity = (LocalContext.current as? Activity)
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
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
fun dietPlanByDayBody(viewModel: DietPlanViewModel) {

    val ctx = LocalContext.current
    val token = SessionManager.getToken(ctx).toString()
    val currentUserId = SessionManager.currentUserId(ctx)


    LaunchedEffect(Unit, block = {
        viewModel.getDietPlan(currentUserId, SessionManager.getDietPlanId(),SessionManager.currentDay(),"Bearer $token")
        //viewModel.getDayData(ctx,token)
    })



    Column{
        LazyColumn{
            items(viewModel.dietPlanList){
                    diet->
                Row(Modifier.background(Color.Gray)){
                    TableCell(text = diet.diet_plan_id.toString())
                    TableCell(text = diet.day)
                    IconButton(
                        modifier = Modifier
                            .border(1.dp, Color.Black)
                            .weight(.14f) // 1/7
                            .padding(8.dp),
                        onClick = {
                            //val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            //editor.putString("email", "")
                            //editor.putString("pwd", "")
                            //editor.apply()
                            val day = diet.day
                            val dayId = diet.diet_plan_day_id
                            val diet_plan_id = diet.diet_plan_id
                            goToDietData(ctx,day,dayId,diet_plan_id)

                            val i = Intent(ctx, ChosenDietPlanActivity::class.java)
                            ctx.startActivity(i)
                        }
                    ) {
                        Icon(
                            Icons.Filled.DoubleArrow,
                            contentDescription = "Go To Diet Plan",
                            tint = Color.Blue
                        )
                    }
                }
            }
        }

    }

}
fun goToDietData(ctx: Context, day: String, dayId : Int,diet_plan_id: Int){
    SessionManager.saveCurrentDay(ctx,day)
    SessionManager.saveCurrentDayId(ctx,dayId)
    SessionManager.saveDietPlanId(diet_plan_id)
    Log.wtf("ANap:",day)
    Log.wtf("ANapId:",dayId.toString())
    Log.wtf("ADietPlanId:",diet_plan_id.toString())
}


