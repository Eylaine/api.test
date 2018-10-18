# api.test

基于 Maven + TestNG + Allure 的接口自动化测试框架

支持多环境配置

1、清理工程：
    mvn clean
2、执行测试：
    mvn test
3、转换测试报告为Html格式：
    allure generate allure-results -o output/report --clean