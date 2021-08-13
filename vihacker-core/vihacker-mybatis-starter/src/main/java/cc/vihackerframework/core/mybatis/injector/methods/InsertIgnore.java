package cc.vihackerframework.core.mybatis.injector.methods;

import cc.vihackerframework.core.mybatis.injector.ViHackerSqlMethod;

/**
 * 插入一条数据（选择字段插入）插入如果中已经存在相同的记录，则忽略当前新数据
 *
 * @author Ranger
 * @since 2021/8/13
 * @email wilton.icp@gmail.com
 */
public class InsertIgnore extends AbstractInsertMethod {

	public InsertIgnore() {
		super(ViHackerSqlMethod.INSERT_IGNORE_ONE);
	}
}
