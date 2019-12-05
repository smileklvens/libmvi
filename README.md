# libmvi 
项目2分支：master采用viewmodule、mvvm利用databing和viewmodule，建议直接使用mvvm分支更灵活，现大致介绍下mvvm分支

该项目是由kotlin、androidx、协程整合，旨在帮助开发者快速搭建项目，敏捷开发采用：
1. 网络请求采用协程+retrofit 
2. 采用viewmodule和databing 
3. 编写基类activity实现了统一处理toolbar、dialog和异常的toast。
4. 一些常用kotlin的扩展
部分代码介绍
```
1、基类activity
    /**
     * DataBinding
     */
    private fun initViewDataBinding() {
        val cls =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)) {
            bindContentView()
        } 
        setToolBar(resources.getString(R.string.app_name), true)
        createViewModel()
    }

    /**
     * 处理content 和 titleBar
     */
    private fun bindContentView() {
        setContentView(R.layout.activity_base)
        mBinding = DataBindingUtil.inflate(layoutInflater, layoutId(), mContainer, false)
        mBinding?.lifecycleOwner = this

        mContainer.addView(mBinding?.root)
    }

    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel =
                ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(tClass) as VM
        }
    }

2、自定义BindingAdapter，app:imageUrl="@{itemData.url}"一行代码实现网络图片的加载   

@BindingAdapter(value = ["imageUri", "placeholder"], requireAll = false)
fun imageUri(imageView: ImageView, imageUri: Uri?, placeholder: Drawable?) {
    when (imageUri) {
        null -> {
            Glide.with(imageView)
                .load(placeholder)
                .into(imageView)
        }
        else -> {
            Glide.with(imageView)
                .load(imageUri)
                .apply(RequestOptions().placeholder(placeholder))
                .into(imageView)
        }
    }
}


  <ImageView
                android:id="@+id/iv_project_list_atticle_ic"
                android:layout_width="60dp"
                android:layout_height="60dp"
                android:layout_marginStart="@dimen/dp_10"
                android:scaleType="centerCrop"
                app:imageUrl="@{itemData.url}"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/tv_project_list_atticle_type" />
                
3、androix viewpage fragment新懒加载

  /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
        }
    }
    
    FragmentPagerAdapter的子类需要传入 FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT

4、协程处理网络数据
 /**
     * 过滤请求结果，其他全抛异常
     * @param block 请求体
     * @param success 成功回调
     * @param errorCall 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param isShowDialog 是否显示加载框
     */
    fun <T> launchOnlyresult(
        block: suspend CoroutineScope.() -> BaseResult<T>,
        success: (T) -> Unit,
        errorCall: (ResponseThrowable) -> Unit = {},
        complete: () -> Unit = {},
        isShowDialog: Boolean = true
    ) {
        if (isShowDialog) defUI.showDialog.call()
        launchUI {
            handleException(
                { withContext(Dispatchers.IO) { block() } },
                { res ->
                    executeResponse(res) { success(it) }
                },
                {
                    errorCall(it)
                },
                {
                    defUI.dismissDialog.call()
                    complete()
                },
                true
            )
        }
    }

5、封装 x5webview ，copy自公司体检宝项目，目前是java实现，未kotlin化

/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe {@link #}
 */
public interface InterWebListener {

    /**
     * 进度条变化时调用
     *
     * @param newProgress 进度0-100
     */
    void startProgress(@IntRange(from = 0, to = 100) int newProgress);

    /**
     * 隐藏进度条
     */
    void hindProgressBar();


    /**
     * 返回标题处理
     */
    void onReceivedTitle(WebView view, String title);

    /**
     * 展示异常页面
     */
    void showErrorView();

6、采用SmartRefreshLayout 统一封装上拉和下拉基类 详见 BaseRefreshMoreFragment

7、编写config.gradle统一管理依赖库

    //androidx
    androidx = [
            appcompat       : "androidx.appcompat:appcompat:${versions['appcompat']}",
            material        : "com.google.android.material:material:${versions['material']}",
            core_ktx        : "androidx.core:core-ktx:1.1.0",
            constraint_layou: "androidx.constraintlayout:constraintlayout:${versions["constraint"]}",
            //viewModel and LiveData
            extensions      : "androidx.lifecycle:lifecycle-extensions:${versions['extensions']}",
            viewmodel_ktx   : "androidx.lifecycle:lifecycle-viewmodel-ktx:${versions['extensions']}",
            annotation      : "androidx.annotation:annotation:1.1.0"
    ]
    
```

----------

# 用法：

```
1、统一 继承子类 activity或者fragment ，常规写法不需要viewmodule和databing，用法详见 MeFragment

2、如果界面逻辑复杂需要自定义ViewModel：BaseViewModel()，
详见 LoginFragment : BaseRefreshFragment<LoginViewModule,FragmentLoginBinding>() 


```
具体详见代码
# 感谢 

[SmartTabLayout](https://github.com/smileklvens/SmartTabLayout)

[BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
此项目参考了众多优秀的开源MVVM项目的优秀思想，整合了很多别人的代码，在此一并表示感谢。










