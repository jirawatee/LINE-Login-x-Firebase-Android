'use strict';

const functions = require('firebase-functions');
const request = require('request-promise');
const admin = require('firebase-admin');

const serviceAccount = require('./service-account.json');
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

exports.createCustomToken = functions.https.onRequest((request, response) => {
  if (request.body.access_token === undefined) {
    const ret = {
      error_message: 'AccessToken not found',
    };
    return response.status(400).send(ret);
  }
  
  return verifyLineToken(request.body)
    .then((customAuthToken) => {
      const ret = {
        firebase_token: customAuthToken,
      };
      return response.status(200).send(ret);
    }).catch((err) => {
      const ret = {
        error_message: `Authentication error: ${err}`,
      };
      return response.status(200).send(ret);
    });
});

function verifyLineToken(body) {
  return request({
    method: 'GET',
    uri: `https://api.line.me/oauth2/v2.1/verify?access_token=${body.access_token}`,
    json: true
  }).then((response) => {
    if (response.client_id !== functions.config().line.channelid) {
      return Promise.reject(new Error('LINE channel ID mismatched'));
    }
    return getFirebaseUser(body);
  }).then((userRecord) => {
    return admin.auth().createCustomToken(userRecord.uid);
  }).then((token) => {
    return token;
  });
}

function getFirebaseUser(body) {
  const firebaseUid = `line:${body.id}`;

  return admin.auth().getUser(firebaseUid).then(function(userRecord) {
    return userRecord;
  }).catch((error) => {
    if (error.code === 'auth/user-not-found') {
        return admin.auth().createUser({
          uid: firebaseUid,
          displayName: body.name,
          photoURL: body.picture,
          email: body.email
        });
    }
    return Promise.reject(error);
  });
}