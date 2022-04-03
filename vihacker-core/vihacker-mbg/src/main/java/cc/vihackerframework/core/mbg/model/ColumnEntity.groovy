package cc.vihackerframework.core.mbg.model

/**
 * Created by Ranger on 2022/3/29.
 */
class ColumnEntity {

    /**
     * 列名
     */
    def  columnName
    /**
     * 列名类型
     */
    def  dataType
    /**
     * 列名备注
     */
    def  comments
    /**
     * 属性名称(第一个字母大写)，如：user_name => UserName
     */
    def  attrName
    /**
     * 属性名称(第一个字母小写)，如：user_name => userName
     */
    def  attrname
    /**
     * 属性类型
     */
    def  attrType
    /**
     * auto_increment
     */
    def  extra
}
