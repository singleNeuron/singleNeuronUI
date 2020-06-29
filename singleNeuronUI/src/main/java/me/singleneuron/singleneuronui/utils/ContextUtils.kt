package me.singleneuron.singleneuronui.utils

import android.app.AndroidAppHelper
import android.content.Context
import android.content.SharedPreferences

class ContextUtils {

    companion object {

        val contextInXposed: Context
            get() = AndroidAppHelper.currentApplication().applicationContext

        fun Context.getSharedPreferenceOnUI(): SharedPreferences {
            return this.getSharedPreferences(
                this.packageName + "_preferences",
                Context.MODE_PRIVATE
            )
        }

    }
}