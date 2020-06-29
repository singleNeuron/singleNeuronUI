package me.singleneuron.singleneuronui.sharedpreferences

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.singleneuron.singleneuronui.activities.BackgroundActivity

class ContentProviderPreference private constructor() {

    lateinit var applicationId: String
    lateinit var contentProviderUri: String

    constructor(@ContentProvider.ContentProviderParams position: String, key: String?, context: Context, mApplicationId: String, mContentProviderUri: String) : this() {
        var bundle: Bundle? = null
        applicationId = mApplicationId
        contentProviderUri = mContentProviderUri
        if (position == ContentProvider.CONTENT_PROVIDER_COMMIT) {
            getBundle(position, key, context)
            return
        }
        if (position == ContentProvider.CONTENT_PROVIDER_JSON) {
            bundle = getBundle(position, key, context)
        } else if (position == ContentProvider.CONTENT_PROVIDER_PREFERENCE || position == ContentProvider.CONTENT_PROVIDER_DEVICE_PROTECTED_PREFERENCE) {
            bundle = getBundle(position, null, context)
        }
        if (bundle == null) {
            this.jsonElement = JsonObject()
        } else {
            //Log.d("XposedMusicNotify", "getBundle: $bundle")
            val originalJsonString = bundle.getString(ContentProvider.BUNDLE_KEY_JSON_STRING)
            try {
                if (!originalJsonString!!.startsWith('['))
                    this.jsonElement = JsonParser.parseString(originalJsonString)
            } catch (e: Exception) {
                e.printStackTrace()
                jsonElement = JsonObject()
            }
        }
    }

    lateinit var jsonElement: JsonElement

    fun getJSONString(): String {
        return GsonBuilder().setPrettyPrinting().create().toJson(jsonElement)
    }


    fun getBundle(@ContentProvider.ContentProviderParams position: String, key: String?, context: Context): Bundle? {
        return getBundle(position, key, context, null)
    }

    fun getBundle(@ContentProvider.ContentProviderParams position: String, key: String?, context: Context, extras: Bundle?): Bundle? {
        try {
            val contentResolver: ContentResolver = context.contentResolver
            val uri = Uri.parse(contentProviderUri)
            var result: Bundle? = null
            try {
                result = contentResolver.call(uri, position, key, extras)
            } catch (e: RuntimeException) {
                try {
                    val intent = Intent()
                    intent.setClassName(applicationId, BackgroundActivity::class.java.name)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } catch (e1: Throwable) {
                    return null
                }
            }
            if (result == null) {
                result = contentResolver.call(uri, position, key, extras)
            }
            if (result == null) {
                return null
            }
            return result
        } catch (ignored: Throwable) {
            return null
        }
    }

}