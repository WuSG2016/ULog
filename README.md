[![](https://www.jitpack.io/v/WuSG2016/Ulog.svg)](https://www.jitpack.io/#WuSG2016/Ulog)

- # 基于注解处理器的日志打印工具
- **根目录build.gradle添加** 
```
	allprojects {
		repositories {
			...
			//添加
			maven { url 'https://www.jitpack.io' }
		}
	}
```
- **APP的build.gradle文件增加**
```
apply plugin: 'kotlin-kapt'

dependencies {
    implementation 'com.github.WuSG2016.Ulog:annotation:1.1'
    implementation 'com.github.WuSG2016.Ulog:common:1.1'
    kapt 'com.github.WuSG2016.Ulog:compiler:1.1'
  
	}
```
- ## 新建类继承AbstractLogger并实现方法 如下:
```
//增加注解 增加对应的TAG文件信息(如dev会生成dev目录下dev-2020-1-6.log的文件)
//make project 会生成_UboxLog文件类 包含TAG等方法(_UboxLog.dev(msg))
//开启日志文件监控会根据保留时间移除文件（在下次重新启动的时候）
@ULog(tagName = ["dev"])
class UboxLog : AbstractLogger() {
    override fun getAbstractLogConfig(): AbstractLogConfig {
        return DefaultLogConfig()//默认自带的配置类
    }
    //日志文件保留时长 一分钟
    override fun onRetentionTime(): Long {
        return 1000 * 60
    }
    //是否开启日志监控
    override fun isRetention(): Boolean {
        return true
    }
     //监控的文件夹目录
    override fun onDetectedFolderPath(): String? {
        return FileZipUtils.generateDefaultLogDirectory("ULog")
    }
}


```
- ## 抽象类AbstractLogConfig说明

```
AbstractLogConfig
     //日志输出文件夹
    abstract fun getDefaultLogDirectory(): String?
    //日志的后缀名
    abstract fun onSuffix(): String?

```
- ## **`注意事项`**
- **使用前记得获取读写权限**
- **增加kotlin注解处理器支持以及kotlin支持**
- ## 更新说明 
- #### 1.2版本修改日志压缩
```
  val file = File("/mnt/sdcard/ULog/output.zip")
        //解压
        if (file.smartCreateNewFile()) {
            file.unZipTo("/mnt/sdcard/Ulog/2020-02")
        }
        //压缩
        file.zipOutputStream()
            .zipFrom(
                "/mnt/sdcard/Ulog/2020-01/dev",
                "/mnt/sdcard/Ulog/2020-01/devLog/devLog-2020-01-9.log"
            )
```
- #### 1.5版本增加日志文件监控