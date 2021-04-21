# LINE Login x Firebase for Android
Code guideline in integrating LINE Login with Firebase for Android developers

## Prerequisites
* Android Studio 3.5.3 or higher
* Targets API level 21 (Lollipop) or later
* Uses Gradle 4.1 or later
* google-services.json in app-level folder
* [Create a channel on the LINE Developers console](https://developers.line.me/en/docs/line-login/getting-started/)
* Create a Firebase project using the [Firebase Console](https://console.firebase.google.com/) and select <b>Blaze plan</b>

## Cloud Functions for Firebase
* Copy LINE Channel ID and set line.channelid in Google Cloud environment variable
```
firebase functions:config:set line.channelid="<your_channel_id>"
```

## Features
* App Login (SDK)
* Web Login (SDK)
* Get Profile (SDK)
* Verify Token (SDK)
* Refresh Token (SDK)
* Login with OpenID(v2.1) including Email
* Create custom token by Cloud Functions for Firebase
* Sign in with Firebase Custom Token
* Logout

## Screenshots
<img src="https://user-images.githubusercontent.com/1763410/37565054-7dd4ed04-2ad4-11e8-9cec-f76b6933db5d.png" width="100%">

## Documentation
* [LINE Login](https://developers.line.me/en/docs/line-login/overview/)
* [Cloud Functions for Firebase](https://firebase.google.com/docs/functions/get-started)

## Blog
* [สร้างระบบ Firebase Custom Authentication ด้วย LINE Login v2.1](https://medium.com/@jirawatee/%E0%B8%AA%E0%B8%A3%E0%B9%89%E0%B8%B2%E0%B8%87%E0%B8%A3%E0%B8%B0%E0%B8%9A%E0%B8%9A-firebase-custom-authentication-%E0%B8%94%E0%B9%89%E0%B8%A7%E0%B8%A2-line-login-v2-1-42f7dc35c9bb)

## Slide Decks
* [LINE Developer Meetup, Thailand](https://docs.google.com/presentation/d/1HCgfoc2J2H5oyTYtULPsrbKaw3EL4D_o0vBj5zKrbTQ/edit?usp=sharing)
