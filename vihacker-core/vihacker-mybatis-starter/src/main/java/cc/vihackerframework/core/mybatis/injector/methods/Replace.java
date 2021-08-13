package cc.vihackerframework.core.mybatis.injector.methods;

import cc.vihackerframework.core.mybatis.injector.ViHackerSqlMethod;

/**
 * 插入一条数据（选择字段插入）
 * <p>
 * 表示插入替换数据，需求表中有PrimaryKey，或者unique索引，如果数据库已经存在数据，则用新数据替换，如果没有数据效果则和insert into一样；
 * </p>
 *
 * @author Ranger
 * @since 2021/8/13
 * @email wilton.icp@gmail.com
 */
public class Replace extends AbstractInsertMethod {

	public Replace() {
		super(ViHackerSqlMethod.REPLACE_ONE);
	}
}

