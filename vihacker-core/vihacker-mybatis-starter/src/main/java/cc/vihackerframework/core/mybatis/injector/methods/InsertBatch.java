package cc.vihackerframework.core.mybatis.injector.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;

/**
 * 批量插入数据
 *
 * @author Ranger
 * @since 2021/8/13
 * @email wilton.icp@gmail.com
 */
public class InsertBatch extends AbstractInsertBatch {
	private static final String SQL_METHOD = "insertBatch";

	public InsertBatch() {
		super(SqlMethod.INSERT_ONE.getSql(), SQL_METHOD);
	}
}
