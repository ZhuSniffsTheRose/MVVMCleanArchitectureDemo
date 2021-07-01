package com.zhu.mvvmdemo.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * @author Zhu
 * @date 2021/7/1 16:05
 * @desc
 */


fun View.setNewHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}

fun View.setNewWidth(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = value
        layoutParams = lp
    }
}

fun View.resize(width: Int, height: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = width
        lp.height = height
        layoutParams = lp
    }
}

//////////////////////////////////////////////////
//                  Margin                      //
//////////////////////////////////////////////////
/**
 * 设置view的margin值
 * code Example :
 *      view.margin {
 *          bottomMargin = 20
 *          topMargin = 20
 *          ...
 *      }
 *
 * @receiver View
 * @param margin ViewGroup.MarginLayoutParams.() -> Unit
 */
inline fun View.margin(margin: ViewGroup.MarginLayoutParams.() -> Unit) {
    margin(this.layoutParams as ViewGroup.MarginLayoutParams)
}

inline var View.bottomMargin: Int
    get():Int {
        return (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin
    }
    set(value) {
        (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = value
    }

inline var View.topMargin: Int
    get():Int {
        return (layoutParams as ViewGroup.MarginLayoutParams).topMargin
    }
    set(value) {
        (layoutParams as ViewGroup.MarginLayoutParams).topMargin = value
    }

inline var View.rightMargin: Int
    get():Int {
        return (layoutParams as ViewGroup.MarginLayoutParams).rightMargin
    }
    set(value) {
        (layoutParams as ViewGroup.MarginLayoutParams).rightMargin = value
    }

inline var View.leftMargin: Int
    get():Int {
        return (layoutParams as ViewGroup.MarginLayoutParams).leftMargin
    }
    set(value) {
        (layoutParams as ViewGroup.MarginLayoutParams).leftMargin = value
    }

fun View.setMargin(left: Int, top: Int, right: Int, bottom: Int) {
    (layoutParams as ViewGroup.MarginLayoutParams).setMargins(left, top, right, bottom)
}

//////////////////////////////////////////////////
//                  Padding                     //
//////////////////////////////////////////////////
inline var View.leftPadding: Int
    get() = paddingLeft
    set(value) = setPadding(value, paddingTop, paddingRight, paddingBottom)

inline var View.topPadding: Int
    get() = paddingTop
    set(value) = setPadding(paddingLeft, value, paddingRight, paddingBottom)

inline var View.rightPadding: Int
    get() = paddingRight
    set(value) = setPadding(paddingLeft, paddingTop, value, paddingBottom)

inline var View.bottomPadding: Int
    get() = paddingBottom
    set(value) = setPadding(paddingLeft, paddingTop, paddingRight, value)


//////////////////////////////////////////////////
//                   click（防止多次点击）        //
//////////////////////////////////////////////////
/**
 * 点击事件
 * @receiver View
 * @param time 多次点击的时间间隔
 * @param block 点击事件
 */
fun View.click(time: Long = 500, block: View.OnClickListener?) {
    setOnClickListener(object : NoDoubleClickListener(time) {
        override fun onSingleClick(v: View) {
            block?.onClick(v)
        }
    })
}

private abstract class NoDoubleClickListener(private val interval: Long) :
        View.OnClickListener {
    private var mLastClickTime: Long = 0

    override fun onClick(v: View) {
        val nowTime = System.currentTimeMillis()
        if (nowTime - mLastClickTime >= interval) { // 单次点击事件
            mLastClickTime = nowTime
            onSingleClick(v)
        }
    }

    protected abstract fun onSingleClick(v: View)
}

/**
 * 长按监听
 */
fun View.longClick(block: ((view: View) -> Boolean)?) {
    setOnLongClickListener(block)
}

/**
 * view 转 Bitmap
 * @receiver View
 * @return Bitmap
 */
fun View.toBitmap(
        config: Bitmap.Config = Bitmap.Config.ARGB_8888,
        bgColor: Int = Color.WHITE
): Bitmap {
    val ret = Bitmap.createBitmap(this.width, this.height, config)
    val canvas = Canvas(ret)
    val bgDrawable = this.background
    if (bgDrawable != null) {
        bgDrawable.draw(canvas)
    } else {
        canvas.drawColor(bgColor)
    }
    this.draw(canvas)
    return ret
}

fun View.drawToBitmapWithoutLayout(
        config: Bitmap.Config = Bitmap.Config.ARGB_8888,
        bgColor: Int = Color.WHITE
): Bitmap {
    measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
    layout(0, 0, measuredWidth, measuredHeight)
    return toBitmap(config, bgColor)
}

fun Bitmap.placeBitmapInCenterOffset(
        bitmap: Bitmap,
        leftOffset: Int = 0,
        topOffset: Int = 0
): Bitmap {
    val height = height
    val width = width

    val bitmapResult = this.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(bitmapResult)
//    canvas.drawBitmap(this, 0f, 0f, null)
    canvas.drawBitmap(
            bitmap,
            ((width - bitmap.width) / 2 + leftOffset).toFloat(),
            ((height - bitmap.height) / 2 + topOffset).toFloat(),
            null
    )
    return bitmapResult
}

//////////////////////////////////////////////////
//                   layout                     //
//////////////////////////////////////////////////
/**
 * 等待布局完成
 * @receiver View
 */
suspend fun View.awaitNextLayout() = suspendCancellableCoroutine<Unit> { cont ->

    // 这里的 lambda 表达式会被立即调用，允许我们创建一个监听器
    val listener = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
                v: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
        ) {
            // 视图的下一次布局任务被调用
            // 先移除监听，防止协程泄漏
            v?.removeOnLayoutChangeListener(this)
            // 最终，唤醒协程，恢复执行
            cont.resume(Unit)
        }
    }
    // 如果协程被取消，移除该监听
    cont.invokeOnCancellation { removeOnLayoutChangeListener(listener) }
    // 最终，将监听添加到 view 上
    addOnLayoutChangeListener(listener)

    // 这样协程就被挂起了，除非监听器中的 cont.resume() 方法被调用
}

suspend fun View.awaitAnimationFrame() = suspendCancellableCoroutine<Unit> { continuation ->
    val runnable = Runnable {
        continuation.resume(Unit)
    }
    // If the coroutine is cancelled, remove the callback
    continuation.invokeOnCancellation { removeCallbacks(runnable) }
    // And finally post the runnable
    postOnAnimation(runnable)
}
