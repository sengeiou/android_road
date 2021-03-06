package com.xiaozhu

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.xiaozhu.asm.AutoTransform
import com.xiaozhu.bean.AutoClassFilter
import com.xiaozhu.bean.AutoSettingParams
import com.xiaozhu.config.GlobalConfig
import com.xiaozhu.util.AutoTextUtil
import com.xiaozhu.util.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 插件入口
 */
class PluginEntry implements Plugin<Project> {

    @Override
    void apply(Project project) {

//        project.repositories {
//            mavenLocal()
//        }
//
//        project.dependencies {
//            if (project.gradle.gradleVersion > "4.0") {
//                project.logger.debug("gradlew version > 4.0")
//                implementation 'oms.mmc:autotrack-gradle-plugin:1.0.2-SNAPSHOT'
//            } else {
//                project.logger.debug("gradlew version < 4.0")
//                compile 'oms.mmc:autotrack-gradle-plugin:1.0.2-SNAPSHOT'
//            }
//        }

        project.extensions.create('xiaozhu', AutoSettingParams)
        GlobalConfig.setProject(project)
        println(GlobalConfig.getParams().name)

        //使用Transform实行遍历
        if (project.plugins.hasPlugin(AppPlugin)) {
            def android = project.getExtensions().findByType(AppExtension)
            registerTransform(android)
        }

        project.afterEvaluate {
            Logger.setDebug(project.xiaozhu.isDebug)
            // 用户配置解析
            analysiUserConfig()
        }
    }

    def static registerTransform(BaseExtension android) {
        AutoTransform transform = new AutoTransform()
        android.registerTransform(transform)
    }

    /**
     * 对build.gradle配置参数及自定义内容进行解析
     */
    static void analysiUserConfig() {

        List<Map<String, Object>> matchDataList = GlobalConfig.getParams().matchData
        List<AutoClassFilter> autoClassFilterList = new ArrayList<>()

        matchDataList.each {
            Map<String, Object> map ->
                AutoClassFilter classFilter = new AutoClassFilter()

                String className = map.get("ClassName")
                String interfaceName = map.get("InterfaceName")
                String methodName = map.get("MethodName")
                String methodDes = map.get("MethodDes")
                Closure methodVisitor = map.get("MethodVisitor")
                boolean isAnnotation = map.get("isAnnotation")
                // 类的全类名
                if (!AutoTextUtil.isEmpty(className)) {
                    className = AutoTextUtil.changeClassNameSeparator(className)
                }
                // 接口的全类名
                if (!AutoTextUtil.isEmpty(interfaceName)) {
                    interfaceName = AutoTextUtil.changeClassNameSeparator(interfaceName)
                }

                classFilter.setClassName(className)
                classFilter.setInterfaceName(interfaceName)
                classFilter.setMethodName(methodName)
                classFilter.setMethodDes(methodDes)
                classFilter.setMethodVisitor(methodVisitor)
                classFilter.setIsAnnotation(isAnnotation)

                autoClassFilterList.add(classFilter)
        }

        GlobalConfig.setAutoClassFilter(autoClassFilterList)

        // 需要手动添加的包
        List<String> includePackages = GlobalConfig.getParams().include
        HashSet<String> include = new HashSet<>()
        if (includePackages != null) {
            include.addAll(includePackages)
        }
        GlobalConfig.setInclude(include)

        // 添加需要过滤的包
        List<String> excludePackages = GlobalConfig.getParams().exclude
        HashSet<String> exclude = new HashSet<>()
        // 不对系统类进行操作，避免非预期错误
        exclude.add('android.support')
        exclude.add('androidx')
        exclude.add('com.mmc.lamandys.liba_datapick.base')
        exclude.add('com.google.code.gson')
        exclude.add('com.google.gson')
        if (excludePackages != null) {
            exclude.addAll(excludePackages)
        }
        GlobalConfig.setExclude(exclude)
    }

}