package com.golden.goldcrowncas.inonline.app.activities.play_web

import android.Manifest
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.golden.goldcrowncas.inonline.app.R
import com.golden.goldcrowncas.inonline.app.constants.Intents
import com.golden.goldcrowncas.inonline.app.constants.Url
import com.golden.goldcrowncas.inonline.app.constants.makeIntent
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.io.IOException
import kotlin.concurrent.thread

@Suppress("DEPRECATION")
class PlayWebActivity : AppCompatActivity() {
    private var wvObject: WebView? = null
    var mainCallb: ValueCallback<Array<Uri>>? = null
    private var secondCallb: Uri? = null
    private var containerOfUrl: ContainerOfUrl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_web)
        (findViewById<View>(R.id.img_preloagind).background as AnimationDrawable).start()
    }

    override fun onResume() {
        super.onResume()

        try {
        val url = getSharedPreferences("url_file", MODE_PRIVATE).getString("url", null)
        if (url == null) {
            Log.i("Play web activity", "Uri is null.")
            thread {
                runBlocking {
                    val response = Retrofit.Builder()
                        .baseUrl(Url.BASE_URL)
                        .addConverterFactory(MoshiConverterFactory.create())
                        .build()
                        .create(RetrofitGetDataApi::class.java)
                        .getInfo()
                    if (response.isSuccessful) {
                        Log.i("Play web activity", "Request is successful.")
                        val info = response.body()!!
                        if (info.can == true && !info.url.isNullOrEmpty()) {
                            Log.i("Play web activity", "Can is true and url is \"${info.url}\".")
                            runOnUiThread {
                                activateWebGame(info.url, true)
                            }
                            return@runBlocking
                        }
                        else {
                            Log.i("Play web activity", "Can is ${info.can} and url is \"${info.url}\".")
                        }
                    }
                    else {
                        Log.w("Play web activity", "Request isn't successful.")
                    }
                    runOnUiThread {
                        startActivity(this@PlayWebActivity makeIntent Intents.OFFLINE_GAMES)
                        finish()
                    }
                }
            }

        } else {
            activateWebGame(url)
        }
    }
        catch (e: Exception) {
            Toast.makeText(this@PlayWebActivity, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun activateWebGame(url: String, saveUrl: Boolean = false) {

        (findViewById<View>(R.id.img_preloagind).background as AnimationDrawable).stop()
        findViewById<View>(R.id.img_preloagind).visibility = View.GONE
        wvObject = findViewById(R.id.wv_web_game)
        wvObject!!.visibility = View.VISIBLE
        (wvObject!!.parent as FrameLayout).setBackgroundColor((0x00000000))

        if(saveUrl) {
            getSharedPreferences("url_file", MODE_PRIVATE).edit()
                .putString("url", url).apply()
        }

        containerOfUrl = ContainerOfUrl(url)

        wvObject!!.settings.domStorageEnabled = webViewIsNotNull()
        wvObject!!.settings.allowUniversalAccessFromFileURLs = webViewIsNotNull()
        wvObject!!.settings.javaScriptEnabled = webViewIsNotNull()

        configureWEObject()

        val cm = getCookieManager()
        cm.setAcceptCookie(webViewIsNotNull())
        cm.setAcceptThirdPartyCookies(wvObject, webViewIsNotNull())

        wvObject!!.loadUrl(url)
    }

    private fun webViewIsNotNull() = wvObject != null
    private fun getChangedUserAgent(userAgent: String): String {
        return if(userAgent.contains("; wv")) {
            userAgent.replace("; wv", "")
        }
        else {
            userAgent
        }
    }
    private fun getCookieManager(): CookieManager {
        return CookieManager.getInstance()
    }
    private fun getOneOrZero(num: Int): Int {
        return when {
            num > 0 -> 1
            num == 0 -> 0
            else -> -1
        }
    }

    private fun configureWEObject() {
        wvObject!!.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return !request?.url.toString().let { it.contains("http") && it.contains("/") }
            }
        }
        wvObject!!.settings.allowContentAccess = webViewIsNotNull()
        wvObject!!.settings.allowFileAccess = webViewIsNotNull()
        wvObject!!.settings.allowFileAccessFromFileURLs = webViewIsNotNull()
        val usrAgent = wvObject!!.settings.userAgentString
        wvObject!!.settings.mixedContentMode = getOneOrZero(0)
        wvObject!!.settings.cacheMode = getOneOrZero(-55)
        wvObject!!.settings.userAgentString = getChangedUserAgent(usrAgent)
        wvObject!!.settings.databaseEnabled = webViewIsNotNull()
        wvObject!!.settings.javaScriptCanOpenWindowsAutomatically = webViewIsNotNull()
        wvObject!!.settings.useWideViewPort = webViewIsNotNull()
        wvObject!!.settings.loadWithOverviewMode = webViewIsNotNull()
        wvObject!!.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                val onCreateWindow = super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
                Log.i("Web chrome client", "Window created.")
                return onCreateWindow
            }

            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                mainCallb = filePathCallback
                return true
            }

            override fun onCloseWindow(window: WebView?) {
                super.onCloseWindow(window)
                Log.i("Web chrome client", "Window closed.")
            }
        }
    }

    val requestPermissionLauncher = registerForActivityResult (
        ActivityResultContracts.RequestPermission()
    ) { _: Boolean? ->
        val pictureFile = createPictureFileOrNull()
        val takeIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        setTakeIntentAndSecondCallb(takeIntent, pictureFile)
        val oldIntent = getOldIntent(Intent(Intent.ACTION_GET_CONTENT))
        val chooserIntent = getChooserIntent(Intent(Intent.ACTION_CHOOSER), oldIntent, takeIntent)
        startActivityForResult(chooserIntent, 1)
    }

    private fun createPictureFileOrNull(): File? {
        return try {
            File.createTempFile(
                "picture_file",
                ".jpg",
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            )
        } catch (ex: IOException) {
            Log.e("Picture creating", "Exception...", ex)
            null
        }
    }

    private fun setTakeIntentAndSecondCallb(takeIntent: Intent, pictureFile: File?) {
        takeIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            Uri.fromFile(pictureFile)
        )
        secondCallb = Uri.fromFile(pictureFile)
    }

    private fun getOldIntent(oldIntent: Intent): Intent {
        oldIntent.addCategory(Intent.CATEGORY_OPENABLE)
        oldIntent.type = "*/*"
        return oldIntent
    }

    private fun getChooserIntent(chooserIntent: Intent, oldIntent: Intent, takeIntent: Intent): Intent {
        chooserIntent.putExtra(Intent.EXTRA_INTENT, oldIntent)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, Array(1) { takeIntent })
        return chooserIntent
    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (mainCallb == null) return
        if (resultCode == -1) {
            Log.i("On activity result", "Try")
            mainCallb!!.onReceiveValue(
                getDataStringUriIfNotNull(data) ?: getSecondCallbInArrayIfNotNull()
            )
        } else {
            mainCallb!!.onReceiveValue(null)
        }
        mainCallb = null
    }

    private fun getDataStringUriIfNotNull(data: Intent?) = data?.dataString?.let {
        arrayOf(Uri.parse(it))
    }

    private fun getSecondCallbInArrayIfNotNull() = secondCallb?.let {
        arrayOf(it)
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        wvObject?.saveState(outState)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (wvObject?.canGoBack() == true) {
            wvObject?.goBack()
        }
        else {
            Log.w("On back pressed", "Can not go back.")
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        wvObject?.restoreState(savedInstanceState)
    }
}