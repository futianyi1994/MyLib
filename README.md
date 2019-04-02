# MyLib

add project build gradle:

allprojects {

    repositories {
    
        ...
        
        maven { url "https://raw.githubusercontent.com/futianyi1994/MyLib/maven" }
      
    }
}

add app build gradle:
dependencies {

    ...
    
    implementation 'com.bracks.futia:mylib:1.0.0'
    
    annotationProcessor rootProject.ext.dependencies["room-compiler"]
    
    annotationProcessor rootProject.ext.dependencies["lifecycle-compiler"]
    
    annotationProcessor rootProject.ext.dependencies["butterknife-compiler"]
    
}
