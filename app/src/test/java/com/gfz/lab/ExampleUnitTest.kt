package com.gfz.lab

import com.gfz.common.ext.getClass
import com.gfz.common.task.EventListTaskUtil
import com.gfz.common.utils.SpUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.ArrayList
import java.util.logging.Handler

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {


        println(listOf(1, 2, 3) == listOf(1, 2, 3))
        println(listOf(1, 2, 3).asSequence() == listOf(1, 2, 3).asSequence())
        println(sequenceOf(1, 2, 3) == sequenceOf(1, 2, 3))

    }

    @Test
    fun printApi(){
        buildApi(
            listOf(
                ApiBean("mm_sync_data/save_user_speech_info_record.do", "保存用户跟读打分使用记录")
            )
        )
    }

    @Test
    fun scale() {
        val w = 300
        val h = 200
        val vw = 1702
        val vh = 876
        val scale = 1f * w / h
        val scale2 = 1f * vw / vh
        val s = if (scale < scale2){
            1f * w / vw
        } else {
            1f * h / vh
        }
        val bw = (s * vw).toInt()
        val bh = (s * vh).toInt()
        val ss = 1f * bw / bh
        println(Scale(w,h,vw,vh,scale,scale2,s,bw,bh,ss))
    }

    data class Scale(
        val w: Int,
        val h: Int,
        val vw: Int,
        val vh: Int,
        val scale: Float,
        val scale2: Float,
        val s: Float,
        val bw: Int,
        val bh: Int,
        val ss: Float,
    )

    @Test
    fun code(){
        val list = listOf(
            User(1, "部门1", 0),
            User(2, "部门2", 1),
            User(3, "部门3", 1),
            User(4, "部门4", 3),
            User(5, "部门5", 4),
            User(6, "部门6", 4),
        )
        handler(list)
        println(list.toString())
    }

    private fun handler(list: List<User>){
        val map = HashMap<Int, MutableList<User>>()
        // id不重复，id不是很多，pid比较分散
        list.forEach {
            val children = map[it.id]
            if (children == null){
                map[it.id] = it.children
            } else {
                it.children = children
            }
            if (it.pid != 0){
                var parent = map[it.pid]
                if (parent == null){
                    parent = ArrayList()
                    map[it.pid] = parent
                }
                parent.add(it)
            }
        }
    }

    data class User(
        val id: Int,
        val name: String,
        val pid: Int,
        var children: MutableList<User> = ArrayList()
    )

    @Test
    fun flow(){
        val list = sequence{
            yield("String")
            yield("Int")
            yield("Boolean")
        }.filter {
            it.length > 3
        }.map {
            it.length
        }.take(4).toList()

        println(list.toString())
    }

    @Test
    fun random() {
        val x = 333
        println(x shr 1)
        println(x shr 1)
        println(x shr 1)
    }

    @Test
    fun testSP() {
        val result = SpUtil.getUserKey("U_test")
        println(result)
    }

    fun thread() = runBlocking {
        val job = GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
            println("World!") // 在延迟后打印输出
        }
        println("Hello,") // 协程已在等待时主线程还在继续
        job.join() // 阻塞主线程 2 秒钟来保证 JVM 存活
    }

    private fun buildApi(url: List<ApiBean>) {
        url.forEach {
            buildApi(it)
        }
        println()
        url.forEach {
            buildApiIml(it)
        }

    }

    private fun buildApi(bean: ApiBean) {
        val url = bean.url
        val tip = bean.tip
        val funName = getFunNameByUrl(url)
        val api = "/**\n" +
                "     * $tip\n" +
                "     */\n" +
                "    @FormUrlEncoded\n" +
                "    @POST(\"$url\")\n" +
                "    Single<ResponseBody> $funName(@FieldMap Map<String,Object> params);"
        println(api)
        println()
    }

    private fun buildApiIml(bean: ApiBean) {
        val url = bean.url
        val tip = bean.tip
        val funName = getFunNameByUrl(url)
        val apiIml = " /**\n" +
                "     * $tip\n" +
                "     */\n" +
                "    public void $funName(Map<String, Object> map,SingleObserver<String> observer){\n" +
                "        ApiManager.baseInstance().$funName(map)\n" +
                "            .map(new Function<ResponseBody, String>() {\n" +
                "                @Override\n" +
                "                public String apply(ResponseBody responseBody) {\n" +
                "                    return handleResponse(responseBody);\n" +
                "                }\n" +
                "            })\n" +
                "            .subscribeOn(Schedulers.io())\n" +
                "            .observeOn(AndroidSchedulers.mainThread())\n" +
                "            .subscribe(observer);\n" +
                "    }"
        println(apiIml)
        println()
    }

    private fun getFunNameByUrl(url: String): String {
        val funNameList = url.split("/")
        val snake = funNameList[funNameList.size - 1].split(".")[0]
        return getLowerCamelCase(snake)
    }

    private fun getLowerCamelCase(str: String): String {
        val word = str.lowercase().split("_")
        val result = StringBuilder()
        word.forEach {
            if (it.isNotBlank()) {
                if (result.isNotBlank()) {
                    result.append(it[0] - 32)
                    result.append(it.substring(1))
                } else {
                    result.append(it)
                }
            }
        }
        return result.toString()
    }

    fun MutableList<Int>.move(isNext: Boolean = true) {
        val num: Int = this.count() / 2
        val partNum = if (isNext) this.count() - num else 0
        this.filterIndexed { index, _ ->
            !(isNext xor (index < num))
        }.forEachIndexed { index, v ->
            this[index + partNum] = v
        }
    }

    data class ApiBean(val url: String, val tip: String)


    @Test
    fun testClass() {
        val c = Test1()
        println(c.getClass(0)?.name ?: "no")
    }

    class TestType
    class Test1 : TestClass<TestType>()
    open class TestClass<T>

    @Test
    fun show(){
        val alpha100 = getAlpha100()
        val alpha1 = getAlpha1()

    }

    fun getAlpha1(): HashMap<String, Float>{
        return HashMap<String, Float>().apply {
            alpha255.forEach { (t, u) ->
                this[u] = t.toFloat() / 100
            }
        }
    }

    private fun getAlpha100(): HashMap<String, Int>{
        return HashMap<String, Int>().apply {
            alpha255.forEach { (t, u) ->
                this[u] = t
            }
        }
    }

    private val alpha255 = hashMapOf(
        100 to "FF",
        99 to "FC",
        98 to "FA",
        97 to "F7",
        96 to "F5",
        95 to "F2",
        94 to "F0",
        93 to "ED",
        92 to "EB",
        91 to "E8",
        90 to "E6",
        89 to "E3",
        88 to "E0",
        87 to "DE",
        86 to "DB",
        85 to "D9",
        84 to "D6",
        83 to "D4",
        82 to "D1",
        81 to "CF",
        80 to "CC",
        79 to "C9",
        78 to "C7",
        77 to "C4",
        76 to "C2",
        75 to "BF",
        74 to "BD",
        73 to "BA",
        72 to "B8",
        71 to "B5",
        70 to "B3",
        69 to "B0",
        68 to "AD",
        67 to "AB",
        66 to "A8",
        65 to "A6",
        64 to "A3",
        63 to "A1",
        62 to "9E",
        61 to "9C",
        60 to "99",
        59 to "96",
        58 to "94",
        57 to "91",
        56 to "8F",
        55 to "8C",
        54 to "8A",
        53 to "87",
        52 to "85",
        51 to "82",
        50 to "80",
        49 to "7D",
        48 to "7A",
        47 to "78",
        46 to "75",
        45 to "73",
        44 to "70",
        43 to "6E",
        42 to "6B",
        41 to "69",
        40 to "66",
        39 to "63",
        38 to "61",
        37 to "5E",
        36 to "5C",
        35 to "59",
        34 to "57",
        33 to "54",
        32 to "52",
        31 to "4F",
        30 to "4D",
        29 to "4A",
        28 to "47",
        27 to "45",
        26 to "42",
        25 to "40",
        24 to "3D",
        23 to "3B",
        22 to "38",
        21 to "36",
        20 to "33",
        19 to "30",
        18 to "2E",
        17 to "2B",
        16 to "29",
        15 to "26",
        14 to "24",
        13 to "21",
        12 to "1F",
        11 to "1C",
        10 to "1A",
        9 to "17",
        8 to "14",
        7 to "12",
        6 to "0F",
        5 to "0D",
        4 to "0A",
        3 to "08",
        2 to "05",
        1 to "03",
        0 to "00"
    )


}
