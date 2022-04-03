package cc.vihackerframework.core.mbg.model

/**
 * Created by Ranger on 2022/3/30.
 */
class TableEntity {

    /**
     * 表的名称
     */
    def tableName;
    /**
     * 表的备注
     */
    def comments;
    /**
     * 表的主键
     */
    def  pk;
    /**
     * 表的列名(不包含主键)
     */
    def columns;
    /**
     * 类名(第一个字母大写)，如：sys_user => SysUser
     */
    def className;
    /**
     * 类名(第一个字母小写)，如：sys_user => sysUser
     */
    def classname;
}
