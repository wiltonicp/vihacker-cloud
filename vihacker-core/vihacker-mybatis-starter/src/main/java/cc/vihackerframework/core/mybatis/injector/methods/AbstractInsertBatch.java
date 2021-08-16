package cc.vihackerframework.core.mybatis.injector.methods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽象的批量插入
 *
 * @author Ranger
 * @since 2021/8/13
 * @email wilton.icp@gmail.com
 */
@RequiredArgsConstructor
public class AbstractInsertBatch extends AbstractMethod {
	private final String sqlTemp;
	private final String sqlMethod;

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		KeyGenerator keyGenerator = new NoKeyGenerator();
		String keyProperty = null;
		String keyColumn = null;
		// 表包含主键处理逻辑,如果不包含主键当普通字段处理
		if (StringUtils.hasText(tableInfo.getKeyProperty())) {
			if (tableInfo.getIdType() == IdType.AUTO) {
				/** 自增主键 */
				keyGenerator = new Jdbc3KeyGenerator();
				keyProperty = tableInfo.getKeyProperty();
				keyColumn = tableInfo.getKeyColumn();
			} else {
				if (null != tableInfo.getKeySequence()) {
					keyGenerator = TableInfoHelper.genKeyGenerator(sqlMethod, tableInfo, this.builderAssistant);
					keyProperty = tableInfo.getKeyProperty();
					keyColumn = tableInfo.getKeyColumn();
				}
			}
		}
		// 所以字段
		String columnScript = SqlScriptUtils.convertTrim(getAllInsertSqlColumnMaybeIf(tableInfo), LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
		// 单条 sql 的脚本
		String valuesScript = SqlScriptUtils.convertTrim(getAllInsertSqlPropertyMaybeIf(tableInfo),
			LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
		// 列表 sql
		String valuesScripts = SqlScriptUtils.convertForeach(valuesScript, COLLECTION, null, ENTITY, COMMA);
		String sql = String.format(sqlTemp, tableInfo.getTableName(), columnScript, valuesScripts);
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		return this.addInsertMappedStatement(mapperClass, modelClass, sqlMethod, sqlSource, keyGenerator, keyProperty, keyColumn);
	}

	/**
	 * 获取 insert 时候字段 sql 脚本片段
	 * <p>insert into table (字段) values (值)</p>
	 * <p>位于 "字段" 部位</p>
	 *
	 * <li> 自动选部位,根据规则会生成 if 标签 </li>
	 *
	 * @return sql 脚本片段
	 */
	private static String getAllInsertSqlColumnMaybeIf(TableInfo tableInfo) {
		List<TableFieldInfo> fieldList = tableInfo.getFieldList();
		return tableInfo.getKeyInsertSqlColumn(true) + fieldList.stream().map(AbstractInsertBatch::getInsertSqlColumnMaybeIf)
			.collect(Collectors.joining(NEWLINE));
	}

	/**
	 * 获取 insert 时候字段 sql 脚本片段
	 * <p>insert into table (字段) values (值)</p>
	 * <p>位于 "字段" 部位</p>
	 *
	 * <li> 根据规则会生成 if 标签 </li>
	 *
	 * @return sql 脚本片段
	 */
	private static String getInsertSqlColumnMaybeIf(TableFieldInfo tableFieldInfo) {
		return tableFieldInfo.getInsertSqlColumn();
	}

	/**
	 * 获取所有 insert 时候插入值 sql 脚本片段
	 * <p>insert into table (字段) values (值)</p>
	 * <p>位于 "值" 部位</p>
	 *
	 * <li> 自动选部位,根据规则会生成 if 标签 </li>
	 *
	 * @return sql 脚本片段
	 */
	private static String getAllInsertSqlPropertyMaybeIf(final TableInfo tableInfo) {
		List<TableFieldInfo> fieldList = tableInfo.getFieldList();
		return tableInfo.getKeyInsertSqlProperty(ENTITY_DOT, true) + fieldList.stream()
			.map(AbstractInsertBatch::getInsertSqlPropertyMaybeIf)
			.collect(Collectors.joining(NEWLINE));
	}

	/**
	 * 获取 insert 时候插入值 sql 脚本片段
	 * <p>insert into table (字段) values (值)</p>
	 * <p>位于 "值" 部位</p>
	 *
	 * <li> 根据规则会生成 if 标签 </li>
	 *
	 * @return sql 脚本片段
	 */
	private static String getInsertSqlPropertyMaybeIf(TableFieldInfo tableFieldInfo) {
		return tableFieldInfo.getInsertSqlProperty(ENTITY_DOT);
	}
}
