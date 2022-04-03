package cc.vihackerframework.core.mbg.mapper

/**
 * Created by Ranger on 2022/3/30.
 */
interface TableMapper {

    /**
     * 查询表详情信息
     * @param tableName
     * @return
     */
    Map<String, String> queryTable(String tableName);

    /**
     *  查询表结构
     * @param tableName
     * @return
     */
    List<Map<String, String>> queryColumns(String tableName);
}