package cn.irina.perk.util

import com.google.common.collect.ImmutableSet
import java.io.IOException
import java.net.URLDecoder
import java.util.jar.JarFile

class ClassUtil private constructor() {
    init {
        throw RuntimeException("Cannot instantiate a utility class.")
    }

    companion object {
        fun getClassesInPackage(plugin: Any, packageName: String): MutableCollection<Class<*>?>? {
            val classes: MutableCollection<Class<*>?> = ArrayList<Class<*>?>()
            val codeSource = plugin.javaClass.getProtectionDomain().codeSource
            val resource = codeSource.location
            val relPath = packageName.replace('.', '/')
            val resPath = URLDecoder.decode(resource.path)
            val jarPath = resPath.replaceFirst("[.]jar!.*".toRegex(), ".jar").replaceFirst("file:".toRegex(), "")

            val jarFile: JarFile?
            try {
                jarFile = JarFile(jarPath)
            } catch (var17: IOException) {
                throw RuntimeException("Unexpected IOException reading JAR File '$jarPath'", var17)
            }

            val entries = jarFile.entries()

            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val entryName = entry.getName()
                var className: String? = null
                if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length > relPath.length + "/".length) {
                    className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "")
                }

                if (className != null) {
                    var clazz: Class<*>? = null

                    try {
                        clazz = Class.forName(className)
                    } catch (var16: ClassNotFoundException) {
                        var16.printStackTrace()
                    }

                    if (clazz != null) {
                        classes.add(clazz)
                    }
                }
            }

            try {
                jarFile.close()
            } catch (var15: IOException) {
                var15.printStackTrace()
            }

            return ImmutableSet.copyOf<Class<*>?>(classes)
        }
    }
}
