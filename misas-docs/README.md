####源码改动

不需要对于检测问题的分析,直接注释
![alt 属性文本](img/1604456573(1).jpg)


####build

sonar-python embeds [Typeshed](https://github.com/python/typeshed) as a Git submodule. Prior to building the project, you should therefore run `git submodule update --init` to retrieve the corresponding sources.

```mvn clean install```

####引入依赖
在项目中引入以下依赖即可开始自定义规则
```
<dependency>
       <groupId>org.sonarsource.python</groupId>
       <artifactId>python-checks-testkit</artifactId>
       <version>3.2-SNAPSHOT</version>
 </dependency>
```

####自定义规则
1. 继承PythonSubscriptionCheck类
1. 重写initialize方法
1. 通过Tree.Kind的类型查找所需的属性值
![alt 属性文本](img/1604468588(1).jpg)