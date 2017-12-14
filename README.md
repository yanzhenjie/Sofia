SystemBar一体化，状态栏和导航栏均支持设置颜色、渐变色、图片、透明度、内容入侵。状态栏支持设置深色字体，以上特性兼容国产魅族、小米手机（包括7.0及以上）和其它标准模式的手机。

**QQ技术交流群：[547839514](https://jq.qq.com/?_wv=1027&k=4Ev0ksp)**

# 前言
从`1.0.4`版本开始本项目改名为Sofia，由于原来的名字不太适合链式调用，于是我顺便换了一个相对优雅的名字：Sofia，译为中文大概可叫做：索菲亚。Sofia一词源自于希腊语∑οφία，含义为智慧，这也是我赋予这个项目的愿望。

# 截图
<image src="./image/1.gif" width="270">  <image src="./image/2.gif"  width="270">  <image src="./image/3.gif"  width="270">  
<image src="./image/4.gif" width="270">  <image src="./image/5.gif"  width="270">  <image src="./image/6.gif"  width="270">  

图一：状态栏和导航栏用图片作为背景。  
图二：状态栏和导航栏随着Content的滑动逐渐变色。  
图三：内容侵入状态栏，比如产品详情页。  
图四：状态栏底色白色，字体深色（大概是灰黑色）。  
图五：和DrawerLayout结合使用。  
图六：和Fragment结合使用。  

# 使用
## 依赖
* Gradle
```groovy
compile 'com.yanzhenjie:sofia:1.0.4'
```

* Maven
```xml
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>sofia</artifactId>
  <version>1.0.3</version>
  <type>pom</type>
</dependency>
```

## Api
这里例如所有的Api，调用时结合具体的场景调用某几个方法即可，也可以一个都不调用，具体请参考Sample。  
通用的用法是：
```java
Sofia.with(Activity)
    ...;
```

调用`with`方法后返回一个`Bar`接口，可以链式连续调用所有方法，方法列表如下：
```java
// 状态栏深色字体。
Bar statusBarDarkFont();

// 状态栏浅色字体。
Bar statusBarLightFont();

// 状态栏背景色。
Bar statusBarBackground(int statusBarColor);

// 状态栏背景Drawable。
Bar statusBarBackground(Drawable drawable);

// 状态栏背景透明度。
Bar statusBarBackgroundAlpha(int alpha);

// 导航栏背景色。
Bar navigationBarBackground(int navigationBarColor);

// 导航栏背景Drawable。
Bar navigationBarBackground(Drawable drawable);

// 导航栏背景透明度。
Bar navigationBarBackgroundAlpha(int alpha);

// 内容入侵状态栏。
Bar invasionStatusBar();

// 内容入侵导航栏。
Bar invasionNavigationBar();

// 让某一个View考虑状态栏的高度，显示在适当的位置，接受ViewId。
Bar fitsSystemWindowView(int viewId);

// 让某一个View考虑状态栏的高度，显示在适当的位置，接受View。
Bar fitsSystemWindowView(View view);
```

**`fitsSystemWindowView()`**一般用在产品详情页，假设需求如`图三`所示。在内容入侵状态栏后，我们的布局整体上移到状态栏，那么`Toolbar`的一部分也会显示在状态栏下方（层级），这个时候就需要`Toolbar`考虑系统状态栏的高度显示在适当的位置了。当开发者设置内容侵入导航栏时，如果页面最下方有按钮，且想让按钮显示在导航栏之上（平级），造成的效果是导航栏一部分页面，导航栏上面有按钮，这样页面就会很丑，所以不会为这种情况自动适配，如果有这样的需求，开发者在按钮布局下方添加一个`NavigationView`即可解决。

**注意**：`Sofia.with(Activity)`调用后会返回一个`Bar`接口实例，开发者可以连续调用某几个方法。在页面滑动时如果需要再次改变SystemBar的颜色，那么开发者可以保存这个`Bar`实例为`Activity`的成员变量，也可以不保存，在重复改变时再次调用`Sofia.with(Activity)`，此时还是会返回上次返回给开发者的`Bar`接口实例。

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