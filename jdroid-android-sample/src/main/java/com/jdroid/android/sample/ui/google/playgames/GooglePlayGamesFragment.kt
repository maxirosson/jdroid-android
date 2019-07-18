package com.jdroid.android.sample.ui.google.playgames

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.games.Player
import com.google.android.gms.games.event.Event
import com.google.android.gms.games.stats.PlayerStats
import com.google.android.material.snackbar.Snackbar
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.google.playgames.CurrentPlayerListener
import com.jdroid.android.google.playgames.EventsListener
import com.jdroid.android.google.playgames.GooglePlayGamesHelper
import com.jdroid.android.google.playgames.PlayerStatsListener
import com.jdroid.android.google.signin.GoogleSignInListener
import com.jdroid.android.loading.FragmentLoading
import com.jdroid.android.loading.NonBlockingLoading
import com.jdroid.android.sample.R
import com.jdroid.java.utils.TypeUtils

class GooglePlayGamesFragment : AbstractFragment(), GoogleSignInListener {

    private var googlePlayGamesHelper: GooglePlayGamesHelper? = null

    private var signInButton: SignInButton? = null
    private var signOutButton: View? = null
    private var revokeButton: View? = null

    private var gameContainer: View? = null
    private var leaderboardId: EditText? = null
    private var showLeaderboardButton: View? = null
    private var showAllLeaderboardsButton: View? = null
    private var score: EditText? = null
    private var submitScore: View? = null

    private var achievementId: EditText? = null
    private var unlockAchievement: View? = null
    private var incrementAchievement: View? = null
    private var showAchievements: View? = null

    private var eventId: EditText? = null
    private var incrementEvent: View? = null
    private var loadEvents: View? = null
    private var loadCurrentPlayer: View? = null
    private var loadPlayerStats: View? = null
    private var status: TextView? = null

    override fun getContentFragmentLayout(): Int? {
        return R.layout.google_play_games_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        status = findView(R.id.status)

        signInButton = findView(R.id.signIn)
        signInButton!!.setSize(SignInButton.SIZE_WIDE)
        signInButton!!.setOnClickListener { googlePlayGamesHelper!!.signIn() }

        signOutButton = findView(R.id.signOut)
        signOutButton!!.setOnClickListener { googlePlayGamesHelper!!.signOut() }

        revokeButton = findView(R.id.revoke)
        revokeButton!!.setOnClickListener { googlePlayGamesHelper!!.revokeAccess() }

        gameContainer = findView(R.id.gameContainer)

        showAllLeaderboardsButton = findView(R.id.showAllLeaderboards)
        showAllLeaderboardsButton!!.setOnClickListener { googlePlayGamesHelper!!.showAllLeaderboards() }

        leaderboardId = findView(R.id.leaderboardId)
        score = findView(R.id.score)

        showLeaderboardButton = findView(R.id.showLeaderboard)
        showLeaderboardButton!!.setOnClickListener { googlePlayGamesHelper!!.showLeaderboard(leaderboardId!!.text.toString()) }

        submitScore = findView(R.id.submitScore)
        submitScore!!.setOnClickListener {
            googlePlayGamesHelper!!.submitScore(
                leaderboardId!!.text.toString(),
                TypeUtils.getInteger(score!!.text.toString())!!.toLong()
            )
        }

        achievementId = findView(R.id.achievementId)
        unlockAchievement = findView(R.id.unlockAchievement)
        unlockAchievement!!.setOnClickListener { googlePlayGamesHelper!!.unlockAchievement(achievementId!!.text.toString()) }

        incrementAchievement = findView(R.id.incrementAchievement)
        incrementAchievement!!.setOnClickListener {
            googlePlayGamesHelper!!.incrementAchievement(
                achievementId!!.text.toString(),
                1
            )
        }

        showAchievements = findView(R.id.showAchievements)
        showAchievements!!.setOnClickListener { googlePlayGamesHelper!!.showAchievements() }

        eventId = findView(R.id.eventId)
        incrementEvent = findView(R.id.incrementEvent)
        incrementEvent!!.setOnClickListener { googlePlayGamesHelper!!.incrementEvent(eventId!!.text.toString(), 1) }

        loadEvents = findView(R.id.loadEvents)
        loadEvents!!.setOnClickListener {
            googlePlayGamesHelper!!.loadEvents(object : EventsListener {

                override fun onEventsLoaded(events: List<Event>) {
                    val builder = StringBuilder()
                    for (event in events) {
                        builder.append("Event name: ")
                        builder.append(event.name)
                        builder.append("\nEvent description: ")
                        builder.append(event.description)
                        builder.append("\nEvent value: ")
                        builder.append(event.formattedValue)
                        builder.append("\n")
                        builder.append("\n")
                    }
                    status!!.text = builder.toString()
                }
            })
        }

        loadCurrentPlayer = findView(R.id.loadEvents)
        loadCurrentPlayer!!.setOnClickListener {
            googlePlayGamesHelper!!.loadCurrentPlayer(object : CurrentPlayerListener {
                override fun onCurrentPlayerLoaded(player: Player) {
                    val builder = StringBuilder()
                    builder.append("Display Name: ")
                    builder.append(player.displayName)
                    builder.append("\nName: ")
                    builder.append(player.name)
                    builder.append("\nTitle: ")
                    builder.append(player.title)
                    builder.append("\nCurrent Level: ")
                    builder.append(player.levelInfo!!.currentLevel)
                    builder.append("\nCurrent XP: ")
                    builder.append(player.levelInfo!!.currentXpTotal)
                    status!!.text = builder.toString()
                }
            })
        }

        loadPlayerStats = findView(R.id.loadPlayerStats)
        loadPlayerStats!!.setOnClickListener {
            googlePlayGamesHelper!!.loadPlayerStats(object : PlayerStatsListener {
                override fun onPlayerStatsLoaded(playerStats: PlayerStats) {
                    val builder = StringBuilder()
                    builder.append("DaysSinceLastPlayed: ")
                    builder.append(playerStats.daysSinceLastPlayed)
                    status!!.text = builder.toString()
                }
            })
        }

        googlePlayGamesHelper = GooglePlayGamesHelper(this, this)
    }

    override fun onStart() {
        super.onStart()

        googlePlayGamesHelper!!.silentSignIn()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googlePlayGamesHelper!!.onActivityResult(requestCode, resultCode, data!!)
    }

    override fun onGoogleSignIn(googleSignInAccount: GoogleSignInAccount) {
        val builder = StringBuilder()
        builder.append("Display Name: ")
        builder.append(googleSignInAccount.displayName)
        builder.append("\nEmail: ")
        builder.append(googleSignInAccount.email)
        builder.append("\nGranted Scopes: ")
        builder.append(googleSignInAccount.grantedScopes)
        builder.append("\nId Token: ")
        builder.append(if (googleSignInAccount.idToken != null) googleSignInAccount.idToken!!.substring(0, 50) else null)
        status!!.text = builder.toString()

        signInButton!!.visibility = View.GONE
        signOutButton!!.visibility = View.VISIBLE
        gameContainer!!.visibility = View.VISIBLE

        Snackbar.make(findView(R.id.container), "onGoogleSignIn", Snackbar.LENGTH_SHORT).show()
    }

    override fun onGoogleSignOut() {
        status!!.setText(R.string.notLogged)

        signInButton!!.visibility = View.VISIBLE
        signOutButton!!.visibility = View.GONE
        gameContainer!!.visibility = View.GONE

        Snackbar.make(findView(R.id.container), "onGoogleSignOut", Snackbar.LENGTH_SHORT).show()
    }

    override fun onGoogleAccessRevoked() {
        status!!.setText(R.string.notLogged)

        signInButton!!.visibility = View.VISIBLE
        signOutButton!!.visibility = View.GONE
        gameContainer!!.visibility = View.GONE

        Snackbar.make(findView(R.id.container), "onGoogleAccessRevoked", Snackbar.LENGTH_SHORT).show()
    }

    override fun getDefaultLoading(): FragmentLoading? {
        return NonBlockingLoading()
    }
}
