// https://maven.apache.org/archetype/maven-archetype-plugin/advanced-usage.html
// https://github.com/apache/maven-archetype/blob/9dc6960ce43c59c0345756de4432a18cc1a8212b/archetype-common/src/main/java/org/apache/maven/archetype/ArchetypeGenerationRequest.java

import org.apache.commons.io.FileUtils

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

def packageName = request.packageName
def demoPackage = "com.demo"
def artifactId = request.artifactId
println "archetype-post-generate >>>>>"

Path projectPath = Paths.get(request.outputDirectory, "${artifactId}")
// handle setenv.sh MAIN_CLASS
Path setEnvPath = projectPath.resolve("${artifactId}-server/build/assembly/conf/setenv.sh");
Charset charset = StandardCharsets.UTF_8
String content = new String(Files.readAllBytes(setEnvPath), charset);
content = content.replace(demoPackage, packageName);
Files.write(setEnvPath, content.getBytes(charset));
println "relpace ${setEnvPath} ${demoPackage} to ${packageName}"

// 隐含文件不会生成到archetype中需要处理
// 闭包可以访问外部变量比较方便
def copyFromClassPath = {
        /**
         * @param target 相对archetype-resources的路径
         * @param dst 相对projectPath 的路径
         */
    String target, String dst ->
        InputStream input
        try {
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream("archetype-resources/${target}")
            FileUtils.copyInputStreamToFile(input, projectPath.resolve(dst).toFile())
            println "copy ${target} to ${dst}"
        } finally {
            null != input && input.close()
        }


}

// copy gitignore
copyFromClassPath("gitignore", ".gitignore")
