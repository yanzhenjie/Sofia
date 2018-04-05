# Sofia
Android状态栏与导航栏一体化项目，实现沉浸式效果，状态栏和导航栏均支持设置颜色、渐变色、图片、透明度、内容入侵和状态栏深色字体；兼容竖屏、横屏，当屏幕旋转时会自动适配。

Sofia一词源自于希腊语∑οφία，含义为智慧，译为中文应该可以叫做*索菲亚*，这也是我赋予这个项目的愿望。

**QQ技术交流群：[46505645](https://jq.qq.com/?_wv=1027&k=5nJm5up)**

## 截图
<image src="./image/1.gif" width="270">  <image src="./image/2.gif"  width="270">  <image src="./image/3.gif"  width="270">  
<image src="./image/4.gif" width="270">  <image src="./image/5.gif"  width="270">  <image src="./image/6.gif"  width="270">  

* 图一：状态栏和导航栏用图片作为背景。
* 图二：状态栏和导航栏随着Content的滑动逐渐变色。
* 图三：内容侵入状态栏，比如产品详情页。
* 图四：状态栏底色白色，字体深色（大概是灰黑色）。
* 图五：和DrawerLayout结合使用。
* 图六：和Fragment结合使用。

## 下载
在Gradle中添加依赖：
```groovy
implementation 'com.yanzhenjie:sofia:1.0.5'
```

开发者也可以使用[其它版本](https://github.com/yanzhenjie/Sofia/releases)。

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
Bar fitsStatusBarView(int viewId);

// 让某一个View考虑状态栏的高度，显示在适当的位置，接受View。
Bar fitsStatusBarView(View view);

// 让某一个View考虑导航栏的高度，显示在适当的位置，接受ViewId。
Bar fitsNavigationBarView(View view);

// 让某一个View考虑导航栏的高度，显示在适当的位置，接受View。
Bar fitsNavigationBarView(View view);
```

`fitsStatusBarView()`一般用在产品详情页，假设需求如`图三`所示。在内容入侵状态栏后，我们的布局整体上移到状态栏，那么`Toolbar`的一部分也会显示在状态栏下方（层级），这个时候就需要`Toolbar`考虑系统状态栏的高度显示在适当的位置了；`fitsNavigationBarView()`同理，只是显示在屏幕的最底部（竖屏）或者最右侧（横屏）。

**注意**：`Sofia.with(Activity)`调用后会返回一个`Bar`接口实例，开发者可以连续调用某几个方法。在页面滑动时如果需要再次改变SystemBar的颜色，那么开发者可以保存这个`Bar`实例为`Activity`的成员变量，也可以不保存，在重复改变时再次调用`Sofia.with(Activity)`，此时还是会返回上次返回给开发者的`Bar`接口实例。

## License
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