package com.jdroid.android.billing;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import android.util.Base64;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Sets;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;

/**
 * Security-related methods. For a secure implementation, all of this code should be implemented on a server that
 * communicates with the application on the device. For the sake of simplicity and clarity of this example, this code is
 * included here and is executed on the device. If you must verify the purchases on the phone, you should obfuscate this
 * code to make it harder for an attacker to replace the code with stubs that treat all purchases as verified.
 */
public class Security {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(Security.class);
	
	private static final String KEY_FACTORY_ALGORITHM = "RSA";
	private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
	private static final SecureRandom RANDOM = new SecureRandom();
	
	/**
	 * This keeps track of the nonces that we generated and sent to the server. We need to keep track of these until we
	 * get back the purchase state and send a confirmation message back to the Google Play app. If we are killed and
	 * lose this list of nonces, it is not fatal. The Google Play app will send us a new "notify" message and we will
	 * re-generate a new nonce. This has to be "static" so that the {@link BillingReceiver} can check if a nonce exists.
	 */
	private static Set<Long> knownNonces = Sets.newHashSet();
	
	/**
	 * Generates a nonce (a random number used once).
	 * 
	 * @return the nonce
	 */
	public static long generateNonce() {
		long nonce = RANDOM.nextLong();
		knownNonces.add(nonce);
		return nonce;
	}
	
	public static void removeNonce(long nonce) {
		knownNonces.remove(nonce);
	}
	
	public static boolean isNonceKnown(long nonce) {
		return knownNonces.contains(nonce);
	}
	
	/**
	 * Verifies that the data was signed with the given signature, and returns the list of verified purchases. The data
	 * is in JSON format and contains a nonce (number used once) that we generated and that was signed (as part of the
	 * whole data string) with a private key. The data also contains the {@link PurchaseState} and product ID of the
	 * purchase. In the general case, there can be an array of purchase transactions because there may be delays in
	 * processing the purchase on the backend and then several purchases can be batched together.
	 * 
	 * @param signedData the signed JSON string (signed, not encrypted)
	 * @param signature the signature for the data, signed with the private key
	 * @return the {@link PurchaseOrder} list
	 */
	public static List<PurchaseOrder> verifyPurchase(String signedData, String signature) {
		if (signedData == null) {
			LOGGER.error("Signed data is null");
			return null;
		}
		LOGGER.debug("Signed data: " + signedData);
		boolean verified = false;
		if (StringUtils.isNotEmpty(signature)) {
			/**
			 * TODO Compute your public key (that you got from the Google Play app publisher site).
			 * 
			 * Instead of just storing the entire literal string here embedded in the program, construct the key at
			 * runtime from pieces or use bit manipulation (for example, XOR with some other string) to hide the actual
			 * key. The key itself is not secret information, but we don't want to make it easy for an adversary to
			 * replace the public key with one of their own and then fake messages from the server.
			 * 
			 * Generally, encryption keys / passwords should only be kept in memory long enough to perform the operation
			 * they need to perform.
			 */
			PublicKey key = Security.generatePublicKey(BillingContext.get().getGooglePlayPublicKey());
			verified = Security.verify(key, signedData, signature);
			if (!verified) {
				LOGGER.warn("Signature does not match data.");
				return null;
			}
		}
		
		JSONArray jTransactionsArray = null;
		int numTransactions = 0;
		long nonce = 0L;
		try {
			JSONObject jObject = new JSONObject(signedData);
			
			// The nonce might be null if the user backed out of the buy page.
			nonce = jObject.optLong("nonce");
			jTransactionsArray = jObject.optJSONArray("orders");
			if (jTransactionsArray != null) {
				numTransactions = jTransactionsArray.length();
			}
		} catch (JSONException e) {
			return null;
		}
		
		if (!Security.isNonceKnown(nonce)) {
			LOGGER.warn("Nonce not found: " + nonce);
			return null;
		}
		
		List<PurchaseOrder> orders = Lists.newArrayList();
		try {
			for (int i = 0; i < numTransactions; i++) {
				JSONObject jElement = jTransactionsArray.getJSONObject(i);
				int response = jElement.getInt("purchaseState");
				PurchaseState purchaseState = PurchaseState.valueOf(response);
				String productId = jElement.getString("productId");
				long purchaseTime = jElement.getLong("purchaseTime");
				String orderId = jElement.optString("orderId", "");
				String notifyId = null;
				if (jElement.has("notificationId")) {
					notifyId = jElement.getString("notificationId");
				}
				String developerPayload = jElement.optString("developerPayload", null);
				
				// If the purchase state is PURCHASED, then we require a
				// verified nonce.
				if ((purchaseState.equals(PurchaseState.PURCHASED)) && !verified) {
					continue;
				}
				orders.add(new PurchaseOrder(purchaseState, notifyId, productId, orderId, purchaseTime,
						developerPayload));
			}
		} catch (JSONException e) {
			LOGGER.error("JSON exception: ", e);
			return null;
		}
		removeNonce(nonce);
		return orders;
	}
	
	/**
	 * Generates a PublicKey instance from a string containing the Base64-encoded public key.
	 * 
	 * @param encodedPublicKey Base64-encoded public key
	 * @return The {@link PublicKey}
	 * @throws IllegalArgumentException if encodedPublicKey is invalid
	 */
	public static PublicKey generatePublicKey(String encodedPublicKey) {
		try {
			byte[] decodedKey = Base64.decode(encodedPublicKey, Base64.DEFAULT);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
			return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeySpecException e) {
			LOGGER.error("Invalid key specification.");
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Verifies that the signature from the server matches the computed signature on the data. Returns true if the data
	 * is correctly signed.
	 * 
	 * @param publicKey public key associated with the developer account
	 * @param signedData signed data from server
	 * @param signature server signature
	 * @return true if the data and signature match
	 */
	private static boolean verify(PublicKey publicKey, String signedData, String signature) {
		LOGGER.debug("Signature: " + signature);
		Signature sig;
		try {
			sig = Signature.getInstance(SIGNATURE_ALGORITHM);
			sig.initVerify(publicKey);
			sig.update(signedData.getBytes());
			if (!sig.verify(Base64.decode(signature, Base64.DEFAULT))) {
				LOGGER.error("Signature verification failed.");
				return false;
			}
			return true;
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("NoSuchAlgorithmException.");
		} catch (InvalidKeyException e) {
			LOGGER.error("Invalid key specification.");
		} catch (SignatureException e) {
			LOGGER.error("Signature exception.");
		}
		return false;
	}
}
