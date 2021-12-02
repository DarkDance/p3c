在扩展的目录中增加规则，不要破坏原有的目录结构，具体可查看AvoidFastJsonAutoTypeSupportRule的方式
涉及文件：
* AvoidFastJsonAutoTypeSupportRule.java
* pateo-extend.xml
* messages.xml
* ExtendRulesTest.java
* AvoidFastJsonAutoTypeSupportRule.xml
* pom里面的ruleset不用管，这个是用来检查当前项目的代码用的

增加文件后，需要修改pom版本号，并且安装到本地，pom版本号在原版本号上增加50个大版本以便区别，比如:
* 2.1.1 -> 52.1.1

上述处理完成后，直接mvn install