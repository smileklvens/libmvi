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
# android 组件化

随着移动端快产品速迭代，多人合作已成为一种标杆，如果是传统的项目都放在一个module中代码耦合严重，存在各种各样的诟病，所以组件化越来越盛行。下面我们一步步揭开组件化的神秘面纱。

### 首先我们看下整体结构

![组件化](C:\Users\Administrator\Desktop\组件化.png)

说明：app是壳工程，下面是业务模块分别有login，my等模块，业务模块都依赖libmvi基础库，

基础库里面有业务组件都会用到的功能比如:基础类、glide、retrofit等，有些组价用到，有些用不到的工具模块，单独抽成工具模块比如libutil，libshare，可被组件按需依赖。

### 实施组件化

#### 模块划分

按项目业务抽取module和工具模块，为了方便管理，抽取config.gradle做版本统一管理，util.gradle做工具模块依赖......

#### 单独编译

在gradle.properties中声明isBuildModule，isBuildModule 为 true 时可以使每个组件独立运行, false 则可以将所有组件集成到宿主 App 中，编写业务module.gradle，每个业务组件必须依赖这个。

单独编译需要新建alone文件夹放入AndroidManifest.xml标识启动入口，关于源码控制和isBuildModule 切换详见module.gradle配置

```
if (isBuildModule.toBoolean()) {
    apply plugin: 'com.android.application'
    configSigning project
} else {
    apply plugin: 'com.android.library'
}
 sourceSets {
        main {
            if (isBuildModule.toBoolean()) {
                //独立运行
                manifest.srcFile 'src/main/alone/AndroidManifest.xml'
            } else {
                //合并到宿主
                manifest.srcFile 'src/main/AndroidManifest.xml'
                resources {
                    //正式版本时，排除alone文件夹下所有调试文件
                    exclude 'src/main/alone/*'
                }
            }
        }
    }
```



#### Application业务分发

以前的项目第三方的初始化全在 项目的application中做的，现在组件化需要单一职责，不同的三方库的初始化放到不同的业务模块，具体步骤如下：

1.新建module模块：providerservice （作为组件通信和application业务分发）,业务模块需依赖它进行通信

2.新建interface IApp ，代理生命App周期

```
interface IApp {

    fun attachBaseContext(base: Context)

    fun onCreate(base: Context)

    fun onTerminate(base: Context)
}
```

3.不同的业务模块实现IApp，并在对应AndroidManifest.xml注册

```
class LoginApp: IApp {
    override fun attachBaseContext(base: Context) {
    }

    override fun onCreate(base: Context) {
	//初始化一键登录，第三方库啥的
    }

    override fun onTerminate(base: Context) {
    //释放一些资源
    }
}

<meta-data
                android:name="com.ikang.loginmodule.LoginApp"
                android:value="ConfigAppLife" />
```

4.编写CommonManifestParser解析所有的ConfigAppLife到集合中，再编写代理类AppDelegate，代理所有模块的生命周期函数调用，最后在BaseApp相应调用。



```
  // CommonManifestParser
  val appInfo =context.packageManager.getApplicationInfo( context.packageName, PackageManager.GET_META_DATA)
            if (appInfo.metaData != null) {
                for (key in appInfo.metaData.keySet()) {
                    if (MODULE_VALUE == appInfo.metaData[key]) {
                        modules.add(parseModule(key))
                    }
                }
            }
           
   //  BaseApp      
 override fun onTerminate() {
        super.onTerminate()
        mAppDelegate.onTerminate(this)
 }
```



#### 业务组件通信

①采用原始方式

1、providerservice模块中在声明接口IService，然后在声明登陆模块暴露的功能有哪些

```
interface ILoginService : IService {
   val accountId: String
}
```

2、在登陆模块loginmodule实现ILoginService

```
class LoginServiceImpl : ILoginService {
    override val accountId: String = "张三"
}
```

3、在loginApp传入实现

```
class LoginApp: IApp {

    override fun onCreate(base: Context) {
        //传统方式通信
	     LoginServiceControl.accountService = LoginServiceImpl()
    }
       ......
```

4.现在即可在mymodule模块MeFragment中获取登陆模块的用户Id

```
LoginServiceControl.accountService.accountId
```

②采用arouter通信跟上面的类似，只不过arouter是编译期apt自动生成代码，放入到路由表map中的，然后扫描所有的dex中的指定报名的IProvider实现的

1、providerservice模块中在声明接口ILoginService ，然后在声明登陆模块暴露的功能有哪些

```
interface ILoginService :IProvider {
    val accountId: String
}
```

- 2.在登陆模块loginmodule实现ILoginService

```
@Route(path = ProviderPath.Provider.LOGIN)
class LoginServiceRouter : ILoginService {
    override val accountId: String = "张三"
	.......
}
```

3.现在即可在mymodule模块MeFragment中获取登陆模块的用户Id

```
 @Autowired
 @JvmField var loginService: ILoginService? = null
```

③基于事件总线

使用 LoacaBroadcastManage或者RxBus进行通信

#### 其他约定

- [ ] 实体类放在本身的 module 中是无法传递的，可以下沉，放到业务模块下边的providerservice中，或者在抽取一个datamodule类

- [ ] 权限管理我们将 normal 级别的权限申请都放到 Base module 中，然后在各个 module 中分别申请 dangerous 的权限 。 这样分配的好处在于当添加或移除单一模块时，隐私权限申请也 会跟随移除 ，能做到最大程度的权限解祸 。不要将权限全部转交到每个 module 中，包括普通权限的声明 ，因为这样会增加 AndroidManifest的合并检测的耗时’。

- [ ] 组件化资源冲突

  ```
  //1、AndroidMainfest 冲突问题
  AndroidMainfest 中引用了 Application 的 app:nam巳 属性 ，当出现冲 突 时， 需要使 用 tools:replace=”android:name”来声明 Application 是可被替换 的 。
  //2、依赖jar包冲突
  当包冲突出现时，可以先检查依赖报告，使用命令 gradledependencies查看依赖目录树
  //3、防止资源重复，resourcePrefix 这个值只能 限定 XML 中的资源，井不能限定图片资源，所有图片资源仍然需要手动去修改资源名 。
  android {
  resourcePrefix "纽件名”
  }
  //组件化混淆,每个module独有的引用库混淆放到各自 的 proguard-rule.pro 中 。
   defaultConfig {
       ......
       consumerProguardFiles 'proguard-rules.pro'
      }
  ```

  ### 代码部署

  多模块可以采用 git submodule\git subtree，或者上传到 公司私服maven上统一管理。









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










