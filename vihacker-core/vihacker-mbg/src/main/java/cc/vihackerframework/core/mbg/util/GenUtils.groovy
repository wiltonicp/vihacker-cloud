package cc.vihackerframework.core.mbg.util

import cc.vihackerframework.core.mbg.mapper.TableMapper
import cc.vihackerframework.core.mbg.model.ColumnEntity
import cc.vihackerframework.core.mbg.model.TableEntity
import org.apache.commons.configuration.Configuration
import org.apache.commons.configuration.PropertiesConfiguration
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.WordUtils
import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.codehaus.groovy.control.ConfigurationException

/**
 * Created by Ranger on 2022/3/30.
 */
class GenUtils {


    static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>()
        templates.add("templates/mybatis/Entity.java.vm")
        templates.add("templates/mybatis/Mapper.java.vm")
        templates.add("templates/mybatis/Mapper.xml.vm")
        templates.add("templates/mybatis/ViHackerService.java.vm")
        templates.add("templates/mybatis/ViHackerServiceImpl.java.vm")
        templates.add("templates/mybatis/ViHackerController.java.vm")
        templates.add("templates/dto/EntityInput.java.vm")
        return templates
    }

    /**
     * 获取配置信息
     */
    static Configuration getConfig() {
        try {
            new PropertiesConfiguration("generator.properties")
        } catch (ConfigurationException e) {
            throw new Exception("获取配置文件失败，", e)
        }
    }
    /**
     * 逆向生成代码
     * @param table
     * @param columns
     * @param config 配置信息
     */
    static void generatorCode(Map<String, String> table, List<Map<String, String>> columns, Configuration config, List<String> templates) {
        boolean hasBigDecimal = false
        boolean hasDate = false
        /**
         * 表信息
         */
        TableEntity tableEntity = new TableEntity(tableName: table.get("tableName"), comments: table.get("tableComment"))
        /**
         * 表名转换成Java类名
         */
        String className = tableToJava(tableEntity.tableName, config.getString("tablePrefix"))
        tableEntity.setClassName(className)
        tableEntity.setClassname(StringUtils.uncapitalize(className))
        /**
         * 列信息
         */
        List<ColumnEntity> columsList = new ArrayList<>()
        for (Map<String, String> column : columns) {
            ColumnEntity columnEntity = new ColumnEntity(
                    columnName: column.get("columnName"),
                    dataType: column.get("dataType"),
                    comments: column.get("columnComment").replace("表",""),
                    extra: column.get("extra"))
            /**
             * 列名转换成Java属性名
             */
            String attrName = columnToJava(columnEntity.getColumnName())
            columnEntity.setAttrName(attrName)
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName))
            /**
             * 列的数据类型，转换成Java类型
             */
            String attrType = config.getString(columnEntity.getDataType(), "unknowType")
            columnEntity.setAttrType(attrType)
            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true
            }
            if (!hasDate && attrType.equals("LocalDateTime")) {
                hasDate = true
            }
            /**
             * 是否主键
             */
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity)
            }
            columsList.add(columnEntity)
        }
        tableEntity.setColumns(columsList)
        /**
         * 没主键，则第一个字段为主键
         */
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0))
        }
        /**
         * 获取初始化模板引擎
         */
        VelocityEngine velocityEngine = new VelocityEngine()

        /**
         * 设置velocity资源加载器
         */
        Properties prop = new Properties()
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
        prop.put("velocimacro.library", "")
        velocityEngine.init(prop)
        String mainPath = config.getString("mainPath")
        mainPath = StringUtils.isBlank(mainPath) ? "cc.vihackerframework" : mainPath
        /**
         * 封装模板数据
         */
        Map<String, Object> map = new HashMap<>()
        map.put("tableName", tableEntity.getTableName())
        map.put("comments", tableEntity.getComments().replace("表", ""))
        map.put("pk", tableEntity.getPk())
        map.put("className", tableEntity.getClassName())
        map.put("classname", tableEntity.getClassname())
        map.put("pathName", tableEntity.className.toLowerCase())
        map.put("columns", tableEntity.getColumns())
        map.put("hasBigDecimal", hasBigDecimal)
        map.put("hasDate", hasDate)
        map.put("mainPath", mainPath)
        map.put("package", config.getString("package"))
        map.put("moduleName", config.getString("moduleName"))
        map.put("author", config.getString("author"))
        map.put("email", config.getString("email"))
        map.put("datetime", DateUtils.format(new Date()))
        map.put("version", "version")
        map.put("createdBy", "createdBy")
        map.put("createdTime", "createdTime")
        map.put("lastModifiedBy", "lastModifiedBy")
        map.put("lastModifiedTime", "lastModifiedTime")
        VelocityContext context = new VelocityContext(map)
        def disk = config.getString("disk")
        /**
         * 解析渲染模板
         */
        for (template in templates) {
            /**
             * 渲染模板
             */
            StringWriter sw = new StringWriter()
            Template tpl = velocityEngine.getTemplate(template, "UTF-8")
            tpl.merge(context, sw)
            try {
                String dirPath = getFileDir(template, config.getString("package"), config.getString("moduleName"))
                String fileName = getFileName(template, tableEntity.getClassName())
                if(fileName.contains("Service.java"))
                    fileName = "I" + fileName

                /**
                 * 添加输出信息
                 */
                disk = System.getProperty("user.dir")
                writerFile(disk + File.separator + dirPath, fileName, sw.toString())
                println(String.format("文件保存在%s%s%s", disk, File.separator, dirPath))
            } catch (IOException e) {
                throw new Exception("渲染模板失败，表名：" + tableEntity.getTableName(), e)
            }
        }

    }
    /**
     *  根据查询出的表列表和单元信息
     */
    static void generatorCode(Map<String, String> table,
                              List<Map<String, String>> columns) {
        /**
         * 配置信息
         */
        Configuration config = getConfig()

        /**
         * 默认模板列表
         */
        List<String> templates = getTemplates()
        generatorCode(table, columns, config, templates)
    }
    /**
     * 使用默认模板
     * @param table 表列表
     * @param columns 列信息
     * @param configuration 配置信息
     */
    static void generatorCode(Map<String, String> table,
                              List<Map<String, String>> columns, Configuration configuration) {
        /**
         * 默认模板列表
         */
        List<String> templates = getTemplates()
        generatorCode(table, columns, configuration, templates)
    }

    /**
     * 写文件
     * @param path
     * @param fileName
     * @param context
     * @return
     */
    static writerFile(String path, String fileName, String context) {
        def dir = new File(path)
        if (dir.exists()) {
            dir.delete()
        }
        dir.mkdirs()
        def file = new File(path + fileName)
        if (file.exists()) {
            file.delete()
        }
        def printWriter = file.newPrintWriter()
        printWriter.println(context)
        printWriter.flush()
        printWriter.close()
    }

    /**
     * 列名转换成Java属性名
     * @param columnName
     * @return
     */
    static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, "_".toCharArray()).replace("_", "")
    }

    /**
     * 表名转换成Java类名
     */
    static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "")
        }
        println(tableName)
        return columnToJava(tableName)
    }

    /**
     * 获取文件名
     */
    static String getFileName(String template, String className) {
        if (template.contains("Entity.java.vm")) {
            return className + ".java"
        }

        if (template.contains("Mapper.java.vm")) {
            return className + "Mapper.java"
        }

        if (template.contains("ViHackerService.java.vm")) {
            return className + "Service.java"
        }

        if (template.contains("ViHackerServiceImpl.java.vm")) {
            return className + "ServiceImpl.java"
        }

        if (template.contains("ViHackerController.java.vm")) {
            return className + "Controller.java"
        }

        if (template.contains("Mapper.xml.vm")) {
            return className + "Mapper.xml"
        }
        if (template.contains("list.html.vm")) {
            return className.toLowerCase() + ".html"
        }
        if (template.contains("list.js.vm")) {
            return className.toLowerCase() + ".js"
        }
        if (template.contains("EntityInput.java.vm")) {
            return className + "Input.java"
        }
        return null
    }

    /**
     * 获取文件目录
     */
    static String getFileDir(String template, String packageName, String moduleName) {
        String packagePath = "main" + File.separator + "java" + File.separator
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator
        }

        if (template.contains("Entity.java.vm")) {
            return packagePath + "entity" + File.separator
        }

        if (template.contains("Mapper.java.vm")) {
            return packagePath + "mapper" + File.separator
        }

        if (template.contains("ViHackerService.java.vm")) {
            return packagePath + "service" + File.separator
        }

        if (template.contains("ViHackerServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator
        }

        if (template.contains("ViHackerController.java.vm")) {
            return packagePath + "controller" + File.separator
        }

        if (template.contains("Mapper.xml.vm")) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator
        }
        if (template.contains("list.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "views" + File.separator + "modules" + File.separator + moduleName + File.separator
        }

        if (template.contains("list.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "views" + File.separator + "modules" + File.separator + moduleName + File.separator + "js" + File.separator
        }
        if (template.contains("EntityInput.java.vm")) {
            return packagePath + "dto" + File.separator + "input" + File.separator
        }
        return null
    }
    /**
     * 自动生成代码
     * @param tableMapper mybatis 接口实现
     * @param tableNames 表名
     * @param configuration 配置文件信息
     */
    static void generatorCode(TableMapper tableMapper, String[] tableNames, Configuration configuration) {
        for (String tableName : tableNames) {
            /**
             * 查询表信息
             */
            Map<String, String> table = tableMapper.queryTable(tableName)
            /**
             * 查询列信息
             */
            List<Map<String, String>> columns = tableMapper.queryColumns(tableName)

            /**
             * 生成代码
             */
            generatorCode(table, columns, configuration)
        }
    }

}
