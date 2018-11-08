# mysmartusc
Team members: Thomas Psyhogeos, Anay Patel, Chase Hainsworth, Brandon Dalton, Alex Young

Instructions for running MySmartUSC application
  1) Download .zip file into Android Studio.
  2) Before running, open Gradle in Android Studio (on the far right sidebar) then navigate to mysmartusc (or whatever you 
     name the project on your computer) / app / Tasks / android, then click signingReport.
  3) After running signingReport, find the SHA1 code given in the Run log and copy the code.
  4) Send the SHA1 code to hainswor@usc.edu. This is the email of one of our members, Chase Hainsworth, who will add you to 
     the project's Gmail API credentials. Without this, you will not be able to use the app after signing in.
  5) Chase will send you an email when he has added you. After this, you may run the app in Android Studio.
  Note: if we were publishing this app on the Google Play store, we could generate a signed APK so all users would not need to send in their own SHA. Since you're building it from scratch, we need to add your debug keystore credentials to our project.
  Note: testing was performed with Nexus 5 emulator running Marshmallow OS.

Instructions for using application
  1) The first step to using the app is to sign in with your Gmail credentials. This will take you to the homepage.
  2) On the homepage, the three buttons take you to the Notifications page, the Settings page, or the Account page.
  3) On the notifications page, you can select any of the three buttons to see a list of the latest received emails that were      given a certain designation (i.e., they were either starred and saved, marked as urgent, or marked as spam) by the app.
  4) On the settings page, you can enter a set of keywords (separated by commas if you want to enter more than one) in each        text field. These keywords will allow the app to filter emails into the three groups listed above. The three buttons on 
     the bottom allow you to assign the keywords to only apply to emails' sender addresses, subject lines, or the body of the 
     email itself. For instance, if you want to be instantly notified if your professor sends you an email saying your last 
     test has been graded, you can enter "test", "grades," in the "Urgent" text field, then press the "Save Body" button at 
     the bottom. Then, you can enter your professor's email address in the same text field and press the "Save Sender" button.
  5) On the account page (which also shows up when you log into the app), you can either sign out of the app or go to the 
     homepage.
     
 Sprint #1 New Features:
  1) We fixed the bug that sometimes caused the application to crash after tapping on the urgent push notification. We also 
     added the core feature of marking emails as read if they do not contain any keywords. We have added a page that displays      current keywords and added the ability to move them.
  2) We updated the user interface design to match the material design standards (most activities have been updated).  
     Activities created in this current sprint have not been updated yet.
  3) We also discussed how we could possibly improve our workflow for the next sprint. Including what tasks people enjoy and  
     what new areas people think that they could assist in.
  4) We added a remove keyword functionality via the KeywordRemoveActivity and the ViewKeywordActivity where users can view 
     and remove any given keyword that they previously entered.
