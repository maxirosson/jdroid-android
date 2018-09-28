package com.jdroid.android.sample.ui.google.playgames;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.event.Event;
import com.google.android.gms.games.stats.PlayerStats;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.playgames.CurrentPlayerListener;
import com.jdroid.android.google.playgames.EventsListener;
import com.jdroid.android.google.playgames.GooglePlayGamesHelper;
import com.jdroid.android.google.playgames.PlayerStatsListener;
import com.jdroid.android.google.signin.GoogleSignInListener;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.NonBlockingLoading;
import com.jdroid.android.sample.R;
import com.jdroid.java.utils.TypeUtils;

import java.util.List;

public class GooglePlayGamesFragment extends AbstractFragment implements GoogleSignInListener {

	private GooglePlayGamesHelper googlePlayGamesHelper;

	private SignInButton signInButton;
	private View signOutButton;
	private View revokeButton;

	private View gameContainer;
	private EditText leaderboardId;
	private View showLeaderboardButton;
	private View showAllLeaderboardsButton;
	private EditText score;
	private View submitScore;

	private EditText achievementId;
	private View unlockAchievement;
	private View incrementAchievement;
	private View showAchievements;

	private EditText eventId;
	private View incrementEvent;
	private View loadEvents;
	private View loadCurrentPlayer;
	private View loadPlayerStats;
	private TextView status;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.google_play_games_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		status = findView(R.id.status);

		signInButton = findView(R.id.signIn);
		signInButton.setSize(SignInButton.SIZE_WIDE);
		signInButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googlePlayGamesHelper.signIn();
			}
		});

		signOutButton = findView(R.id.signOut);
		signOutButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googlePlayGamesHelper.signOut();
			}
		});

		revokeButton = findView(R.id.revoke);
		revokeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googlePlayGamesHelper.revokeAccess();
			}
		});

		gameContainer = findView(R.id.gameContainer);

		showAllLeaderboardsButton = findView(R.id.showAllLeaderboards);
		showAllLeaderboardsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googlePlayGamesHelper.showAllLeaderboards();
			}
		});

		leaderboardId = findView(R.id.leaderboardId);
		score = findView(R.id.score);

		showLeaderboardButton = findView(R.id.showLeaderboard);
		showLeaderboardButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googlePlayGamesHelper.showLeaderboard(leaderboardId.getText().toString());
			}
		});

		submitScore = findView(R.id.submitScore);
		submitScore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googlePlayGamesHelper.submitScore(leaderboardId.getText().toString(), TypeUtils.getInteger(score.getText().toString()));
			}
		});

		achievementId = findView(R.id.achievementId);
		unlockAchievement = findView(R.id.unlockAchievement);
		unlockAchievement.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googlePlayGamesHelper.unlockAchievement(achievementId.getText().toString());
			}
		});

		incrementAchievement = findView(R.id.incrementAchievement);
		incrementAchievement.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googlePlayGamesHelper.incrementAchievement(achievementId.getText().toString(), 1);
			}
		});

		showAchievements = findView(R.id.showAchievements);
		showAchievements.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googlePlayGamesHelper.showAchievements();
			}
		});

		eventId = findView(R.id.eventId);
		incrementEvent = findView(R.id.incrementEvent);
		incrementEvent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googlePlayGamesHelper.incrementEvent(eventId.getText().toString(), 1);
			}
		});

		loadEvents = findView(R.id.loadEvents);
		loadEvents.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googlePlayGamesHelper.loadEvents(new EventsListener() {

					@Override
					public void onEventsLoaded(List<Event> events) {
						StringBuilder builder = new StringBuilder();
						for (Event event : events) {
							builder.append("Event name: ");
							builder.append(event.getName());
							builder.append("\nEvent description: ");
							builder.append(event.getDescription());
							builder.append("\nEvent value: ");
							builder.append(event.getFormattedValue());
							builder.append("\n");
							builder.append("\n");
						}
						status.setText(builder.toString());
					}
				});
			}
		});

		loadCurrentPlayer = findView(R.id.loadEvents);
		loadCurrentPlayer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googlePlayGamesHelper.loadCurrentPlayer(new CurrentPlayerListener() {
					@Override
					public void onCurrentPlayerLoaded(Player player) {
						StringBuilder builder = new StringBuilder();
						builder.append("Display Name: ");
						builder.append(player.getDisplayName());
						builder.append("\nName: ");
						builder.append(player.getName());
						builder.append("\nTitle: ");
						builder.append(player.getTitle());
						builder.append("\nCurrent Level: ");
						builder.append(player.getLevelInfo().getCurrentLevel());
						builder.append("\nCurrent XP: ");
						builder.append(player.getLevelInfo().getCurrentXpTotal());
						status.setText(builder.toString());
					}
				});
			}
		});

		loadPlayerStats = findView(R.id.loadPlayerStats);
		loadPlayerStats.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				googlePlayGamesHelper.loadPlayerStats(new PlayerStatsListener() {
					@Override
					public void onPlayerStatsLoaded(PlayerStats playerStats) {
						StringBuilder builder = new StringBuilder();
						builder.append("DaysSinceLastPlayed: ");
						builder.append(playerStats.getDaysSinceLastPlayed());
						status.setText(builder.toString());
					}
				});
			}
		});

		googlePlayGamesHelper = new GooglePlayGamesHelper(this, this);
	}

	@Override
	public void onStart() {
		super.onStart();

		googlePlayGamesHelper.silentSignIn();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		googlePlayGamesHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onGoogleSignIn(GoogleSignInAccount googleSignInAccount) {
		StringBuilder builder = new StringBuilder();
		builder.append("Display Name: ");
		builder.append(googleSignInAccount.getDisplayName());
		builder.append("\nEmail: ");
		builder.append(googleSignInAccount.getEmail());
		builder.append("\nGranted Scopes: ");
		builder.append(googleSignInAccount.getGrantedScopes());
		builder.append("\nId Token: ");
		builder.append(googleSignInAccount.getIdToken() != null ? googleSignInAccount.getIdToken().substring(0, 50) : null);
		status.setText(builder.toString());


		signInButton.setVisibility(View.GONE);
		signOutButton.setVisibility(View.VISIBLE);
		gameContainer.setVisibility(View.VISIBLE);

		Snackbar.make(findView(R.id.container), "onGoogleSignIn", Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void onGoogleSignOut() {
		status.setText(R.string.notLogged);

		signInButton.setVisibility(View.VISIBLE);
		signOutButton.setVisibility(View.GONE);
		gameContainer.setVisibility(View.GONE);

		Snackbar.make(findView(R.id.container), "onGoogleSignOut", Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void onGoogleAccessRevoked() {
		status.setText(R.string.notLogged);

		signInButton.setVisibility(View.VISIBLE);
		signOutButton.setVisibility(View.GONE);
		gameContainer.setVisibility(View.GONE);

		Snackbar.make(findView(R.id.container), "onGoogleAccessRevoked", Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return new NonBlockingLoading();
	}
}
