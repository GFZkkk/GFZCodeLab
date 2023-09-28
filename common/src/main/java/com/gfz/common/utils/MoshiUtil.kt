package com.gfz.common.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

object MoshiUtil {
    /**
     * bean 转 json
     * @param t T
     * @return String
     *
     *  val user = UserEntity()
     *  val json = MoshiUtil.toJson(user)
     */
    inline fun <reified T> toJson(t: T): String {
        val jsonAdapter: JsonAdapter<T> = create().adapter(T::class.java)
        return jsonAdapter.toJson(t)
    }

    fun create(): Moshi {
        return Moshi.Builder().build()
    }

    /**
     * json 转 bean
     * @param json String
     * @return T?
     *
     * val json = "{xxxx}"
     * val user = MoshiUtil.fromJson<UserEntity>(json)
     */
    inline fun <reified T> fromJson(json: String): T? {
        val jsonAdapter: JsonAdapter<T> = create().adapter(T::class.java)
        return jsonAdapter.fromJson(json)
    }
}