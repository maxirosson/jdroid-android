package com.jdroid.android.google.playgames;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.AnnotatedData;
import com.google.android.gms.games.EventsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerStatsClient;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.event.Event;
import com.google.android.gms.games.event.EventBuffer;
import com.google.android.gms.games.stats.PlayerStats;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.signin.GoogleSignInHelper;
import com.jdroid.android.google.signin.GoogleSignInListener;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.RandomUtils;

import org.slf4j.Logger;

import java.util.List;

import androidx.annotation.NonNull;

public class GooglePlayGamesHelper extends GoogleSignInHelper {

	private static Logger LOGGER = LoggerUtils.getLogger(GooglePlayGamesHelper.class);

	private static final int UNUSED_REQUEST_CODE = RandomUtils.INSTANCE.get16BitsInt();

	private LeaderboardsClient leaderboardsClient;
	private AchievementsClient achievementsClient;
	private EventsClient eventsClient;
	private PlayersClient playersClient;
	private PlayerStatsClient playerStatsClient;

	public GooglePlayGamesHelper(AbstractFragment abstractFragment, GoogleSignInListener googleSignInListener) {
		super(abstractFragment, googleSignInListener);
	}

	@Override
	protected GoogleSignInOptions getGoogleSignInOptions() {
		return GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
	}

	@Override
	protected void onGoogleSignIn(GoogleSignInAccount account) {
		super.onGoogleSignIn(account);

		leaderboardsClient = Games.getLeaderboardsClient(getAbstractFragment().getContext(), account);
		achievementsClient = Games.getAchievementsClient(getAbstractFragment().getContext(), account);
		eventsClient = Games.getEventsClient(getAbstractFragment().getContext(), account);
		playersClient = Games.getPlayersClient(getAbstractFragment().getContext(), account);
		playerStatsClient = Games.getPlayerStatsClient(getAbstractFragment().getContext(), account);
	}

	@Override
	protected void onGoogleSignOut() {
		super.onGoogleSignOut();

		achievementsClient = null;
		leaderboardsClient = null;
		eventsClient = null;
		playersClient = null;
		playerStatsClient = null;
	}

	@Override
	protected void onGoogleAccessRevoked() {
		super.onGoogleAccessRevoked();

		achievementsClient = null;
		leaderboardsClient = null;
		eventsClient = null;
		playersClient = null;
		playerStatsClient = null;
	}

	public void submitScore(@NonNull String leaderboardId, long score) {
		if (leaderboardsClient != null) {
			leaderboardsClient.submitScore(leaderboardId, score);
		}
	}

	public void showLeaderboard(@NonNull String leaderboardId) {
		if (leaderboardsClient != null) {
			leaderboardsClient.getLeaderboardIntent(leaderboardId).addOnSuccessListener(new OnSuccessListener<Intent>() {
				@Override
				public void onSuccess(Intent intent) {
					ActivityLauncher.startActivityForResult(getAbstractFragment().getActivity(), intent, UNUSED_REQUEST_CODE);
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
				}
			});
		}
	}

	public void showAllLeaderboards() {
		if (leaderboardsClient != null) {
			leaderboardsClient.getAllLeaderboardsIntent().addOnSuccessListener(new OnSuccessListener<Intent>() {
				@Override
				public void onSuccess(Intent intent) {
					ActivityLauncher.startActivityForResult(getAbstractFragment().getActivity(), intent, UNUSED_REQUEST_CODE);
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
				}
			});
		}
	}

	public void unlockAchievement(@NonNull String achievementId) {
		if (achievementsClient != null) {
			achievementsClient.unlock(achievementId);
		}
	}

	public void incrementAchievement(@NonNull String achievementId, int numSteps) {
		if (achievementsClient != null) {
			achievementsClient.increment(achievementId, numSteps);
		}
	}

	public void showAchievements() {
		if (achievementsClient != null) {
			achievementsClient.getAchievementsIntent().addOnSuccessListener(new OnSuccessListener<Intent>() {
				@Override
				public void onSuccess(Intent intent) {
					ActivityLauncher.startActivityForResult(getAbstractFragment().getActivity(), intent, UNUSED_REQUEST_CODE);
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
				}
			});
		}
	}

	public void incrementEvent(@NonNull String eventId, int incrementAmount) {
		if (eventsClient != null) {
			eventsClient.increment(eventId, incrementAmount);
		}
	}

	public void loadEvents(@NonNull EventsListener eventsListener) {
		loadEvents(eventsListener, false);
	}

	public void loadEvents(@NonNull EventsListener eventsListener, boolean forceReload) {
		if (eventsClient != null) {
			eventsClient.load(forceReload).addOnSuccessListener(new OnSuccessListener<AnnotatedData<EventBuffer>>() {
				@Override
				public void onSuccess(AnnotatedData<EventBuffer> eventBufferAnnotatedData) {
					EventBuffer eventBuffer = eventBufferAnnotatedData.get();

					int count = 0;
					if (eventBuffer != null) {
						count = eventBuffer.getCount();
					}
					LOGGER.info("Events loaded: " + count);

					List<Event> events = Lists.newArrayList();
					for (int i = 0; i < count; i++) {
						Event event = eventBuffer.get(i);
						LOGGER.info("Event: " + event.getName() + " -> " + event.getValue());
						events.add(event);
					}
					eventsListener.onEventsLoaded(events);
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
				}
			});
		}
	}

	public void loadCurrentPlayer(CurrentPlayerListener currentPlayerListener) {
		playersClient.getCurrentPlayer().addOnCompleteListener(new OnCompleteListener<Player>() {
			@Override
			public void onComplete(@NonNull Task<Player> task) {
				if (task.isSuccessful()) {
					currentPlayerListener.onCurrentPlayerLoaded(task.getResult());
				} else {
					Exception e = task.getException();
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
				}
			}
		});
	}

	public void loadPlayerStats(PlayerStatsListener playerStatsListener) {
		loadPlayerStats(playerStatsListener, false);
	}

	public void loadPlayerStats(PlayerStatsListener playerStatsListener, boolean forceReload) {
		playerStatsClient.loadPlayerStats(forceReload).addOnSuccessListener(new OnSuccessListener<AnnotatedData<PlayerStats>>() {
			@Override
			public void onSuccess(AnnotatedData<PlayerStats> playerStatsAnnotatedData) {
				PlayerStats playerStats = playerStatsAnnotatedData.get();
				playerStatsListener.onPlayerStatsLoaded(playerStats);
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
			}
		});
	}
}
