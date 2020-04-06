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
        assertEquals(4, 2 + 2)
/*        val a = arrayOf(true,true,false,false)
        val b = arrayOf(true,false,true,false)
        for (i in 0..3){
            print(test(a[i],b[i]))
            println(" ${!(a[i] xor b[i])}")
        }*/
        val x: MutableList<Int?> = ArrayList(10)
        println(x.count())

    }

    fun MutableList<Int>.move(isNext: Boolean = true, num: Int = 3){
        val partNum = if (isNext) num else 0
        this.filterIndexed { index, _ ->
            !(isNext xor (index < num))
        }.forEachIndexed{ index, v ->
            this[index + partNum] = v
        }
    }
}
