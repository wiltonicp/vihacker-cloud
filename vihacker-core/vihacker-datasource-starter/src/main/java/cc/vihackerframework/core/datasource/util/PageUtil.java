package cc.vihackerframework.core.datasource.util;

import cc.vihackerframework.core.datasource.entity.QuerySearch;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 分页工具类
 * Created by Ranger on 2022/02/20
 */
public class PageUtil {

	public static <T> IPage<T> getPage(QuerySearch querySearch) {
		return new Page<T>(querySearch.getCurrent(), querySearch.getSize());
	}
}
