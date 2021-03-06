package com.blankj.plugin.readme

import org.gradle.api.Plugin
import org.gradle.api.Project

class ReadmeCorePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('readme', ReadmeExtension)

        project.task('readmeTask') {
            doLast {
                println "readmeTask start..."

                def ext = project['readme'] as ReadmeExtension
                def readmeCN = ext.readmeCnFile
                def readmeEng = ext.readmeFile

                readmeOfUtilCode2Eng(readmeCN, readmeEng)

                println "readmeTask finished."
            }
        }
    }

    static def readmeOfUtilCode2Eng(File readmeCN, File readmeEng) {
        FormatUtils.format(readmeCN)
        def lines = readmeCN.readLines("UTF-8")
        def sb = new StringBuilder()
        readmeCN.eachLine { line ->
            if (line.contains("* ###")) {
                if (line.contains("UtilsTransActivity")) {
                    sb.append(line)
                } else {
                    String utilsName = line.substring(line.indexOf("[") + 1, line.indexOf("Utils"))
                    sb.append("* ### About ").append(utilsName).append(line.substring(line.indexOf(" -> ")))
                }
            } else if (line.contains(": ") && !line.contains("[")) {
                sb.append(line.substring(0, line.indexOf(':')).trim())
            } else if (line.contains("打个小广告") || line.contains("知识星球") || line.contains("我的二维码")) {
                return
            } else {
                sb.append(line)
            }
            sb.append(FormatUtils.LINE_SEP)
        }
        readmeEng.write(sb.toString(), "UTF-8")
    }
}


