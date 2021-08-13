package cc.vihackerframework.core.mybatis.injector;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 扩展的自定义方法
 *
 * @author Ranger
 * @since 2021/8/13
 * @email wilton.icp@gmail.com
 */
@Getter
@AllArgsConstructor
public enum ViHackerSqlMethod {

	/**
	 * 插入如果中已经存在相同的记录，则忽略当前新数据
	 */
	INSERT_IGNORE_ONE("insertIgnore", "插入一条数据（选择字段插入）", "<script>\nINSERT IGNORE INTO %s %s VALUES %s\n</script>"),

	/**
	 * 表示插入替换数据，需求表中有PrimaryKey，或者unique索引，如果数据库已经存在数据，则用新数据替换，如果没有数据效果则和insert into一样；
	 */
	REPLACE_ONE("replace", "插入一条数据（选择字段插入）", "<script>\nREPLACE INTO %s %s VALUES %s\n</script>");

	private final String method;
	private final String desc;
	private final String sql;
}
