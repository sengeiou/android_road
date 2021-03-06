package com.mmc.lamandys.liba_datapick.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mmc.lamandys.liba_datapick.Constants
import com.mmc.lamandys.liba_datapick.ENGINE_ID
import com.mmc.lamandys.liba_datapick.R
import com.mmc.lamandys.liba_datapick.activity.ui.login.LoginActivity
import com.mmc.lamandys.liba_datapick.adapter.HomePageAdapter
import com.mmc.lamandys.liba_datapick.base.BaseActivity
import com.mmc.lamandys.liba_datapick.bean.SecondChildBean
import com.mmc.lamandys.liba_datapick.util.HookUtils
import com.mmc.lamandys.liba_datapick.util.StatusBarUtils
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.plugin.common.MethodChannel
import io.flutter.view.FlutterMain
import kotlinx.android.synthetic.main.activity_home_page_layout_v2.*

class HomePageActivityV2 : BaseActivity() {

    init {
        val engine = FlutterEngineCache.getInstance().get(ENGINE_ID)

        val channel = MethodChannel(engine?.dartExecutor, Constants.Method_channel)

        channel.setMethodCallHandler { call, _ ->
            when (call.method) {
                Constants.method_finish -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
        }
        val secondChildBean = SecondChildBean("", "")
        secondChildBean.name
    }

    private lateinit var methodChannel: MethodChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.transparencyBar(this)
        setContentView(R.layout.activity_home_page_layout_v2)
        //强烈建议放到Application里初始化,初始化一次即可,放这里只是举个例子
        FlutterMain.startInitialization(this)
        initView()
    }


    private fun initView() {
        val params = GridLayoutManager(this, 3)
        params.orientation = LinearLayoutManager.VERTICAL
        with(rv_home_page) {
            layoutManager = params
            adapter = HomePageAdapter(initData())
        }

        val mAdapter = HomeV2BannerAdapter()
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(tv_home_page_banner)
        with(tv_home_page_banner) {
            layoutManager = manager
            adapter = mAdapter
        }

        //        testBtn = findViewById(R.id.videoBtn);
//        testBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//////                dialog.setTitle("我是弹窗");
//////                dialog.setMessage("点击弹出尝试一下");
//////                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//////                    @Override
//////                    public void onClick(DialogInterface dialogInterface, int i) {
//////                        dialogInterface.dismiss();
//////                    }
//////                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//////                    @Override
//////                    public void onClick(DialogInterface dialogInterface, int i) {
//////                        System.out.println("this is a test ");
//////                        dialogInterface.dismiss();
//////                    }
//////                }).show();
//
//                goScheme("qn413e80ac2897://");
//            }
//        });
        val looper = Looper.myLooper()

        Thread(Runnable {
            handler = Handler(looper, Handler.Callback { msg: Message? ->
                println("我收到了handler消息")
                false
            })
            handler!!.sendEmptyMessage(0)
        }).start()


    }

    var handler: Handler? = null


    private fun initData(): List<String> {
        val list = mutableListOf<String>()
        list.add("ViewPager2")
        list.add("RecyclerView")
        list.add("ToolBar")
        list.add("Radio")
        list.add("Drawer")
        list.add("Animation")
        list.add("自定义Behavior")
        list.add("调用flutter页面")
        list.add("调用指定flutter页面")
        list.add("Flutter与原生交互")
        list.add("咸鱼FlutterBoost")
        list.add("LiveData+ViewModel")

        return list
    }

    companion object {
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }

}


class HomeV2BannerAdapter : RecyclerView.Adapter<HomeV2BannerViewHolder>() {

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: HomeV2BannerViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeV2BannerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_viewpager2_item, parent, false)
        return HomeV2BannerViewHolder(itemView)
    }
}

const val json = "{\"first\":\"\"lili\",\"last\":\"liyang\"}"

public data class User(var first: String, var last: String)

inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, T::class.java)

class HomeV2BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var mIv: AppCompatImageView? = null
    private var colors: Array<Int>

    init {
        mIv = itemView.findViewById(R.id.iv_item)

        colors = arrayOf(R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_4)

        mIv?.let { it ->
//            // 如果lambda表达式是函数调用的最后一个实参，就可以把它挪到小括号外面
//            it.setOnClickListener() { view: View ->
//                view.visibility = VISIBLE
//            }
//            //当lambda是函数的唯一实参，就可以去掉空的小括号
//            it.setOnClickListener { view: View ->
//                view.visibility = VISIBLE
//            }
//            //如果lambda的参数的类型可以被编译器推导出来，则可以省略
//            it.setOnClickListener { view ->
//                view.visibility = VISIBLE
//            }
//            it.setOnClickListener {
//                it.visibility = VISIBLE
//            }
            it.setOnClickListener {
                itemView.context.startActivity(Intent(itemView.context, MainActivity::class.java))
                try {
                    HookUtils.hookOnclickListener(it)
                    HookUtils.reflectNewInstance("")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
//        var gson = Gson().fromJson<User>(json)
    }

    fun bindData(position: Int) {
        mIv?.setBackgroundResource(colors[position])
    }
}