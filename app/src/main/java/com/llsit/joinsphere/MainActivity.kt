package com.llsit.joinsphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.llsit.joinsphere.navigation.RootNavDisplay
import com.llsit.joinsphere.ui.theme.JoinSphereTheme
import com.llsit.navigation.LocalNavigator
import com.llsit.navigation.rememberAppNavigator

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoinSphereTheme {
                val navigator = rememberAppNavigator()
                BackHandler(enabled = navigator.activeTabBackStack.size > 1) {
                    navigator.goBack()
                }
                CompositionLocalProvider(LocalNavigator provides navigator) {
                    RootNavDisplay(navigator = navigator)
                }
            }
        }

//        val crashButton = Button(this)
//        crashButton.text = "Test Crash"
//        crashButton.setOnClickListener {
//            throw RuntimeException("Test Crash") // Force a crash
//        }

//        addContentView(
//            crashButton, ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//        )
    }
}

