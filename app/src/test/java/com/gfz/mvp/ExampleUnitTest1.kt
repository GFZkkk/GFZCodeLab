package com.gfz.mvp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.ArrayList

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest1 {

    @Test
    fun addition_isCorrect() {
        buildApi(listOf(
            ApiBean("ext_data/query_ext_example_by_senseId.do","根据释义ID查询补充例句")
        ))
    }

    fun thread() = runBlocking{
        val job = GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
            println("World!") // 在延迟后打印输出
        }
        println("Hello,") // 协程已在等待时主线程还在继续
        job.join() // 阻塞主线程 2 秒钟来保证 JVM 存活
    }

    private fun buildApi(url: List<ApiBean>){
        url.forEach {
            buildApi(it)
        }
        println()
        url.forEach {
            bulidApiIml(it)
        }

    }

    private fun buildApi(bean: ApiBean){
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

    private fun bulidApiIml(bean: ApiBean){
        val url = bean.url
        val tip = bean.tip
        val funName = getFunNameByUrl(url)
        val apiIml =" /**\n" +
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

    private fun getFunNameByUrl(url: String): String{
        val funNameList = url.split("/")
        val snake = funNameList[funNameList.size - 1].split(".")[0]
        return getLowerCamelCase(snake)
    }

    private fun getLowerCamelCase(str: String): String{
        val word = str.toLowerCase().split("_")
        val result = StringBuilder()
        word.forEach {
            if(it.isNotBlank()){
                if (result.isNotBlank()){
                    result.append(it[0] - 32)
                    result.append(it.substring(1))
                }else{
                    result.append(it)
                }
            }
        }
        return result.toString()
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

    data class ApiBean(val url: String, val tip: String)
}
