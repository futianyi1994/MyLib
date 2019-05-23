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


#1.基本指令区
-optimizationpasses 5 #代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-dontusemixedcaseclassnames #混合时不使用大小写混合，混合后的类名为小写,windows下必须使用该选项
-dontskipnonpubliclibraryclasses #指定不去忽略非公共库的类
-dontskipnonpubliclibraryclassmembers #指定不去忽略非公共库的类成员
-dontoptimize #不进行优化，建议使用此选项
-dontpreverify #不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆
#-ignorewarning #遇到警告的时候，忽略警告继续执行ProGuard，避免打包时某些警告出现，不建议添加此项
-verbose #把所有信息都输出，而不仅仅是输出出错信息
-printmapping proguardMapping.txt #包含有类名->混淆后类名的映射关系
-optimizations !code/simplification/cast,!field/*,!class/merging/* # 指定混淆是采用的算法，后面的参数是一个过滤器，这个过滤器是谷歌推荐的算法，一般不做更改
-keepattributes *Annotation*,Signature,Exceptions,InnerClasses,Deprecated,SourceFile,LineNumberTable,EnclosingMethod #不混淆Annotation,泛型等


#2.默认保留区
#保留四大组件、自定义application等类不被混淆，这些类很有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.support.multidex.MultiDexApplication
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

#保留support下的所有类及其内部类
-keep class android.support.** { *;}

#保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

#保留R下面的资源
-keep class **.R$* { *;}

#保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保留Parcelable不被混淆：否则会产生 Android.os.BadParcelableException 异常
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保留某些构造方法不能去混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保留native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保留在Activity中的方法参数是view的方法，这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

#保留枚举：使用enum类型时需要注意避免以下两个方法混淆，因为enum类的特殊性，以下两个方法会被反射调用（反射用到的类混淆时需要注意：只要保持反射用到的类名和方法即可，并不需要将整个被反射到的类都进行保持）
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保留带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

#保留这些js要调用的原生方法不能够被混淆，于是我们需要做如下处理
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

#保留app中使用了webView需要进行特殊处理
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}


##################################################以上为默认#########################################################
####################################################分割线###########################################################
##################################################以下为三方#########################################################


#AndroidUtilCode 开始
-keep class com.blankj.utilcode.** { *; }
-keepclassmembers class com.blankj.utilcode.** { *; }
-dontwarn com.blankj.utilcode.**
#AndroidUtilCode 结束

#Okhttp开始
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *; }
-dontwarn okio.**
#Okhttp结束

#Retrofit开始
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions
#Retrofit结束

#Retrolambda开始
-dontwarn java.lang.invoke.*
#Retrolambda结束

#RxJava RxAndroid开始
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#RxJava RxAndroid结束

#Rxandroid-1.2.1开始
-keepclassmembers class rx.android.**{ *; }
#Rxandroid-1.2.1结束

#Gson开始
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
-keep class org.xz_sale.entity.**{ *; }
-keep class com.google.gson.** { *; }
-keep class com.google.**{ *; }
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
#Gson结束

#PersistentCookieJar开始
-dontwarn com.franmontiel.persistentcookiejar.**
-keep class com.franmontiel.persistentcookiejar.**
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#PersistentCookieJar结束


##################################################以上为三方#########################################################
####################################################分割线###########################################################
##################################################以下为 APP#########################################################


#不要混淆BasePresenter所有子类
-keep public class * extends com.bracks.futia.mylib.base.basemvp.BasePresenter

#不要混淆Base所有子类的属性与方法
-keepclasseswithmembers class * extends com.bracks.futia.mylib.base.model.Base{
    <fields>;
    <methods>;
}

#不要混淆Entity所有子类的属性与方法
-keepclasseswithmembers class * extends com.bracks.futia.mylib.base.model.Entity{
    <fields>;
    <methods>;
}

#不要混淆Result所有子类的属性与方法
-keepclasseswithmembers class * extends com.bracks.futia.mylib.base.model.Result{
    <fields>;
    <methods>;
}

#MyLib开始
#-keep class com.bracks.futia.mylib.** { *; }
#-keepclassmembers class com.bracks.futia.mylib.** { *; }
#-dontwarn com.bracks.futia.mylib.**
#MyLib结束

#声明第三方jar包,不用管第三方jar包中的.so文件(如果有)
#-libraryjars libs/xutils.jar

#不要混淆内部类
#-keepclassmembers class com.bracks.futia.mylib.base.adapter.BaseBindingAdapter$* { *; }

#不要混淆实体类
#-keep class com.amyrobotics.medicalmission.model.** { *; }

#不要混淆用到反射的类（反射用到的类混淆时需要注意：只要保持反射用到的类名和方法即可，并不需要将整个被反射到的类都进行保持）