<p align="center">
  <img src="http://oss.vihacker.top/image/ViHackerlogo-1.png" width="260">
</p>
<p align="center">
  <img src='https://img.shields.io/github/license/wiltonicp/vihacker-cloud' alt='License'/>
  <img src="https://img.shields.io/github/stars/wiltonicp/vihacker-cloud" alt="Stars"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-2.5.1-green" alt="SpringBoot"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud-2020.0.3-blue" alt="SpringCloud"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud%20Alibaba-2021.1-brightgreen" alt="Spring Cloud Alibaba"/>
</p>

## 如果您觉得有帮助，请点击右上角 "Star" 支持一下，谢谢！

VihackerCloud是一款基于Spring Cloud Alibaba的微服务架构。引入组件化的思想实现高内聚低耦合并且高度可配置化，简化使用流程。

#### 持续开发中......



#### 模块说明

| 模块名称                       | 说明                                             | 是否发布 |
| ------------------------------ | ------------------------------------------------ | -------- |
| vihacker-core                  | 父类，可用于快速新建项目                         | 是       |
| vihacker-dependencies-starter  | 依赖管理                                         | 是       |
| vihacker-common-starter        | 工具包                                           | 是       |
| vihacker-cloud-starter         | 快速新建SpringCloud                              | 是       |
| vihacker-datasource-starter    | 数据库配置，包含 mybatis-plus                    | 是       |
| vihacker-doc-starter           | 快速集成knife4j文档管理                          | 是       |
| vihacker-redis-starter         | 内置redis连接池、常用接口、分布式锁              | 是       |
| vihacker-web-starter           | web 项目                                         |          |
| vihacker-security-starter      | 封装 SpringSecurity，快速集成资源服务器          | 是       |
| vihacker-auth-starter          | Security相关工具类                               | 是       |
| vihacker-sentinel-starter      | *Sentinel* 熔断降级快速集成                      |          |
| vihacker-kafka-starter         | kafka 快速集成                                   |          |
| vihacker-log-starter           | 日志工具类，包含logback 输出和切面日志打印、存储 | 是       |
| vihacker-mail-starter          | 快速集成邮件，内置邮件接口                       | 是       |
| vihacker-feign-starter         | 快速集成 feign                                   |          |
| vihacker-mybatis-starter       | mybatis-plus封装                                 | 是       |
| vihacker-elasticsearch-starter | elasticsearch连接封装、常用接口、注解、使用方便  | 是       |

#### 使用说明

最新版本 `v1.0.4.R`版本已发布到 maven 中央仓库，可直接引入

```pom
<dependency>
    <groupId>cc.vihackerframework</groupId>
    <artifactId>模块名称</artifactId>
    <version>1.0.4.R</version>
</dependency>
```


### 关于我

**公众号:**【编程手记】

<img src="http://oss.vihacker.top/image/%E5%85%B3%E6%B3%A8%E6%88%91.png" alt="关注我" style="zoom: 50%;" />