# 开发环境

开发用数据库: `localhost:3306/dmc_apim, root, 空密码, serverTimezone=Hongkong`

初始化 DAO 层: `./gradlew mbgenerator`

编译: `./gradlew build`

# Zuul 过滤器说明(按执行顺序)
##  CustomTokenValidateFilter

 用户校验请求头中的token是否不存在或者被禁用

## CustomZuulPreFilter
 用于校验请求头中的token解析以及请求参数校验
 
## CustomZuulPostFilter
 用于请求完成后的请求记录操作

## **CustomZuulErrorFilter**(特殊异常过滤器)
 当出现异常的时候的处理器

