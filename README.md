# MVVMProject

### 一个简单的MVVM框架实现

实现思路模式基本遵循<a href="https://developer.android.com/jetpack/docs/guide">Google官方应用架构指南</a>进行  
默认使用RxJava+RxLifecycle进行观察者生命周期控制  
集成Databinding & <a href="https://github.com/JeremyLiao/LiveEventBus">LiveEventBus</a>

提供了支持懒加载的Fragment：LazyFragment   
提供了支持Databinding的RecycleView.BaseAdapter & ViewHolder   
提供了支持空布局的RecycleView   

本项目目的在于搭建一个基本骨架，为保证灵活性没有使用太多的三方库

    module中的相关依赖
    api "com.trello.rxlifecycle2:rxlifecycle:2.2.2"
    api "com.trello.rxlifecycle2:rxlifecycle-android:2.2.2"
    api "com.trello.rxlifecycle2:rxlifecycle-components:2.2.2"
    api "io.reactivex.rxjava2:rxjava:2.2.4"
    api "io.reactivex.rxjava2:rxandroid:2.1.0"
    api "android.arch.lifecycle:extensions:1.1.1"
    api "android.arch.lifecycle:common-java8:1.1.1"
