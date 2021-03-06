package com.mmc.lamandys.liba_datapick.main.ui.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.mmc.lamandys.liba_datapick.R
import com.mmc.lamandys.liba_datapick.activity.MainActivity
import com.mmc.lamandys.liba_datapick.adapter.HomePageAdapter
import com.mmc.lamandys.liba_datapick.util.HookUtils
import kotlinx.android.synthetic.main.activity_home_page_layout_v2.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        val params = GridLayoutManager(activity, 3)
        params.orientation = LinearLayoutManager.VERTICAL
        with(rv_home_page) {
            layoutManager = params
            adapter = HomePageAdapter(initData())
        }

        val mAdapter = HomeV2BannerAdapter()
        val manager = LinearLayoutManager(activity)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(tv_home_page_banner)
        with(tv_home_page_banner) {
            layoutManager = manager
            adapter = mAdapter
        }
    }

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

class HomeV2BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var mIv: AppCompatImageView? = null
    private var colors: Array<Int>

    init {
        mIv = itemView.findViewById(R.id.iv_item)

        colors = arrayOf(R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_4)

        mIv?.let { it ->
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
    }

    fun bindData(position: Int) {
        mIv?.setBackgroundResource(colors[position])
    }
}
