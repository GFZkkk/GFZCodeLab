# GFZCodeLib

## 前言

这个项目初衷是对kotlin的初步尝试，现在逐步发展为对新语言，新想法，新架构的尝试。由最初敲定的mvp，到现在改为mvvm，此项目将不断接受改变。

### 记录

#### 2021-09-09 开始搭建navigation

* 决定采用navigation + livedata + viewmode作为基础架构。

+ 使用单activity多fragment方式搭建项目。
+ 不再使用rxjava作为线程控制工具。
+ 不再使用eventbus作为通信工具。

#### 2021-09-14 构建首页

+ 使用viewpager2 + recyclerview作为首页结构。
+ BaseRecyclerviewViewHolder 适配 viewbinding。
+ 添加开屏。
+ 添加基础头部。
+ 修改状态栏主题颜色。