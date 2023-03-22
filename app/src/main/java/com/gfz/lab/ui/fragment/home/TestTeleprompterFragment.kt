package com.gfz.lab.ui.fragment.home

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognitionService
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.gfz.common.ext.setVisible
import com.gfz.common.utils.TopLog
import com.gfz.lab.databinding.FragmentTeleprompterBinding
import com.gfz.ui.base.page.BaseVBFragment
import java.util.*

/**
 *
 * created by xueya on 2023/3/20
 */
class TestTeleprompterFragment : BaseVBFragment<FragmentTeleprompterBinding>(),
    RecognitionListener {

    private val _content = MutableLiveData<String>()

    private lateinit var speechRecognizer : SpeechRecognizer

    private val intent by lazy {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, requireContext().packageName)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.CHINESE.toString())
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
        }

    }

    override fun initView() {
        binding.btnStart.setOnClickListener {
            start()
        }
        binding.btnStop.setOnClickListener {
            stop()
        }
        binding.btnEdit.setOnClickListener {
            if (binding.btnEdit.text == "编辑"){
                binding.btnEdit.text = "完成"
                binding.tvCopyWriting.setVisible(false)
                binding.etCopyWriting.setVisible(true)
            } else {
                binding.btnEdit.text = "编辑"
                binding.tvCopyWriting.setVisible(true)
                binding.etCopyWriting.setVisible(false)
            }

        }

        _content.observe(viewLifecycleOwner){
            binding.tvCopyWriting.text = it
            binding.etCopyWriting.setText(it, TextView.BufferType.EDITABLE)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            check()
        }

        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun check(){
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), 0)
        TopLog.e("testccc 是否可以使用：${SpeechRecognizer.isRecognitionAvailable(requireContext())}")
        val serviceComponent = Settings.Secure.getString(requireContext().contentResolver, "voice_recognition_service")
        TopLog.e("testccc serviceComponent:$serviceComponent")
        val component = ComponentName.unflattenFromString(serviceComponent)
        if (component == null){
            TopLog.e("testccc error:component == null")
            return
        }
        val list = requireContext().packageManager.queryIntentServices(Intent(RecognitionService.SERVICE_INTERFACE), PackageManager.MATCH_ALL)
        list.forEach {
            TopLog.e("testccc   resolveInfo: ${it.serviceInfo.packageName}/${it.serviceInfo.name}")
            speechRecognizer = if (it.serviceInfo.packageName == component.packageName){
                SpeechRecognizer.createSpeechRecognizer(requireContext())
            } else {
                SpeechRecognizer.createSpeechRecognizer(requireContext(),
                    ComponentName(it.serviceInfo.packageName,it.serviceInfo.name)
                )
            }
        }
        speechRecognizer.setRecognitionListener(this)
    }

    private fun start(){
        speechRecognizer.startListening(intent)
    }

    private fun stop(){
        speechRecognizer.stopListening()
        speechRecognizer.cancel()
    }

    override fun onReadyForSpeech(params: Bundle?) {
        TopLog.e("testccc  onReadyForSpeech")
    }

    override fun onBeginningOfSpeech() {
        TopLog.e("testccc  onBeginningOfSpeech")
    }

    override fun onRmsChanged(rmsdB: Float) {
    }

    override fun onBufferReceived(buffer: ByteArray?) {
    }

    override fun onEndOfSpeech() {
        TopLog.e("testccc  onEndOfSpeech")
    }

    override fun onError(error: Int) {
        TopLog.e("testccc  onError:$error")
    }

    override fun onResults(results: Bundle?) {
        results?.let {
            it.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let { content ->
                TopLog.e("testccc  onResults:${content}")
                binding.tvCopyWriting.text = buildString {
                    append(binding.tvCopyWriting.text)
                    content.forEach { text ->
                        append(text)
                    }
                }
            }
        }

    }

    override fun onPartialResults(partialResults: Bundle?) {
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stop()
        speechRecognizer.destroy()
    }
}