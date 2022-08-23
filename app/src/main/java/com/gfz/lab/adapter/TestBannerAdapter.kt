package com.gfz.lab.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.gfz.common.ext.getScreenWidth
import com.gfz.common.ext.setWidth
import com.gfz.common.ext.toDP
import com.gfz.common.ext.toPX
import com.gfz.common.utils.TopLog
import com.gfz.lab.databinding.ItemBannerBinding
import com.gfz.lab.model.bean.BannerBean
import com.gfz.recyclerview.adapter.BaseCenterAdapter
import com.gfz.recyclerview.adapter.BaseRecyclerViewHolder
import com.gfz.recyclerview.adapter.BaseVBRecyclerViewHolder
import com.gfz.recyclerview.decoration.SpaceItemDecoration

/**
 *
 * created by xueya on 2022/8/22
 */
class TestBannerAdapter(context: Context) : BaseCenterAdapter<BannerBean>(context) {
    override fun onCreateViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<BannerBean> {
        return ViewHolder(ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getSnapHelper(): SnapHelper {
        return LeftPagerSnapHelper()
    }

    inner class ViewHolder(binding: ItemBannerBinding): BaseVBRecyclerViewHolder<BannerBean, ItemBannerBinding>(binding){


        override fun onBindViewHolder(data: BannerBean, position: Int) {
            val itemWidth = 300.toPX(context)
            val screenWidth = context.getScreenWidth()
            val space = screenWidth - itemWidth
            val padding = space / 5
            val next = space - padding * 2
            val endPadding = padding + next
            TopLog.e("itemWidth:$itemWidth, screenWidth:$screenWidth, space:$space, padding:$padding, next:$next, endPadding:$endPadding")
            /*binding.vLeft.setWidth(padding)
            if (position == length - 1){
                binding.vRight.setWidth(endPadding)
            } else {
                binding.vRight.setWidth(0)
            }*/
            binding.tvTitle.text = data.title
        }

    }


    inner class LeftPagerSnapHelper : SnapHelper(){
        private val MAX_SCROLL_ON_FLING_DURATION = 100 // ms

        private fun getContainerCenter(targetView: View, helper: OrientationHelper): Int {
            return helper.startAfterPadding + helper.getDecoratedMeasurement(targetView) / 2
        }

        // Orientation helpers are lazily created per LayoutManager.
        private var mVerticalHelper: OrientationHelper? = null
        private var mHorizontalHelper: OrientationHelper? = null

        override fun calculateDistanceToFinalSnap(
            layoutManager: RecyclerView.LayoutManager,
            targetView: View
        ): IntArray? {
            val out = IntArray(2)
            if (layoutManager.canScrollHorizontally()) {
                out[0] = distanceToCenter(
                    layoutManager, targetView,
                    getHorizontalHelper(layoutManager)
                )
            } else {
                out[0] = 0
            }
            if (layoutManager.canScrollVertically()) {
                out[1] = distanceToCenter(
                    layoutManager, targetView,
                    getVerticalHelper(layoutManager)
                )
            } else {
                out[1] = 0
            }
            return out
        }

        override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
            if (layoutManager.canScrollVertically()) {
                return findCenterView(layoutManager, getVerticalHelper(layoutManager))
            } else if (layoutManager.canScrollHorizontally()) {
                return findCenterView(layoutManager, getHorizontalHelper(layoutManager))
            }
            return null
        }

        override fun findTargetSnapPosition(
            layoutManager: RecyclerView.LayoutManager, velocityX: Int,
            velocityY: Int
        ): Int {
            val itemCount = layoutManager.itemCount
            if (itemCount == 0) {
                return RecyclerView.NO_POSITION
            }
            val orientationHelper = getOrientationHelper(layoutManager)
                ?: return RecyclerView.NO_POSITION

            // A child that is exactly in the center is eligible for both before and after
            var closestChildBeforeCenter: View? = null
            var distanceBefore = Int.MIN_VALUE
            var closestChildAfterCenter: View? = null
            var distanceAfter = Int.MAX_VALUE

            // Find the first view before the center, and the first view after the center
            val childCount = layoutManager.childCount
            for (i in 0 until childCount) {
                val child = layoutManager.getChildAt(i) ?: continue
                val distance = distanceToCenter(layoutManager, child, orientationHelper)
                if (distance <= 0 && distance > distanceBefore) {
                    // Child is before the center and closer then the previous best
                    distanceBefore = distance
                    closestChildBeforeCenter = child
                }
                if (distance >= 0 && distance < distanceAfter) {
                    // Child is after the center and closer then the previous best
                    distanceAfter = distance
                    closestChildAfterCenter = child
                }
            }

            // Return the position of the first child from the center, in the direction of the fling
            val forwardDirection = isForwardFling(layoutManager, velocityX, velocityY)
            if (forwardDirection && closestChildAfterCenter != null) {
                return layoutManager.getPosition(closestChildAfterCenter)
            } else if (!forwardDirection && closestChildBeforeCenter != null) {
                return layoutManager.getPosition(closestChildBeforeCenter)
            }

            // There is no child in the direction of the fling. Either it doesn't exist (start/end of
            // the list), or it is not yet attached (very rare case when children are larger then the
            // viewport). Extrapolate from the child that is visible to get the position of the view to
            // snap to.
            val visibleView =
                (if (forwardDirection) closestChildBeforeCenter else closestChildAfterCenter)
                    ?: return RecyclerView.NO_POSITION
            val visiblePosition = layoutManager.getPosition(visibleView)
            val snapToPosition = (visiblePosition
                    + if (isReverseLayout(layoutManager) == forwardDirection) -1 else +1)
            return if (snapToPosition < 0 || snapToPosition >= itemCount) {
                RecyclerView.NO_POSITION
            } else snapToPosition
        }

        private fun isForwardFling(
            layoutManager: RecyclerView.LayoutManager, velocityX: Int,
            velocityY: Int
        ): Boolean {
            return if (layoutManager.canScrollHorizontally()) {
                velocityX > 0
            } else {
                velocityY > 0
            }
        }

        private fun isReverseLayout(layoutManager: RecyclerView.LayoutManager): Boolean {
            val itemCount = layoutManager.itemCount
            if (layoutManager is RecyclerView.SmoothScroller.ScrollVectorProvider) {
                val vectorProvider =
                    layoutManager as RecyclerView.SmoothScroller.ScrollVectorProvider
                val vectorForEnd = vectorProvider.computeScrollVectorForPosition(itemCount - 1)
                if (vectorForEnd != null) {
                    return vectorForEnd.x < 0 || vectorForEnd.y < 0
                }
            }
            return false
        }

        override fun createSnapScroller(layoutManager: RecyclerView.LayoutManager): LinearSmoothScroller? {
            return if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider) {
                null
            } else {
                getScroller(context)
            }
        }

        private fun distanceToCenter(
            layoutManager: RecyclerView.LayoutManager,
            targetView: View, helper: OrientationHelper
        ): Int {
            val childCenter = (helper.getDecoratedStart(targetView)
                    + helper.getDecoratedMeasurement(targetView) / 2)
            val containerCenter = getContainerCenter(targetView, helper)
            return childCenter - containerCenter
        }

        /**
         * Return the child view that is currently closest to the center of this parent.
         *
         * @param layoutManager The [RecyclerView.LayoutManager] associated with the attached
         * [RecyclerView].
         * @param helper The relevant [OrientationHelper] for the attached [RecyclerView].
         *
         * @return the child view that is currently closest to the center of this parent.
         */
        private fun findCenterView(
            layoutManager: RecyclerView.LayoutManager,
            helper: OrientationHelper
        ): View? {
            val childCount = layoutManager.childCount
            if (childCount == 0) {
                return null
            }
            var closestChild: View? = null
            val center = getContainerCenter(layoutManager.getChildAt(0)!!, helper)

            val viewRight = layoutManager.getChildAt(childCount - 1)
            val lastPos = layoutManager.getPosition(viewRight!!)
            //获得position，判断是否是最后一个
            if (lastPos == layoutManager.itemCount - 1) {
                //居左时要判断 最右一个item
                if (helper.getDecoratedEnd(viewRight) == helper.endAfterPadding) {
                    return viewRight
                }
            }

            var absClosest = Int.MAX_VALUE
            for (i in 0 until childCount) {
                val child = layoutManager.getChildAt(i)
                val childCenter = (helper.getDecoratedStart(child)
                        + helper.getDecoratedMeasurement(child) / 2)
                val absDistance = Math.abs(childCenter - center)

                /* if child center is closer than previous closest, set it as closest  */if (absDistance < absClosest) {
                    absClosest = absDistance
                    closestChild = child
                }
            }
            return closestChild
        }

        private fun getOrientationHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper? {
            return if (layoutManager.canScrollVertically()) {
                getVerticalHelper(layoutManager)
            } else if (layoutManager.canScrollHorizontally()) {
                getHorizontalHelper(layoutManager)
            } else {
                null
            }
        }

        private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
            if (mVerticalHelper == null || mVerticalHelper!!.layoutManager !== layoutManager) {
                mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
            }
            return mVerticalHelper!!
        }

        private fun getHorizontalHelper(
            layoutManager: RecyclerView.LayoutManager
        ): OrientationHelper {
            if (mHorizontalHelper == null || mHorizontalHelper!!.layoutManager !== layoutManager) {
                mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
            }
            return mHorizontalHelper!!
        }
    }
}