package com.jdroid.android.publisher;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Apklistings;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Apks.Upload;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Commit;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Images;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Images.Deleteall;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Insert;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.Apk;
import com.google.api.services.androidpublisher.model.ApkListing;
import com.google.api.services.androidpublisher.model.ApksListResponse;
import com.google.api.services.androidpublisher.model.AppEdit;
import com.google.api.services.androidpublisher.model.ImagesUploadResponse;
import com.google.api.services.androidpublisher.model.Listing;
import com.google.api.services.androidpublisher.model.Track;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class to initialize the publisher APIs client library.
 * <p>
 * Before making any calls to the API through the client library you need to call the
 * {@link GooglePlayPublisher#init(AppContext)} method. This will run all precondition checks for for client id and
 * secret setup properly in resources/client_secrets.json and authorize this client against the API.
 * </p>
 */
public class GooglePlayPublisher {

	private GooglePlayPublisher(){}

	private static final Logger LOGGER = LoggerUtils.getLogger(GooglePlayPublisher.class);

	public static final String MIME_TYPE_APK = "application/vnd.android.package-archive";
	
	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	
	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;
	
	/**
	 * Performs all necessary setup steps for running requests against the API.
	 * 
	 * @param appContext
	 * @return the {@Link AndroidPublisher} service
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	private static AndroidPublisher init(AppContext appContext) {
		
		try {
			
			if (HTTP_TRANSPORT == null) {
				HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			}
			
			// Authorization.
			Credential credential = authorizeWithServiceAccount(appContext);
			
			// Set up and return API client.
			return new AndroidPublisher.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
				appContext.getPackageName()).build();
		} catch (GeneralSecurityException e) {
			throw new UnexpectedException(e);
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}
	
	private static Credential authorizeWithServiceAccount(AppContext appContext) throws GeneralSecurityException,
			IOException {
		System.out.println(String.format("Authorizing using Service Account: %s", appContext.getServiceAccountEmail()));
		
		// Build service account credential.
		GoogleCredential.Builder builder = new GoogleCredential.Builder();
		builder.setTransport(HTTP_TRANSPORT);
		builder.setJsonFactory(JSON_FACTORY);
		builder.setServiceAccountId(appContext.getServiceAccountEmail());
		builder.setServiceAccountScopes(Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER));
		builder.setServiceAccountPrivateKeyFromP12File(new File(appContext.getPrivateKeyFile()));
		return builder.build();
	}
	
	/**
	 * Lists all the apks for a given app.
	 * 
	 * @param appContext
	 */
	public static void listApks(AppContext appContext) {
		try {
			
			// Create the API service.
			AndroidPublisher service = init(appContext);
			Edits edits = service.edits();
			
			// Create a new edit to make changes.
			Insert editRequest = edits.insert(appContext.getPackageName(), null);
			AppEdit appEdit = editRequest.execute();
			
			// Get a list of apks.
			ApksListResponse apksResponse = edits.apks().list(appContext.getPackageName(), appEdit.getId()).execute();
			
			// Print the apk info.
			for (Apk apk : apksResponse.getApks()) {
				System.out.println(String.format("Version: %d - Binary sha1: %s", apk.getVersionCode(), apk.getBinary().getSha1()));
			}
		} catch (IOException ex) {
			LOGGER.error("Exception was thrown while updating listing", ex);
		}
	}
	
	public static void updateListings(AppContext appContext, List<LocaleListing> localeListings) {
		try {
			
			// Create the API service.
			AndroidPublisher service = init(appContext);
			Edits edits = service.edits();
			
			// Create an edit to update listing for application.
			Insert editRequest = edits.insert(appContext.getPackageName(), null);
			AppEdit edit = editRequest.execute();
			String editId = edit.getId();
			System.out.println(String.format("Created edit with id: %s", editId));
			
			// Update listing for each locale of the application.
			for (LocaleListing each : localeListings) {
				Listing listing = new Listing();
				listing.setTitle(each.getTitle());
				listing.setFullDescription(each.getFullDescription());
				listing.setShortDescription(each.getShortDescription());
				
				Edits.Listings.Update updateListingsRequest = edits.listings().update(appContext.getPackageName(),
					editId, each.getLocale().toString(), listing);
				Listing updatedListing = updateListingsRequest.execute();
				System.out.println(String.format("Created new " + each.getLocale().toString() + " app listing with title: %s",
					updatedListing.getTitle()));
				
				// Update images
				Images.Upload uploadImageRequest = edits.images().upload(appContext.getPackageName(), editId,
					each.getLocale().toString(), ImageType.FEATURE_GRAPHIC.getKey(), each.getFeatureGraphic());
				ImagesUploadResponse response = uploadImageRequest.execute();
				System.out.println(String.format("Feature graphic %s has been updated.", response.getImage()));
				
				uploadImageRequest = edits.images().upload(appContext.getPackageName(), editId,
					each.getLocale().toString(), ImageType.PROMO_GRAPHIC.getKey(), each.getPromoGraphic());
				response = uploadImageRequest.execute();
				System.out.println(String.format("Promo graphic %s has been updated.", response.getImage()));
				
				uploadImageRequest = edits.images().upload(appContext.getPackageName(), editId,
					each.getLocale().toString(), ImageType.ICON.getKey(), each.getHighResolutionIcon());
				response = uploadImageRequest.execute();
				System.out.println(String.format("High resolution icon %s has been updated.", response.getImage()));
				
				Deleteall deleteallRequest = edits.images().deleteall(appContext.getPackageName(), editId,
					each.getLocale().toString(), ImageType.PHONE_SCREENSHOTS.getKey());
				deleteallRequest.execute();
				System.out.println("Phone screenshots has been deleted.");
				for (AbstractInputStreamContent content : each.getPhoneScreenshots()) {
					uploadImageRequest = edits.images().upload(appContext.getPackageName(), editId,
						each.getLocale().toString(), ImageType.PHONE_SCREENSHOTS.getKey(), content);
					response = uploadImageRequest.execute();
					System.out.println(String.format("Phone screenshot %s has been updated.", response.getImage()));
				}
				
				deleteallRequest = edits.images().deleteall(appContext.getPackageName(), editId,
					each.getLocale().toString(), ImageType.SEVEN_INCH_SCREENSHOTS.getKey());
				deleteallRequest.execute();
				System.out.println("Seven inch screenshots has been deleted.");
				for (AbstractInputStreamContent content : each.getSevenInchScreenshots()) {
					uploadImageRequest = edits.images().upload(appContext.getPackageName(), editId,
						each.getLocale().toString(), ImageType.SEVEN_INCH_SCREENSHOTS.getKey(), content);
					response = uploadImageRequest.execute();
					System.out.println(String.format("Seven inch screenshot %s has been updated.", response.getImage()));
				}
				
				deleteallRequest = edits.images().deleteall(appContext.getPackageName(), editId,
					each.getLocale().toString(), ImageType.TEN_INCH_SCREENSHOTS.getKey());
				deleteallRequest.execute();
				System.out.println("Ten inch screenshots has been deleted.");
				for (AbstractInputStreamContent content : each.getTenInchScreenshots()) {
					uploadImageRequest = edits.images().upload(appContext.getPackageName(), editId,
						each.getLocale().toString(), ImageType.TEN_INCH_SCREENSHOTS.getKey(), content);
					response = uploadImageRequest.execute();
					System.out.println(String.format("Ten inch screenshot %s has been updated.", response.getImage()));
				}
			}
			
			commitEdit(appContext, edits, editId);
			
		} catch (IOException ex) {
			LOGGER.error("Exception was thrown while updating listing", ex);
		}
	}
	
	public static void updateApk(AppContext appContext, String apkFilePath, TrackType trackType,
			List<LocaleListing> localeListings) {
		try {
			
			if (Strings.isNullOrEmpty(apkFilePath)) {
				throw new UnexpectedException("apkPath cannot be null or empty!");
			}
			
			if (trackType == null) {
				throw new UnexpectedException("trackType cannot be null or empty!");
			}
			
			// Create the API service.
			AndroidPublisher service = init(appContext);
			Edits edits = service.edits();
			
			// Create a new edit to make changes.
			Insert editRequest = edits.insert(appContext.getPackageName(), null);
			AppEdit edit = editRequest.execute();
			String editId = edit.getId();
			System.out.println(String.format("Created edit with id: %s", editId));
			
			// Upload new apk to developer console
			String apkPath = GooglePlayPublisher.class.getResource(apkFilePath).toURI().getPath();
			AbstractInputStreamContent apkFile = new FileContent(MIME_TYPE_APK, new File(apkPath));
			Upload uploadRequest = edits.apks().upload(appContext.getPackageName(), editId, apkFile);
			Apk apk = uploadRequest.execute();
			System.out.println(String.format("Version code %d has been uploaded", apk.getVersionCode()));
			
			// Assign apk to beta track.
			List<Integer> apkVersionCodes = new ArrayList<>();
			apkVersionCodes.add(apk.getVersionCode());
			Edits.Tracks.Update updateTrackRequest = edits.tracks().update(appContext.getPackageName(), editId,
				trackType.getKey(), new Track().setVersionCodes(apkVersionCodes));
			Track updatedTrack = updateTrackRequest.execute();
			System.out.println(String.format("Track %s has been updated.", updatedTrack.getTrack()));
			
			for (LocaleListing each : localeListings) {
				// Update recent changes field in apk listing.
				ApkListing newApkListing = new ApkListing();
				newApkListing.setRecentChanges(each.getRecentChanges());
				Apklistings.Update updateRecentChangesRequest = edits.apklistings().update(appContext.getPackageName(),
					editId, apk.getVersionCode(), each.getLocale().toString(), newApkListing);
				updateRecentChangesRequest.execute();
				System.out.println("Recent changes has been updated.");
			}
			
			// Commit changes for edit.
			commitEdit(appContext, edits, editId);
			
		} catch (IOException | URISyntaxException ex) {
			LOGGER.error("Exception was thrown while uploading apk and updating recent changes", ex);
		}
	}
	
	private static void commitEdit(AppContext appContext, Edits edits, String editId) throws IOException {
		Commit commitRequest = edits.commit(appContext.getPackageName(), editId);
		AppEdit appEdit = commitRequest.execute();
		System.out.println(String.format("App edit with id %s has been comitted", appEdit.getId()));
	}
}
