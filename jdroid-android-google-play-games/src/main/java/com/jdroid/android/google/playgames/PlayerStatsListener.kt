package com.jdroid.android.google.playgames

import com.google.android.gms.games.stats.PlayerStats

interface PlayerStatsListener {

    fun onPlayerStatsLoaded(playerStats: PlayerStats)
}
