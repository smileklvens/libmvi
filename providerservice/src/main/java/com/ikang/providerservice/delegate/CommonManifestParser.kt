package com.ikang.providerservice.delegate

import android.content.Context
import android.content.pm.PackageManager
import java.util.*

/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe 用于解析 AndroidManifest 中的 Meta 属性获取所有声明的applife
 */
class CommonManifestParser(private val context: Context) {
    fun parse(): List<IApp> {
        val modules: MutableList<IApp> =
            ArrayList()
        try {
            val appInfo =
                context.packageManager.getApplicationInfo(
                    context.packageName, PackageManager.GET_META_DATA
                )
            if (appInfo.metaData != null) {
                for (key in appInfo.metaData.keySet()) {
                    if (MODULE_VALUE == appInfo.metaData[key]) {
                        modules.add(parseModule(key))
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Unable to find metadata to parse ConfigModule", e)
        }
        return modules
    }

    companion object {
        private const val MODULE_VALUE = "ConfigAppLife"
        private fun parseModule(className: String): IApp {
            val clazz: Class<*>
            clazz = try {
                Class.forName(className)
            } catch (e: ClassNotFoundException) {
                throw IllegalArgumentException(
                    "Unable to find ConfigModule implementation",
                    e
                )
            }
            val module: Any
            module = try {
                clazz.newInstance()
            } catch (e: InstantiationException) {
                throw RuntimeException(
                    "Unable to instantiate ConfigModule implementation for $clazz",
                    e
                )
            } catch (e: IllegalAccessException) {
                throw RuntimeException(
                    "Unable to instantiate ConfigModule implementation for $clazz",
                    e
                )
            }
            if (module !is IApp) {
                throw RuntimeException("Expected instanceof ConfigModule, but found: $module")
            }
            return module
        }
    }

}