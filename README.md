# Pepet
Tamagotchi-like application with minigames for school course TAMZ II created in Android Studio.

Main activity is frog pet with stats. You can fill the stats by playing minigames:
* fill Love stat by petting your frog
<img src="pics/Screenshot_2021-02-11-11-59-23-72.jpg" width="100" alt="Main screen">
* fill Happy stat with playing Flappy Pepe
<img src="pics/Screenshot_2021-02-11-11-59-44-59.jpg" width="100" alt="Floppy Pepe minigame">
* fill Fed stat with catching flies and beers. Avoid wasps! (hold to move right, release to move left)
<img src="pics/Screenshot_2021-02-11-12-00-16-80.jpg" width="100" alt="Feeding minigame">
* fill Social stat with taking selfies and put meme stickers over faces (auto face detection)
<img src="pics/Screenshot_2021-02-11-12-00-53-72.jpg" width="100" alt="Social minigame">
* fill Fit stat by dancing to Gandalf Saxguy music (when arrow is hitting bottom, tilt your phone that way)
<img src="pics/Screenshot_2021-02-11-11-59-56-21.jpg" width="100" alt="Dancing minigame">

This app is in Java and uses
* `Camera` and [OpenCV lib](https://github.com/opencv/opencv/tree/3.4.7) for increasing Social stat  
* `Sensors` (accelerometer) and [GIF lib](https://github.com/koral--/android-gif-drawable) for dance minigame   
* `Thread` & `Canvas` for Flappy Pepe minigame  
* `Timer` & `Handler` for other minigames  
* `SoundPool` for few minigames
* `SharedPreferences` for pet's stats and high scores


Optimalized for 1080x2280 resolution phone with [Android 9 (API level 28)](https://developer.android.com/about/versions/pie/android-9.0.html).
