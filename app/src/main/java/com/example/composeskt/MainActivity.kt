package com.example.composeskt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Preview
@Composable
fun MainScreen(){
    val words = arrayOf("JAVA","PYTHON","KOTLIN","PHP","C++","C","NEXT.JS","REACT.JS","LUA","JIRA","MONGO.DB")
    val color = arrayOf(Color.Gray,Color.Cyan,Color.Green,Color.Red,Color.Gray,Color.Magenta,Color.Yellow,Color.LightGray,Color.Blue,Color.White,Color.Transparent)
    LazyColumn(Modifier.background(color = Color.Black), horizontalAlignment = Alignment.CenterHorizontally) {
        items(words.size){ index ->
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(color = color[index]),  // Card takes up the full width
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Text(
                    text = words[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



