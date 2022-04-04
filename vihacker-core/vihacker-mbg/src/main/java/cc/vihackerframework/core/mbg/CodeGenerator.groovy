package cc.vihackerframework.core.mbg

import cc.vihackerframework.core.mbg.mapper.TableMapper
import cc.vihackerframework.core.mbg.util.GenUtils
import org.apache.commons.configuration.PropertiesConfiguration
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder

/**
 * 自动生成代码
 * Created by Ranger on 2022/4/3.
 */
class CodeGenerator {

    /**
     * 需要自动生成代码的表名
     */
    static final def tableNames = ["t_dept"] as String[]

    /**
     * 开始生产代码
     * @param args
     */
    static void main(String[] args) {
        startProduction()
    }

    static void startProduction() {
        String resource = "mybatis-configuration.xml"
        InputStream inputStream = Resources.getResourceAsStream(resource)
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream)
        SqlSession session = sqlSessionFactory.openSession()
        def tableMapper = session.getMapper(TableMapper.class)

        /**
         * 生成代码
         */
        GenUtils.generatorCode(tableMapper, tableNames, new PropertiesConfiguration("generator.properties"))
    }
}
