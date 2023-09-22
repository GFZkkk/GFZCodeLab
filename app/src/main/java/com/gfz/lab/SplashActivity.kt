package com.gfz.lab

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.Formatter
import com.gfz.common.utils.TopLog
import com.gfz.lab.ui.activity.RootActivity
import com.gfz.ui.base.page.BaseActivity
import me.jessyan.autosize.internal.CancelAdapt
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * 启动页
 * created by gaofengze on 2021/4/30
 */
class SplashActivity : BaseActivity(), CancelAdapt {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val action = intent.action
        val type = intent.type
        if (Intent.ACTION_VIEW == action && type != null) {
            val uri = intent.data ?: return
            try{
                val filePathColumn = arrayOf(MediaStore.Files.FileColumns.SIZE)
                val cursor: Cursor =
                    contentResolver.query(uri, filePathColumn, null, null, null) ?: return
                cursor.moveToFirst()
                val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                val filePath: String = cursor.getString(columnIndex)
                cursor.close()
                TopLog.e("testccc  filePath:$filePath")
            }catch (e: Exception){

            }
            val filePath2 = Uri.decode(uri.encodedPath)
            TopLog.e("testccc  filePath:$filePath2")

            val file = File(filePath2)
            contentResolver.openFileDescriptor(uri, "r")?.let {
                val fileReader = FileReader(it.fileDescriptor)
                val reader = BufferedReader(fileReader)
                val string = buildString {
                    append(reader.readText())
                }
                TopLog.e("testccc: $string")
                reader.close()
                fileReader.close()
                it.close()

            }
            TopLog.e(
                "testccc  fileSize:${
                    Formatter.formatFileSize(
                        this,
                        file.length()
                    )
                }/${Formatter.formatFileSize(this, 5 * 1000 * 1000)}"
            )
        }
    }

    override fun loadView() {}

    override fun initData() {
        handler.postDelayed({
            if (this.isFinishing) {
                return@postDelayed
            }
            start(RootActivity::class.java)
            finish()
        }, 0)
    }
}