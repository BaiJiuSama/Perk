package cn.irina.perk.util

import com.google.common.collect.ImmutableSet
import java.io.File
import java.io.IOException
import java.net.URL
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.jar.JarFile

/**
 * 改进版的 ClassUtil，用于从 JAR 文件中扫描类。
 * 该工具类旨在提供更健壮、更安全且更易于维护的代码。
 */
class ClassUtil private constructor() {
    // 禁止实例化工具类，抛出更具体的异常
    init {
        throw UnsupportedOperationException("ClassUtil is a utility class and cannot be instantiated.")
    }
    
    companion object {
        private const val CLASS_FILE_EXTENSION = ".class"
        
        /**
         * 扫描包含给定 plugin 实例的 JAR 文件，并返回指定包（包括子包）中的所有类。
         *
         * 此工具旨在处理通过 CodeSource 识别出的 JAR 文件。
         * 它不扫描类路径中的目录，如果 CodeSource 指向一个目录，将返回一个空集并打印警告。
         *
         * @param plugin 一个位于要扫描的 JAR 文件中的对象实例。
         * @param packageName 需要扫描的包名（例如："com.example.myplugin"）。
         * @return 一个不可变的 `Class` 对象集合，包含在指定包中找到的所有类。如果未找到或 CodeSource 指向目录，则返回空集。
         * @throws IOException 如果在读取 JAR 文件时发生 I/O 错误，或遇到不支持的协议。
         * @throws IllegalStateException 如果 plugin 的 CodeSource 位置为 null 或格式错误。
         */
        fun getClassesInPackage(plugin: Any, packageName: String): ImmutableSet<Class<*>> {
            // 使用 mutableSetOf 以便在过程中添加类，并确保不重复
            val classes = mutableSetOf<Class<*>>()
            
            val codeSource = plugin.javaClass.protectionDomain.codeSource
            val location = codeSource?.location ?: throw IllegalStateException("Plugin's CodeSource location cannot be null.")
            
            val actualJarFileOnDisk: File
            
            // 根据 CodeSource URL 的协议确定 JAR 文件在磁盘上的实际路径
            when (location.protocol) {
                "jar" -> {
                    // 示例: jar:file:/path/to/my.jar!/BOOT-INF/classes!/
                    // 我们需要提取 "/path/to/my.jar"
                    val path = URLDecoder.decode(location.path, StandardCharsets.UTF_8.name())
                    val exclamationIndex = path.indexOf("!")
                    if (exclamationIndex == -1) {
                        throw IllegalStateException("Malformed JAR URL in CodeSource path: $path, expected '!' delimiter.")
                    }
                    val fileUrlPart = path.take(exclamationIndex) // 例如: file:/path/to/my.jar
                    // 使用 toURI() 和 File 构造函数以更健壮地处理文件路径，避免编码问题
                    actualJarFileOnDisk = File(URL(fileUrlPart).toURI())
                }
                "file" -> {
                    // 示例: file:/path/to/my.jar 或 file:/path/to/classes/
                    val path = URLDecoder.decode(location.path, StandardCharsets.UTF_8.name())
                    actualJarFileOnDisk = File(path)
                    if (actualJarFileOnDisk.isDirectory) {
                        // 此工具特指扫描 JAR 文件。如果 CodeSource 指向目录，则返回空集并警告。
                        System.err.println(
                            "Warning: CodeSource for plugin '${plugin.javaClass.name}' points to a directory " +
                                    "('${actualJarFileOnDisk.absolutePath}'), not a JAR file. " +
                                    "This utility scans JARs only. Returning empty set."
                        )
                        return ImmutableSet.of() // JARFile 无法打开目录，因此返回空集
                    }
                }
                else -> {
                    throw IOException(
                        "Unsupported CodeSource protocol: '${location.protocol}' " +
                                "for plugin '${plugin.javaClass.name}'. " +
                                "Only 'file' and 'jar' protocols are supported."
                    )
                }
            }
            
            // 检查识别出的 JAR 文件是否存在且确实是一个文件
            if (!actualJarFileOnDisk.exists() || !actualJarFileOnDisk.isFile) {
                throw IOException(
                    "JAR file identified by CodeSource does not exist or is not a file: " +
                            "'${actualJarFileOnDisk.absolutePath}'"
                )
            }
            
            val jarFile: JarFile
            try {
                jarFile = JarFile(actualJarFileOnDisk)
            } catch (e: IOException) {
                throw IOException("Failed to open JAR file '${actualJarFileOnDisk.absolutePath}'", e)
            }
            
            // 使用 Kotlin 的 'use' 块确保 JarFile 会被自动关闭
            jarFile.use { jar ->
                // 将包名转换为 JAR 条目路径格式，并添加斜杠以便进行前缀匹配
                val relPath = packageName.replace('.', '/') + "/"
                
                for (entry in jar.entries()) {
                    val entryName = entry.name
                    // 检查条目是否是 .class 文件，并且位于目标包路径下
                    if (entryName.endsWith(CLASS_FILE_EXTENSION) && entryName.startsWith(relPath)) {
                        // 将 JAR 条目名转换为标准的类名
                        val className = entryName.removeSuffix(CLASS_FILE_EXTENSION).replace('/', '.')
                        
                        try {
                            // 使用 plugin 自身的类加载器来加载类，这在插件系统中非常重要
                            val clazz = plugin.javaClass.classLoader.loadClass(className)
                            classes.add(clazz)
                        } catch (e: ClassNotFoundException) {
                            System.err.println(
                                "Warning: Could not load class '$className' from JAR " +
                                        "'${actualJarFileOnDisk.name}': ${e.message}"
                            )
                            // 继续处理下一个条目
                        } catch (e: NoClassDefFoundError) {
                            System.err.println(
                                "Warning: NoClassDefFoundError loading class '$className' from JAR " +
                                        "'${actualJarFileOnDisk.name}': ${e.message}"
                            )
                        } catch (e: Throwable) { // 捕获其他任何意外的类加载错误
                            System.err.println(
                                "Warning: Unexpected error loading class '$className' from JAR " +
                                        "'${actualJarFileOnDisk.name}': ${e.javaClass.simpleName} - ${e.message}"
                            )
                        }
                    }
                }
            }
            
            // 返回一个不可变的集合
            return ImmutableSet.copyOf(classes)
        }
    }
}
