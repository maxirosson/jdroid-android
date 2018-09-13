package com.jdroid.android.google.playgames;

import com.google.android.gms.games.stats.PlayerStats;

public interface PlayerStatsListener {

	void onPlayerStatsLoaded(PlayerStats playerStats);
}
