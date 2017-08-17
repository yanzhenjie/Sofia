状态栏一体化，包括随着滑动渐变，透明状态栏，黑色字体的状态栏，兼容国产魅族、小米手机，其它手机使用标准模式。

我的主页：[http://www.yanzhenjie.com](http://www.yanzhenjie.com)  
欢迎关注我的微博：[http://weibo.com/yanzhenjieit](http://weibo.com/yanzhenjieit)  

**QQ技术交流群：[547839514](https://jq.qq.com/?_wv=1027&k=4Ev0ksp)**  

# 截图
<image src="./image/1.gif" width="270">  <image src="./image/2.gif"  width="270">  <image src="./image/3.gif"  width="270">

# 如何使用
依赖本项目后，使用 `StatusView`和`StatusUtils`简单几行代码即可完成状态栏一体化。

## 依赖
* Gradle
```groovy
compile 'com.yanzhenjie:statusview:1.0.1'
```

* Maven
```xml
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>statusview</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

## 类和方法介绍
1. `StatusView`：用来替代系统的Status，开发者只需要控制它的`background`属性即可，其它都会自动处。
2. `StatusUtils`：用来设置`Activity`的状态栏和导航栏，这个类的方法要着重说明。
- `setStatusBarColor`：设置状态栏颜色。
- `setNavigationBarColor`：设置导航栏颜色。 
- `setSystemBarColor`：同时设置状态栏、导航栏颜色，每个颜色可以单独指定。
- `setLayoutFullScreen`：设置`Activity`的`ContentView`占满全屏，并让`StatusBar`透明，内容侵入状态栏下层。
 - `setStatusBarDarkFont`：设置状态栏的字体为深色，一般用于当`Toolbar`和状态栏为浅色时（比如白色状态栏）。

## 图一效果实现
`ContentView`的顶部的第一个`View`放`StatusView`，然后调用`setLayoutFullScreen`把`StatusView`顶到状态栏下层，接下来就可以控制状态栏颜色了。

布局如下：
```xml
<LinearLayout android:id="@+id/root_layout">

    <LinearLayout>
        <com.yanzhenjie.statusview.StatusView
            <!-- 宽高随便指定 -->
            android:background="?attr/colorPrimary"
            app:fitsView="@id/root_layout"/>

        <android.support.v7.widget.Toolbar/>
    </LinearLayout>

    <.../>
    
</LinearLayout>
```

布局伪代码如上所示，`app:fitsView="@id/root_layout"`这个属性必须要指定，并且只能通过`xml`布局指定，它的作用是适配`Android5.0`以下时避免霸占了状态栏，但是不能修改状态栏颜色的问题。

接着在`Activity#onCreate()`中调用`setLayoutFullScreen`：
```
setContentView(...);
StatusUtils.setLayoutFullScreen(this); // Layout full screen.
```

接下来随意修改状态栏颜色：
```java
mStatusView.setBackgroundColor(Color.BLUE);
```

## 图二效果实现
使用`FrameLayout`作为`root`，底层放需要侵入状态栏的`View`，上层放`StatusView`，然后调用`setLayoutFullScreen`把`StatusView`顶到状态栏下层，接下来就可以控制状态栏的透明度了。

布局如下：
```xml
<FrameLayout android:id="@+id/root_layout">

    <.../>

    <LinearLayout>

        <com.yanzhenjie.statusview.StatusView
            <!-- 宽高随便指定 -->
            android:background="?attr/colorPrimary"
            app:fitsView="@id/root_layout"/>

        <android.support.v7.widget.Toolbar/>

    </LinearLayout>

</FrameLayout>
```

接着在`Activity#onCreate()`中调用`setLayoutFullScreen`：
```
setContentView(...);
StatusUtils.setLayoutFullScreen(this); // Layout full screen.
```

接下来随意修改状态栏的透明度：
```java
mStatusView.getBackground().mutate().setAlpha(0~255); // 透明度值是[0, 255]。
```

## 图三效果实现
图三就比较简单了，因为它是修改状态栏的字体为深色：
```java
StatusUtils.setStatusBarDarkFont(this, true); // Dark font for StatusBar.
```

因为仅仅在在6.0以后可以修改状态栏字体为深色字体，部分小米和魅族在6.0以下也支持修改状态栏字体为深色，所以这里要判断下：
```
if(StatusUtils.setStatusBarDarkFont(this, true)) {
	// 成功设置深色字体后再设置状态栏为白色。
    mStatusView.setBackgroundColor(Color.WHITE);
} else {
	// 如果没成功设置为深色字体，那么状态栏用灰色或者黑色（可以理解为原生色）。
    mStatusView.setBackgroundColor(Color.BLACK);
}
```

# License
```text
Copyright 2017 Yan Zhenjie

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```