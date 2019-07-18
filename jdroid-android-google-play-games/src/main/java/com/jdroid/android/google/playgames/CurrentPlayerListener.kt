package com.jdroid.android.google.playgames

import com.google.android.gms.games.Player

interface CurrentPlayerListener {

    fun onCurrentPlayerLoaded(player: Player)
}
