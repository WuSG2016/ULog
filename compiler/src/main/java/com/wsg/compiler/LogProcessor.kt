package com.wsg.compiler

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.wsg.annotation.*
import java.util.LinkedHashSet
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 *  @author WuSG
 *  @date : 2020-01-06 17:06
 *  日志处理器
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class LogProcessor : AbstractProcessor() {

    companion object {
        const val ABSTRACT_LOGGER = "com.wsg.common.AbstractLogger"
        const val COMMON_PACKAGE_NAME = "com.wsg.common"
        const val APPLICATION_PACKAGE_NAME = "android.app"
    }


    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val types = LinkedHashSet<String>()
        types.add(ULog::class.java.canonicalName)
        return types
    }

    /**
     * 日志打印
     */
    private fun log(msg: String) {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, msg)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_8
    }

    override fun process(
        typeElement: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        if (!typeElement.isNullOrEmpty()) {
            val annotatedSet = roundEnvironment!!.getElementsAnnotatedWith(ULog::class.java)
            if (annotatedSet.isNotEmpty()) {
                processLog(annotatedSet)
            }
        }
        return true
    }

    private fun processLog(annotatedSet: Set<Element>) {
        if (annotatedSet.size > 1) {
            log("ULog annotation greater than 1")
            return
        }
        for (element in annotatedSet) {
            val logAnnotation = element.getAnnotation(ULog::class.java)
            val abstractLogger = processingEnv.elementUtils.getTypeElement(ABSTRACT_LOGGER)
            if (processingEnv.typeUtils.isSubtype(element.asType(), abstractLogger.asType())) {
                createKotlinFile(element, logAnnotation)
            } else {
                log("Just Support AbstractLogger Log!")
            }
        }
    }

    private fun createKotlinFile(element: Element, logAnnotation: ULog?) {
        if (logAnnotation!!.tagName.isEmpty()) {
            log("logAnnotation size is 0")
            return
        }
        val fileClassNameString = "_${element.simpleName}"
        val e = element.enclosingElement
        val className = element.simpleName.toString()
        val packNameString = processingEnv.elementUtils.getPackageOf(e).asType().toString()

        val arrayFunction1 = arrayListOf<FunSpec.Builder>()

        val arrayFunction2 = arrayListOf<FunSpec.Builder>()
        val tag = ParameterSpec.builder("tag", String::class)
            .defaultValue("\"TAG\"")
            .build()
        val msgParameter = ParameterSpec.builder("msg", String::class)
            .build()
        val jsonLogTag = ParameterSpec.builder("logTag", String::class)
            .defaultValue("\"jsonLogTag\"")
            .build()
        val defaultLogTagParameter = ParameterSpec.builder("tag", String::class)
            .defaultValue("\"Ubox_TAG\"")
            .build()
        val defaultLogTagSet = arrayOf("d", "e", "w", "i")
        val allLogTag = logAnnotation.tagName.plus(defaultLogTagSet)
        for (funName in allLogTag) {
            val funSpec =
                when {
                    defaultLogTagSet.contains(funName) -> {
                        FunSpec.builder(funName)
                            .addParameter(msgParameter)
                            .addParameter(defaultLogTagParameter)
                            .addStatement(
                                " %T.$funName(msg = msg, tag = tag)",
                                ClassName(COMMON_PACKAGE_NAME, "Logger")
                            )

                    }
                    else -> {
                        FunSpec.builder(funName)
                            .addParameter(msgParameter)
                            .addParameter(tag)
                            .addStatement("if (mLoggerConfig!!.checkPermission()) {")
                            .addStatement("val filePath = mLoggerConfig?.getLogFilePath(\"$funName\")")
                            .addStatement(
                                " %T.otherTagLog(msg = msg, tag = tag, filePath = filePath!!)",
                                ClassName(COMMON_PACKAGE_NAME, "Logger")
                            )
                            .addStatement("}")
                    }
                }
            val funSpec2 =
                when {
                    defaultLogTagSet.contains(funName) -> {
                        FunSpec.builder(funName)
                            .addParameter(msgParameter)
                            .addStatement(
                                " %T.$funName(msg = msg, tag = \"Ubox_TAG\")",
                                ClassName(COMMON_PACKAGE_NAME, "Logger")
                            )
                    }
                    else -> {
                        FunSpec.builder(funName)
                            .addParameter(msgParameter)
                            .addStatement("if (mLoggerConfig!!.checkPermission()) {")
                            .addStatement("val filePath = mLoggerConfig?.getLogFilePath(\"$funName\")")
                            .addStatement(
                                " %T.otherTagLog(msg = msg,  tag = \"Ubox_TAG\", filePath = filePath!!)",
                                ClassName(COMMON_PACKAGE_NAME, "Logger")
                            )
                            .addStatement("}")
                    }
                }
            arrayFunction1.add(funSpec)
            arrayFunction2.add(funSpec2)
        }
        val logger =
            PropertySpec.builder(
                "mLogger",
                ClassName(packNameString, className).copy(nullable = true)
            )
                .mutable()
                .addModifiers(KModifier.PRIVATE)
                .initializer("$className()")
                .build()
        val loggerConfig =
            PropertySpec.builder(
                "mLoggerConfig",
                ClassName(COMMON_PACKAGE_NAME, "AbstractLogConfig").copy(nullable = true)
            )
                .mutable()
                .addModifiers(KModifier.PRIVATE)
                .initializer("mLogger?.getAbstractLogConfig()")
                .build()

        val flux=FunSpec.constructorBuilder()
            .addStatement(" mLogger = %T()",ClassName(packNameString,className))
            .addStatement(" mLoggerConfig = mLogger?.getAbstractLogConfig()")
            .build()
        val companion = TypeSpec.companionObjectBuilder()
            .addProperty(logger)
            .addProperty(loggerConfig)

        companion.let {
            it.addFunction(
                FunSpec.builder("json")
                    .addParameter("json", String::class)
                    .addParameter(jsonLogTag)
                    .addStatement("if (mLoggerConfig?.checkPermission()!!) {")
                    .addStatement("val filePath = mLoggerConfig?.getLogFilePath(logTag)")
                    .addStatement(
                        " %T.json(json = json, filePath = filePath!!)",
                        ClassName(COMMON_PACKAGE_NAME, "Logger")
                    )
                    .addStatement("}")
                    .build()
            )
            it.addFunction(
                FunSpec.builder("json")
                    .addParameter("json", String::class)
                    .addStatement("if (mLoggerConfig?.checkPermission()!!) {")
                    .addStatement("val filePath = mLoggerConfig?.getLogFilePath(\"jsonLogTag\")")
                    .addStatement(
                        " %T.json(json = json, filePath = filePath!!)",
                        ClassName(COMMON_PACKAGE_NAME, "Logger")
                    )
                    .addStatement("}")
                    .build()
            )
            for (funSpec in arrayFunction1) {
                it.addFunction(funSpec.build())
            }
            for (funSpec in arrayFunction2) {
                it.addFunction(funSpec.build())
            }
        }
        val file = FileSpec.builder(packNameString, fileClassNameString)
            .addType(
                TypeSpec.classBuilder(fileClassNameString)
                    .addType(companion.build())
                    .build()
            )
            .build()

        file.writeTo(processingEnv.filer)
    }


}