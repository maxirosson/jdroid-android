package com.jdroid.android.google.playgames

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.games.AchievementsClient
import com.google.android.gms.games.EventsClient
import com.google.android.gms.games.Games
import com.google.android.gms.games.LeaderboardsClient
import com.google.android.gms.games.PlayerStatsClient
import com.google.android.gms.games.PlayersClient
import com.google.android.gms.games.event.Event
import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.google.signin.GoogleSignInHelper
import com.jdroid.android.google.signin.GoogleSignInListener
import com.jdroid.java.collections.Lists
import com.jdroid.java.utils.LoggerUtils
import com.jdroid.java.utils.RandomUtils

class GooglePlayGamesHelper(abstractFragment: AbstractFragment, googleSignInListener: GoogleSignInListener) : GoogleSignInHelper(abstractFragment, googleSignInListener) {

    companion object {
        private val LOGGER = LoggerUtils.getLogger(GooglePlayGamesHelper::class.java)
        private val UNUSED_REQUEST_CODE = RandomUtils.get16BitsInt()
    }

    private var leaderboardsClient: LeaderboardsClient? = null
    private var achievementsClient: AchievementsClient? = null
    private var eventsClient: EventsClient? = null
    private var playersClient: PlayersClient? = null
    private var playerStatsClient: PlayerStatsClient? = null

    override fun getGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN
    }

    override fun onGoogleSignIn(account: GoogleSignInAccount) {
        super.onGoogleSignIn(account)

        leaderboardsClient = Games.getLeaderboardsClient(abstractFragment.requireContext(), account)
        achievementsClient = Games.getAchievementsClient(abstractFragment.requireContext(), account)
        eventsClient = Games.getEventsClient(abstractFragment.requireContext(), account)
        playersClient = Games.getPlayersClient(abstractFragment.requireContext(), account)
        playerStatsClient = Games.getPlayerStatsClient(abstractFragment.requireContext(), account)
    }

    override fun onGoogleSignOut() {
        super.onGoogleSignOut()

        achievementsClient = null
        leaderboardsClient = null
        eventsClient = null
        playersClient = null
        playerStatsClient = null
    }

    override fun onGoogleAccessRevoked() {
        super.onGoogleAccessRevoked()

        achievementsClient = null
        leaderboardsClient = null
        eventsClient = null
        playersClient = null
        playerStatsClient = null
    }

    fun submitScore(leaderboardId: String, score: Long) {
        if (leaderboardsClient != null) {
            leaderboardsClient!!.submitScore(leaderboardId, score)
        }
    }

    fun showLeaderboard(leaderboardId: String) {
        if (leaderboardsClient != null) {
            leaderboardsClient!!.getLeaderboardIntent(leaderboardId).addOnSuccessListener { intent ->
                ActivityLauncher.startActivityForResult(abstractFragment.activity, intent, UNUSED_REQUEST_CODE)
            }.addOnFailureListener { e ->
                AbstractApplication.get().exceptionHandler.logHandledException(e)
            }
        }
    }

    fun showAllLeaderboards() {
        if (leaderboardsClient != null) {
            leaderboardsClient!!.allLeaderboardsIntent.addOnSuccessListener { intent ->
                ActivityLauncher.startActivityForResult(abstractFragment.activity, intent, UNUSED_REQUEST_CODE)
            }.addOnFailureListener { e ->
                AbstractApplication.get().exceptionHandler.logHandledException(e)
            }
        }
    }

    fun unlockAchievement(achievementId: String) {
        if (achievementsClient != null) {
            achievementsClient!!.unlock(achievementId)
        }
    }

    fun incrementAchievement(achievementId: String, numSteps: Int) {
        if (achievementsClient != null) {
            achievementsClient!!.increment(achievementId, numSteps)
        }
    }

    fun showAchievements() {
        if (achievementsClient != null) {
            achievementsClient!!.achievementsIntent.addOnSuccessListener { intent ->
                ActivityLauncher.startActivityForResult(abstractFragment.activity, intent, UNUSED_REQUEST_CODE)
            }.addOnFailureListener { e ->
                AbstractApplication.get().exceptionHandler.logHandledException(e)
            }
        }
    }

    fun incrementEvent(eventId: String, incrementAmount: Int) {
        eventsClient?.increment(eventId, incrementAmount)
    }

    fun loadEvents(eventsListener: EventsListener, forceReload: Boolean = false) {
        if (eventsClient != null) {
            eventsClient!!.load(forceReload).addOnSuccessListener { eventBufferAnnotatedData ->
                val eventBuffer = eventBufferAnnotatedData.get()

                var count = 0
                if (eventBuffer != null) {
                    count = eventBuffer.count
                }
                LOGGER.info("Events loaded: $count")

                val events = Lists.newArrayList<Event>()
                for (i in 0 until count) {
                    val event = eventBuffer!!.get(i)
                    LOGGER.info("Event: " + event.name + " -> " + event.value)
                    events.add(event)
                }
                eventsListener.onEventsLoaded(events)
            }.addOnFailureListener { e ->
                AbstractApplication.get().exceptionHandler.logHandledException(e)
            }
        }
    }

    fun loadCurrentPlayer(currentPlayerListener: CurrentPlayerListener) {
        playersClient!!.currentPlayer.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                currentPlayerListener.onCurrentPlayerLoaded(task.result!!)
            } else {
                val e = task.exception
                AbstractApplication.get().exceptionHandler.logHandledException(e)
            }
        }
    }

    fun loadPlayerStats(playerStatsListener: PlayerStatsListener, forceReload: Boolean = false) {
        playerStatsClient!!.loadPlayerStats(forceReload).addOnSuccessListener { playerStatsAnnotatedData ->
            val playerStats = playerStatsAnnotatedData.get()
            playerStatsListener.onPlayerStatsLoaded(playerStats!!)
        }.addOnFailureListener { e ->
            AbstractApplication.get().exceptionHandler.logHandledException(e)
        }
    }
}
