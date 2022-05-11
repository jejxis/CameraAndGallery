# CameraAndGallery
이것이 안드로이드다 with 코틀린 개정3판
AndroidManifest.xml 파일의 <provider> 태그의 android:authorities의 값이 책에는 "${applicationID}.provider"였지만 잘 되지 않아서 build.gradle(:app)의 android{.. defaultConfig{
  applicationID의 값을 ${applicationID}의 자리에 넣었더니 해결되었다.
