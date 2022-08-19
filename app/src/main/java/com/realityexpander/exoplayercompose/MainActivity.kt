package com.realityexpander.exoplayercompose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.LivePlaybackSpeedControl
import com.google.android.exoplayer2.MediaItem
import com.realityexpander.exoplayercompose.screens.MainScreen
import com.realityexpander.exoplayercompose.screens.RadioScreen
import com.realityexpander.exoplayercompose.screens.VideoScreen
import com.realityexpander.exoplayercompose.ui.theme.ExoPlayerComposeTheme

val livePlaybackSpeedControl = object: LivePlaybackSpeedControl {
    override fun setLiveConfiguration(liveConfiguration: MediaItem.LiveConfiguration) {
        println("setLiveConfiguration: $liveConfiguration")
    }

    override fun setTargetLiveOffsetOverrideUs(liveOffsetUs: Long) {
        println("setTargetLiveOffsetOverrideUs $liveOffsetUs")
    }

    override fun notifyRebuffer() {
        println("notifyRebuffer")
    }

    override fun getAdjustedPlaybackSpeed(liveOffsetUs: Long, bufferedDurationUs: Long): Float {
        println("getAdjustedPlaybackSpeed $liveOffsetUs $bufferedDurationUs")

        return 2.0f
    }

    override fun getTargetLiveOffsetUs(): Long {
        println("getTargetLiveOffsetUs")

        return 0L
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ExoPlayerComposeTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Navigation(navController = navController)
                }
            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(navController)
        }
        composable("video") {
           VideoScreen()
        }
        composable("radio") {
            RadioScreen()
        }
    }
}

fun provideExoPlayer(context : Context, mediaItem: MediaItem): ExoPlayer {
    val player = ExoPlayer.Builder(context)
        .setSeekForwardIncrementMs(5000)
        .setSeekBackIncrementMs(5000)
        .setLivePlaybackSpeedControl(livePlaybackSpeedControl)
        .build()

    player.setMediaItem(mediaItem)
    return player
}