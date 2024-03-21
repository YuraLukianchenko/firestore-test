* Before you start you need running firestore DB. Also don't forget to check your ProjectID (cloud project) in firestore options.
* This app can be run as is in case you're running it in the cloud where firestore DB is running.
* If you are running it outside the cloud you need to configure service account in your cloud (with roles/datastore.user role)
  Create json key for the service account, download it, set env var GOOGLE_APPLICATION_CREDENTIALS=pathToKey.json
