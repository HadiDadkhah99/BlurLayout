[![](https://jitpack.io/v/HadiDadkhah99/BlurLayout.svg)](https://jitpack.io/#HadiDadkhah99/BlurLayout)

# Android blur layout


### Step 1. Add the JitPack repository to your build file

			
```groovy
allprojects {
		repositories {
	                //...
			maven { url 'https://jitpack.io' }
		}
	}
```



### Step 2. Add the dependency

```groovy
dependencies {
	       implementation 'com.github.HadiDadkhah99:BlurLayout:$last_version'
	}
```

## How to use it

![](http://www.dadkhahhadi.ir/github/blurLayoutGif.gif)

### You must use this way to use it
```xml
<AnyLayout>

  <com.foc.libs.blurred.bottom.blurredbottomnavigation.BlurLayout
   android:id="@+id/blurLayout"
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   app:blurValue="25.0">
  
      <AnyLayout>
  
      </AnyLayout>
  
  </com.foc.libs.blurred.bottom.blurredbottomnavigation.BlurLayout>
  
  
  <com.foc.libs.blurred.bottom.blurredbottomnavigation.BlurItem
        android:id="@+id/blurItem"
        android:background="@android:color/transparent"
        android:layout_height="96dp"
        android:layout_marginBottom="24dp"
        android:layout_marginEnd="24dp"
        android:layout_marginStart="24dp"
        android:layout_width="match_parent"
        app:cardCornerRadius="12dp"
        app:cardElevation="12dp"
        >

        <AnyLayout>
  
        </AnyLayout>
        
    </com.foc.libs.blurred.bottom.blurredbottomnavigation.BlurItem>
  


</AnyLayout>
```
### in your Activity
```java
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BlurLayout blurLayout = findViewById(R.id.blurLayout);
        BlurItem blurItem = findViewById(R.id.blurItem);
        blurLayout.attachItem(blurItem);
    }
```
