# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 카카오 API
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter

#다윈 관련
-keep class com.dawin.** { *; }
-dontwarn com.dawin.**
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-keep class com.samsung.** { *; }
-dontwarn com.samsung.**

#Google 라이브러리
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{public *;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{public *;}

#Vistcuit 라이브러리관련
-keep class com.viscuit.sdk.** { *; }

#카카오톡 라이브러리
-keep class com.kakao.** { *; }
-keepattributes Signature
-keepclassmembers class * {
  public static <fields>;
  public *;
}

-dontwarn android.support.v4.**,org.slf4j.**,com.google.android.gms.**

#퍼플관련
-keep class com.crossmedia.** { *; }
-dontwarn com.crossmedia.**

#핀크럭스
-dontwarn com.pincrux.offerwall.**
-keep interface com.pincrux.offerwall.** { *; }
-keep class com.pincrux.offerwall.** { *; }

#쉘위에드 관련
-dontwarn com.co.shallwead.sdk.**
-keep class com.co.shallwead.sdk.** { *; }

#TNK 관련
-keep class com.tnkfactory.** { *;}

#igaworks 관련
##---------------Begin: proguard configuration for Igaworks Common  ----------
-keep class com.igaworks.** { *; }
-dontwarn com.igaworks.**
##---------------End: proguard configuration for Igaworks Common   ----------

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Application classes that will be serialized/deserialized over Gson
-keep class com.igaworks.adbrix.model.** { *; }

# NAS SDK Proguard
-dontwarn com.nextapps.naswall.**
-keep class com.nextapps.naswall.** {
    *;
}