package com.yoyiyi.soleil.base

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.yoyiyi.soleil.R
import com.yoyiyi.soleil.utils.AppUtils
import com.yoyiyi.soleil.utils.ToastUtils

/**
 * @author zzq  作者 E-mail:   soleilyoyiyi@gmail.com
 * *
 * @date 创建时间：2017/5/15 11:13
 * * 描述:基础刷新的Fragment
 */

abstract class BaseRefreshFragment<T : BaseContract.BasePresenter<*>, K> : BaseFragment<T>(), SwipeRefreshLayout.OnRefreshListener {
    protected var mRecycler: RecyclerView? = null
    protected var mRefresh: SwipeRefreshLayout? = null
    protected var mIsRefreshing = false
    protected var mList = arrayListOf<K>()

    override fun initRefreshLayout() {
        mRefresh.let {
            mRefresh?.setColorSchemeResources(R.color.colorPrimary)
            mRecycler?.post {
                mRefresh?.isRefreshing = true
                lazyLoadData()
            }
            mRefresh?.setOnRefreshListener(this)
        }
    }

    override fun onRefresh() {
        clearData()
        lazyLoadData()
    }


    override fun clearData() {
        mIsRefreshing = true

    }

    override fun finishCreateView(state: Bundle?) {
        mRefresh = mRootView?.findViewById(R.id.refresh) as SwipeRefreshLayout?
        mRecycler = mRootView?.findViewById(R.id.recycler) as RecyclerView?
        mIsPrepared = true
        lazyLoad()
    }

    override fun lazyLoad() {
        if (!mIsPrepared || !mIsVisible) return
        initRefreshLayout()
        initRecyclerView()
        mRefresh ?: lazyLoadData()
        mIsPrepared = false
    }

    override fun complete() {
        super.complete()
        AppUtils.runOnUIDelayed({
            mRefresh.let { mRefresh?.isRefreshing = false }
        }, 650)
        if (mIsRefreshing) {
            mList.clear()
            clear()
            ToastUtils.showSingleLongToast("刷新成功")
        }
        mIsRefreshing = false
    }

    protected open fun clear() {

    }


    override fun initWidget() {

    }
}