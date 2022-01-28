package com.gfz.common.task

import android.util.SparseArray
import android.util.SparseIntArray
import com.gfz.common.ext.launchSafe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * Created by xueya on 2022/1/28
 */
class JobManager(private val helper: JobHelper) {
    private val jobArray: SparseArray<Job> by lazy {
        SparseArray()
    }

    private val loadingArray: SparseIntArray by lazy {
        SparseIntArray()
    }

    fun startJob(tag: Int = 0, loading: Boolean = false, block: suspend () -> Unit) {
        val content = JobItem(tag, loading, block)
        startJob(content)
    }

    fun startJob(item: JobItem) {
        with(item){
            if (item.loading){
                showLoading(item.tag)
            }
            stopJob(tag)
            val job = getJob(this)
            jobArray.append(tag, job)
        }

    }

    fun stopJob(tag: Int = 0){
        jobArray[tag]?.let {
            it.cancel()
            jobArray.remove(tag)
        }
    }

    fun showLoading(tag: Int){
        val num = loadingArray[tag, 0]
        loadingArray.append(tag, num + 1)
        checkLoadingStatus()
    }

    fun hideLoading(tag: Int){
        val num = loadingArray[tag, 0]
        if (num - 1 <= 0){
            loadingArray.delete(tag)
        }else{
            loadingArray.append(tag, num)
        }
        checkLoadingStatus()
    }

    private fun checkLoadingStatus(){
        helper.changeLoadingStatus(loadingArray.size() > 0)
    }

    private fun getJob(item: JobItem): Job{
        return helper.getScope().launchSafe(onComplete = {
            if (item.loading){
                hideLoading(item.tag)
            }
        }) {
            item.block()
        }
    }
}

interface JobHelper{
    fun getScope(): CoroutineScope
    fun startJob(tag: Int = 0, loading: Boolean = false, block: suspend () -> Unit)
    fun stopJob(tag: Int = 0)
    fun showLoading(tag: Int)
    fun hideLoading(tag: Int)
    fun changeLoadingStatus(show: Boolean)
}

data class JobItem(
    val tag: Int = 0,
    val loading: Boolean = false,
    val block: suspend () -> Unit
)