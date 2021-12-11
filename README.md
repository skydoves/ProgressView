<h1 align="center">ProgressView</h1></br>
<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=17"><img alt="API" src="https://img.shields.io/badge/API-17%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/skydoves/progressview/actions"><img alt="CI" src="https://github.com/skydoves/ProgressView/workflows/Android%20CI/badge.svg"/></a>
  <a href="https://medium.com/@skydoves/a-polished-and-flexible-progress-view-for-android-5a4e90eff25e"><img alt="Medium" src="https://skydoves.github.io/badges/Story-Medium.svg"/></a>
  <a href="https://github.com/skydoves"><img alt="License" src="https://skydoves.github.io/badges/skydoves.svg"/>
  <a href="https://skydoves.github.io/libraries/progressview/javadoc/progressview/com.skydoves.progressview/index.html"><img alt="API" src="https://img.shields.io/badge/Javadoc-ProgressView-yellow"/></a>
</p>

<p align="center">
🌊 A polished and flexible ProgressView, fully customizable with animations.
</p>

<p align="center">
<img src="https://user-images.githubusercontent.com/24237865/63502889-6916f300-c509-11e9-893a-d634f1c6a850.gif" width="32%"/>
<img src="https://user-images.githubusercontent.com/24237865/63537603-182aed00-c551-11e9-95ea-08e25517f046.gif" width="32%"/>
</p>

## UI/UX Design Philosophy
![1_z4zp5wWQ202LaX2q9zPyjA](https://user-images.githubusercontent.com/24237865/82746964-1e941100-9dd0-11ea-80a1-1ee88209c84b.png)

> <p align="center">Label is integrated into the progress bar and the label moves flexibly according to the progress. <br>If the width size of the label is bigger than the width size of the progress, the label will be located on the outside (right side) of the progress bar. We can consider the color of the label according to the color of the container. <br>And if the width size of the progress is bigger than the width size of the label, the label will be located on the inside of the progress bar. ProgressView follows the color consistency of the label. <br>You can check more details about it on the <a href="https://medium.com/@skydoves/a-polished-and-flexible-progress-view-for-android-5a4e90eff25e" target="_blank"> medium post</a>. </p>


## Including in your project
[![Maven Central](https://img.shields.io/maven-central/v/com.github.skydoves/progressview.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.skydoves%22%20AND%20a:%22progressview%22)
[![Jitpack](https://jitpack.io/v/skydoves/ProgressView.svg)](https://jitpack.io/#skydoves/ProgressView)
### Gradle 
Add below codes to your **root** `build.gradle` file (not your module build.gradle file).
```gradle
allprojects {
    repositories {
        mavenCentral()
    }
}
```
And add a dependency code to your **module**'s `build.gradle` file.
```gradle
dependencies {
    implementation "com.github.skydoves:progressview:1.1.3"
}
```

## Usage
Add following XML namespace inside your XML layout file.

```gradle
xmlns:app="http://schemas.android.com/apk/res-auto"
```

### ProgressView
Here is a basic example of implementing `ProgressView`.

```gradle
<com.skydoves.progressview.ProgressView
  android:id="@+id/progressView1"
  android:layout_width="match_parent"
  android:layout_height="35dp"
  app:progressView_colorBackground="@color/white" // the color of the container.
  app:progressView_colorProgress="@color/skyBlue" // the color of the progress bar.
  app:progressView_progress="40" // progress value.
  app:progressView_min="15" // min progress value.
  app:progressView_max="100" // max progress value.
  app:progressView_progressFromPrevious="true" // animates progress from previous progress.
  app:progressView_autoAnimate="true" // starts filling animation automatically when finishing inflate.
  app:progressView_radius="12dp" // the corner radius of the progressView and progress bar.
  app:progressView_padding="1dp" // the padding of the progress bar.
  app:progressView_labelColorInner="@color/white" // the text color when the label placed on the progress bar.
  app:progressView_labelColorOuter="@color/black" // the text color when the label placed out of the progress bar.
  app:progressView_labelSize="13sp" // the label text size.
  app:progressView_labelSpace="10dp" // the space size of the between label and progress bar. 
  app:progressView_labelText="achieve 65%" // the text of the label.
  app:progressView_labelTypeface="bold" // the typeface of the label.
 />
```

### Gradient
We can give a gradient effect to the progress bar using the below attributes.

```gradle
app:progressView_colorGradientStart="@color/md_yellow_100" // starting color of the gradient.
app:progressView_colorGradientEnd="@color/md_yellow_200" // ending color of the gradient.
```

We can change the progress color and gradient colors using below methods.
```kotlin
progressView.highlightView.color = ContextCompat.getColor(this, R.color.colorPrimary)
progressView.highlightView.colorGradientEnd = ContextCompat.getColor(this, R.color.colorPrimary)
progressView.highlightView.colorGradientStart = ContextCompat.getColor(this, R.color.colorPrimary)
```

### Progress filling Animation
We can implement progress filling animation using the below attributes or method.

```gradle
app:progressView_autoAnimate="true" // starts filling animation automatically when progress is changed.

// if you want to animate progress from previous progress.
app:progressView_progressFromPrevious="true"
```

or we can animate manually using below method.

```kotlin
progressView.progressAnimate()
```

### ProgressViewAnimation
We can implement progress animations when the progress value is changed.

```kotlin
BalloonAnimation.NORMAL
BalloonAnimation.BOUNCE
BalloonAnimation.DECELERATE
BalloonAnimation.ACCELERATEDECELERATE
```

NORMAL | BOUNCE | DECELERATE | ACCELERATEDECELERATE |
| :---------------: | :---------------: | :---------------: | :---------------: |
| <img src="https://user-images.githubusercontent.com/24237865/76772026-926bd900-67e3-11ea-9843-13322bdb5328.gif" align="center" width="100%"/> | <img src="https://user-images.githubusercontent.com/24237865/76772034-9566c980-67e3-11ea-9bec-11efb6e2e4c0.gif" align="center" width="100%"/> | <img src="https://user-images.githubusercontent.com/24237865/76772038-9697f680-67e3-11ea-8fd8-3c1dd637fb73.gif" align="center" width="100%"/> | <img src="https://user-images.githubusercontent.com/24237865/76772042-97c92380-67e3-11ea-9a3b-60d5969af42c.gif" align="center" width="100%"/> |

If we want to use our customized `interpolator`, we can use below method.
```java
progressView.interpolator = new NiceInterpolator(); 
```

### Highlighting Effect
We can give a highlighting effect on the progress bar when clicked.

```gradle
app:progressView_highlighting="true" // gives the highlighting effect or not.
app:progressView_highlightAlpha="0.8" // the alpha of the highlight.
app:progressView_highlightColor="@color/skyBlue" // the color of the highlight.
app:progressView_highlightWidth="1.5dp" // the thickness of the highlight.
app:progressView_padding="1.5dp" // the padding of the progress bar.
```

### OnProgressChangeListener, OnProgressClickListener
We can listen to the progress value is changed or the progressbar is clicked.

```java
progressView.setOnProgressChangeListener(new OnProgressChangeListener() {
  @Override
  public void onChange(float progress) {
    progressView.setLabelText(progress + "% archived");
  }
});

progressView.setOnProgressClickListener(new OnProgressClickListener() {
  @Override
  public void onClickProgress(boolean highlighting) {
    // do something
  }
});
```

We can simplify it using kotlin.

```kotlin
progressView.setOnProgressChangeListener { progressView.labelText = "achieve ${it.toInt()}%" }
progressView.setOnProgressClickListener { Toast.makeText(baseContext, "clicked", Toast.LENGTH_SHORT).show() }
```

### Vertical Orientation
We can implement the `ProgressView` vertically using the below option. <br>
We should set the width and height value like vertical shape.

```gradle
<com.skydoves.progressview.ProgressView
  android:layout_width="35dp"
  android:layout_height="300dp"
  app:progressView_orientation="vertical"
  
  ...
```

### TextForm
TextFrom is an attribute class that has some attributes about TextView for customizing `ProgressView`'s label.

```java
TextForm textForm = new TextForm.Builder(context)
    .setText("This is a TextForm")
    .setTextColor(R.color.colorPrimary)
    .setTextSize(14f)
    .setTextTypeFace(Typeface.BOLD)
    .build();

progressView.setTextForm(textForm);
```

This is how to create `TextForm` using kotlin dsl.

```kotlin
val form = textForm(context) {
  text = "This is a TextForm"
  textColor = ContextCompat.getColor(baseContext, R.color.white_87)
  textSize = 14f
  textTypeface = Typeface.BOLD
}
```

### Create using Builder
We can create the `ProgressView` using `ProgressView.Builder`.

```java
ProgressView progressView = new ProgressView.Builder(context)
    .setSize(300, 35)
    .setProgress(70f)
    .setMax(100f)
    .setRadius(12f)
    .setDuration(1200L)
    .setAutoAnimate(true)
    .setLabelColorInner(ContextCompat.getColor(context, R.color.white))
    .setLabelColorOuter(ContextCompat.getColor(context, R.color.black))
    .setLabelText("archive 70%")
    .setLabelSize(13f)
    .setLabelSpace(10f)
    .setLabelTypeface(Typeface.BOLD);
```
This is how to create an instance of the `ProgressView` using kotlin dsl.
```kotlin
val myProgressView = progressView(context) {
    setSize(300, 35)
    setProgress(70f)
    setMin(10f)
    setMax(100f)
    setRadius(12f)
    setDuration(1200L)
    setAutoAnimate(true)
    setLabelColorInner(ContextCompat.getColor(context, R.color.white))
    setLabelColorOuter(ContextCompat.getColor(context, R.color.black))
    setLabelText("archive 70%")
    setLabelSize(13f)
    setLabelSpace(10f)
    setLabelTypeface(Typeface.BOLD)
}
```

## ProgressView Attributes
Here are attributes of the `ProgressView` and `ProgressView.Builder`.

### Progressbar
Attributes | Type | Default | Description
--- | --- | --- | ---
orientation | ProgressViewOrientation | Horizontal | ProgressView's orientation.
progress | Float | 0f | value of the progress.
min | Float | 0f | value of the minimum progress. The progress value can not under the min value.
max | Float | 100f | value of the maximum progress. The progress value can not over the max value.
radius | Dimension | 8dp | corner radius of the ProgressView.
padding | Dimension | 0dp | padding of the progressbar.
duration | Long | 1000L | duration of the animation.
colorProgress | Color | colorPrimary | color of the progressbar.
colorBackground | Color | Color.WHITE | color of the container.
colorGradientStart | Color | colorPrimary | starting color of the gradient.
colorGradientEnd | Color | colorPrimary | ending color of the gradient.
borderColor | Color | colorBackground | a border color of the container.
borderWidth | Int | 0 | a border width size of the container.
autoAnimate | Boolean | true | starts filling animation automatically when finishing inflate.
animation | ProgressViewAnimation | NORMAL | animation of the progress animation.
progressFromPrevious | Boolean | false | animates progress from previous progress.

### Label
Attributes | Type | Default | Description
--- | --- | --- | ---
labelText | String | "" | text of the progressbar label.
labelSize | Dimension | 12p | text size of the progressbar label.
labelColorInner | Int(Color) | colorPrimary | text color when the label placed on the progress bar.
labelColorOuter | Int(Color) | colorPrimary | text color when the label placed out of the progress bar.
labelSpace | Dimension | 8dp | space size of the between label and progress bar. 
labelTypeface | Typeface | Typeface.NORMAL | typeface of the label.
labelConstraints | ProgressLabelConstraints | Progress | determines the constraints of the label positioning.

### Highlighting
Attributes | Type | Default | Description
--- | --- | --- | ---
highlighting | Boolean | false | highlights the progressbar when clicked.
highlightColor | Int(Color) | colorPrimary | color of the highlighting effect.
highlightAlpha | Float | 1f | alpha of the highlighting effect.
highlightWidth | Float | 0dp | thickness of the highlighting effect.

## Find this library useful? :heart:
Support it by joining __[stargazers](https://github.com/skydoves/ProgressView/stargazers)__ for this repository. :star:

# License
```xml
Copyright 2019 skydoves (Jaewoong Eum)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
