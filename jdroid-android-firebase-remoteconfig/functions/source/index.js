const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

exports.propagateFirebaseRemoteConfig = functions.remoteConfig.onUpdate(versionMetadata => {
  // Create FCM payload to send data message to PUSH_RC topic.
  const payload = {
    topic: "FIREBASE_RC_PROPAGATION",
    data: {
      "messageKey": "firebaseRemoteConfigFetchFcmMessage"
    }
  };
  // Use the Admin SDK to send the ping via FCM.
  return admin.messaging().send(payload).then(resp => {
    console.log(resp);
    return null;
  });
});