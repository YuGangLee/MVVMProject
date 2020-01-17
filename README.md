# MVVMProject(不严谨以及过时的设计)

### 一个简单的MVVM框架实现

使用RxJava+RxLifecycle实现了一个默认的生命周期控制工具  
使用Databinding作为数据绑定框架  
使用<a href="https://github.com/JeremyLiao/LiveEventBus">LiveEventBus</a>作为消息总线进行通讯

提供了支持懒加载的Fragment：BaseLazyFragment   
提供了支持Databinding的RecycleView.BaseAdapter & ViewHolder   
提供了支持空内容布局的RecycleView (空布局大小取决于自身定义，与RecyclerView无关)
