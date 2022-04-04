package cc.vihackerframework.core.datasource.starter.util;

import cc.vihackerframework.core.datasource.starter.entity.Search;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 分页工具类
 * Created by Ranger on 2022/02/20
 */
public class PageUtil {

	public static <T> IPage<T> getPage(Search search) {
		return new Page<T>(search.getCurrent(), search.getSize());
	}
}
