package com.gfz.lab.ui.fragment.home

import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.gfz.common.utils.ScreenUtil
import com.gfz.common.utils.TopLog
import com.gfz.lab.base.BaseFragment
import com.gfz.lab.base.BaseVBFragment
import com.gfz.lab.databinding.FragmentMarqueeBinding
import java.lang.ref.WeakReference

/**
 *
 * created by xueya on 2022/2/21
 */
class TestMarqueeFragment(): BaseVBFragment<FragmentMarqueeBinding>(){
    override fun initView() {

        with(binding){

            tvMarquee.isSelected = true

            val limit = (ScreenUtil.getScreenWidth(tvMarquee.context) / tvMarquee.textSize).toInt()
            tvMarquee.text = getTextContent(limit)
            tvMarquee.postTask {
                TopLog.e(width)
            }
        }
    }

    fun getTextContent(limit: Int): CharSequence{
        val list = listOf(
            "第一条第一条第一条第一条",
            "第二条第二条",
            "第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条第三条",
            "第四条",
        )
        val text: StringBuilder = StringBuilder()

        repeat(limit){
            text.append("   ")
        }
        TopLog.e(text.length)
        list.forEach {
            text.append(it)
            text.append("                              ")
        }
        return text
    }

    fun View.postTask(delay: Long = 0, task: View.() -> Unit){
        postDelayed(object : ViewRunnable<View>(WeakReference(this)){
            override fun run(view: View) {
                task(view)
            }
        }, delay)
    }

    /*fun TextView.postTask(delay: Long = 0, task: TextView.() -> Unit){
        postDelayed(object : ViewRunnable<TextView>(WeakReference(this)){
            override fun run(view: TextView) {
                task(view)
            }
        }, delay)
    }*/

    abstract class ViewRunnable<T: View>(private val view: WeakReference<T>) : Runnable{
        override fun run() {
            view.get()?.let {
                run(it)
            }
        }

        abstract fun run(view: T)
    }
}