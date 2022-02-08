package com.game.kotlin.sample.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.game.kotlin.sample.R
import com.game.kotlin.sample.adapter.TodoAdapter
import com.game.kotlin.sample.app.App
import com.game.kotlin.sample.base.BaseMvpListFragment
import com.game.kotlin.sample.constant.Constant
import com.game.kotlin.sample.event.RefreshTodoEvent
import com.game.kotlin.sample.event.TodoEvent
import com.game.kotlin.sample.event.TodoTypeEvent
import com.game.kotlin.sample.ext.setNewOrAddData
import com.game.kotlin.sample.ext.showSnackMsg
import com.game.kotlin.sample.mvp.contract.TodoContract
import com.game.kotlin.sample.mvp.model.bean.TodoDataBean
import com.game.kotlin.sample.mvp.model.bean.TodoResponseBody
import com.game.kotlin.sample.mvp.presenter.TodoPresenter
import com.game.kotlin.sample.ui.activity.CommonActivity
import com.game.kotlin.sample.utils.DialogUtil
import com.game.kotlin.sample.utils.NetWorkUtil
import com.game.kotlin.sample.widget.SwipeItemLayout
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @description:
 * @author:  xubp
 * @date :   2022/2/8 10:31
 */
class TodoFragment : BaseMvpListFragment<TodoContract.View, TodoContract.Presenter>(), TodoContract.View {

    companion object {
        fun getInstance(type: Int): TodoFragment {
            val fragment = TodoFragment()
            val bundle = Bundle()
            bundle.putInt(Constant.TODO_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mType: Int = 0

    /**
     * 是否是已完成 false->待办 true->已完成
     */
    private var bDone: Boolean = false

    private val mAdapter: TodoAdapter by lazy {
        TodoAdapter()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_todo

    override fun createPresenter(): TodoContract.Presenter = TodoPresenter()

    override fun initView(view: View) {
        super.initView(view)

        mType = arguments?.getInt(Constant.TODO_TYPE) ?: 0

        recyclerView.run {
            adapter = mAdapter
            addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(activity))
        }

        mAdapter.run {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as TodoDataBean
                itemChildClick(item, view, position)
            }
            loadMoreModule.setOnLoadMoreListener(onRequestLoadMoreListener)
        }
    }

    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        pageNum = 0
        if (bDone) {
            mPresenter?.getDoneList(1, mType)
        } else {
            mPresenter?.getNoTodoList(1, mType)
        }
    }

    override fun onRefreshList() {
        lazyLoad()
    }

    override fun onLoadMoreList() {
        if (bDone) {
            mPresenter?.getDoneList(pageNum + 1, mType)
        } else {
            mPresenter?.getNoTodoList(pageNum + 1, mType)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doTodoTypeEvent(event: TodoTypeEvent) {
        mType = event.type
        bDone = false
        lazyLoad()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doTodoEvent(event: TodoEvent) {
        if (mType == event.curIndex) {
            when (event.type) {
                Constant.TODO_ADD -> {
                    Intent(activity, CommonActivity::class.java).run {
                        putExtra(Constant.TYPE_KEY, Constant.Type.ADD_TODO_TYPE_KEY)
                        putExtra(Constant.TODO_TYPE, mType)
                        startActivity(this)
                    }
                }
                Constant.TODO_NO -> {
                    bDone = false
                    lazyLoad()
                }
                Constant.TODO_DONE -> {
                    bDone = true
                    lazyLoad()
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doRefresh(event: RefreshTodoEvent) {
        if (event.isRefresh) {
            if (mType == event.type) {
                lazyLoad()
            }
        }
    }

    override fun showNoTodoList(todoResponseBody: TodoResponseBody) {
        val list = mutableListOf<TodoDataBean>()
        var bHeader = true
        todoResponseBody.datas.forEach { todoBean ->
            bHeader = true
            for (i in list.indices) {
                if (todoBean.dateStr == list[i].headerName) {
                    bHeader = false
                    break
                }
            }
            if (bHeader) {
                list.add(TodoDataBean(headerName = todoBean.dateStr))
            }
            list.add(TodoDataBean(todoBean = todoBean))
        }
        mAdapter.setNewOrAddData(pageNum == 0, list)
        if (mAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun showDeleteSuccess(success: Boolean) {
        if (success) {
            showMsg(resources.getString(R.string.delete_success))
        }
    }

    override fun showUpdateSuccess(success: Boolean) {
        if (success) {
            showMsg(resources.getString(R.string.completed))
        }
    }

    /**
     * Item Child Click
     * @param item TodoDataBean
     * @param view View
     * @param position Int
     */
    private fun itemChildClick(item: TodoDataBean, view: View, position: Int) {
        val data = item.todoBean ?: return
        when (view.id) {
            R.id.btn_delete -> {
                if (!NetWorkUtil.isNetworkAvailable(App.context)) {
                    showSnackMsg(resources.getString(R.string.no_network))
                    return
                }
                activity?.let {
                    DialogUtil.getConfirmDialog(it, resources.getString(R.string.confirm_delete)) { _, _ ->
                        mPresenter?.deleteTodoById(data.id)
                        mAdapter.removeAt(position)
                    }.show()
                }
            }
            R.id.btn_done -> {
                if (!NetWorkUtil.isNetworkAvailable(App.context)) {
                    showSnackMsg(resources.getString(R.string.no_network))
                    return
                }
                if (bDone) {
                    mPresenter?.updateTodoById(data.id, 0)
                } else {
                    mPresenter?.updateTodoById(data.id, 1)
                }
                mAdapter.removeAt(position)
            }
            R.id.item_todo_content -> {
                if (bDone) {
                    Intent(activity, CommonActivity::class.java).run {
                        putExtra(Constant.TYPE_KEY, Constant.Type.SEE_TODO_TYPE_KEY)
                        putExtra(Constant.TODO_BEAN, data)
                        putExtra(Constant.TODO_TYPE, mType)
                        startActivity(this)
                    }
                } else {
                    Intent(activity, CommonActivity::class.java).run {
                        putExtra(Constant.TYPE_KEY, Constant.Type.EDIT_TODO_TYPE_KEY)
                        putExtra(Constant.TODO_BEAN, data)
                        putExtra(Constant.TODO_TYPE, mType)
                        startActivity(this)
                    }
                }
            }
        }
    }
}