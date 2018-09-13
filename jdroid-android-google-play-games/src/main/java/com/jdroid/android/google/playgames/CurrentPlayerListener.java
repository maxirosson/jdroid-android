package com.jdroid.android.google.playgames;

import com.google.android.gms.games.Player;

public interface CurrentPlayerListener {

	void onCurrentPlayerLoaded(Player player);
}
