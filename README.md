# api.test

基于 Maven + TestNG + Allure 的接口自动化测试框架

支持多环境配置

1、清理工程：
    mvn clean
    
2、执行测试：
    mvn test
    
3、转换测试报告为Html格式：
    allure generate allure-results -o output/report --clean
    
Cookie解决方案：
1、HttplCient单例模式，保持Cookie
2、TestNG注解BeforeTest调用登录接口
3、TestNG配置文件Suite组织管理用例
4、TestCase继承BaseCase

结果校验：
1、Http Status Code
2、Json Schema
3、精确字段校验，Json Path获取字段

测试用例管理：
1、迎合测试报告Allure
2、分开管理的Suite，配置文件在resource/xml下
3、根目录下聚合的TestNG.xml，将2里面的xml文件包含进去
4、执行入口文件在pom.xml文件中配置

TODO：
1、运行参数定制化

改造重构：
1、模板引擎，根据Excel文件生成TestCase的Java文件
