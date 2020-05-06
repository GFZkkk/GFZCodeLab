package com.gfz.mvp

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println(String.format("%.1f", 3.1))
//        var list = mutableListOf<Int>(1,2,3,4,5)
//        list.move()
//        list.forEach{
//            print(it)
//        }

    }

    fun MutableList<Int>.move(isNext: Boolean = true){
        val num: Int = this.count() / 2
        val partNum = if (isNext) this.count() - num else 0
        this.filterIndexed { index, _ ->
            !(isNext xor (index < num))
        }.forEachIndexed{ index, v ->
            this[index + partNum] = v
        }
    }
}
