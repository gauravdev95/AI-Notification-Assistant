-keep class net.zetetic.** { *; }
-keep class org.sqlite.** { *; }
-dontwarn net.zetetic.**
-dontwarn org.sqlite.**

-keepattributes Signature
-keepattributes *Annotation*

-keep class com.google.firebase.** { *; }
-keep class com.google.** { *; }

-keep class com.gaurav.ainotificationassistant.** { *; }

-keepclasseswithmembers class * {
    @com.google.dagger.hilt.* <fields>;
}
-keepclasseswithmembers class * {
    @com.google.dagger.hilt.* <methods>;
}

-keepclasseswithmembers class * {
    @androidx.room.* <methods>;
}

-dontwarn android.util.StatsLog
