package cc.vihackerframework.core.datasource.util;

import cc.vihackerframework.core.constant.ViHackerConstant;
import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.util.ViHackerUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;

/**
 * 处理排序工具类
 *
 * @author Ranger
 * @date: 2021/3/6 15:49
 * @email: wilton.icp@gmail.com
 */
public abstract class SortUtil {
    /**
     * 处理排序（分页情况下） for mybatis-plus
     *
     * @param request           QuerySearch
     * @param page              Page
     * @param defaultSort       默认排序的字段
     * @param defaultOrder      默认排序规则
     * @param camelToUnderscore 是否开启驼峰转下划线
     */
    public static void handlePageSort(QuerySearch request, Page<?> page, String defaultSort, String defaultOrder, boolean camelToUnderscore) {
        page.setCurrent(request.getCurrent());
        page.setSize(request.getSize());
        String sortField = request.getProp();
        if (camelToUnderscore) {
            sortField = ViHackerUtil.camelToUnderscore(sortField);
            defaultSort = ViHackerUtil.camelToUnderscore(defaultSort);
        }
        if (StringUtils.isNotBlank(request.getProp())
                && StringUtils.isNotBlank(request.getOrder())
                && !StringUtils.equalsIgnoreCase(request.getProp(), "null")
                && !StringUtils.equalsIgnoreCase(request.getOrder(), "null")) {
            if (StringUtils.equals(request.getOrder(), ViHackerConstant.ORDER_DESC)) {
                page.addOrder(OrderItem.desc(sortField));
            } else {
                page.addOrder(OrderItem.asc(sortField));
            }
        } else {
            if (StringUtils.isNotBlank(defaultSort)) {
                if (StringUtils.equals(defaultOrder, ViHackerConstant.ORDER_DESC)) {
                    page.addOrder(OrderItem.desc(defaultSort));
                } else {
                    page.addOrder(OrderItem.asc(defaultSort));
                }
            }
        }
    }

    /**
     * 处理排序 for mybatis-plus
     *
     * @param request QuerySearch
     * @param page    Page
     */
    public static void handlePageSort(QuerySearch request, Page<?> page) {
        handlePageSort(request, page, null, null, false);
    }

    /**
     * 处理排序 for mybatis-plus
     *
     * @param request           QuerySearch
     * @param page              Page
     * @param camelToUnderscore 是否开启驼峰转下划线
     */
    public static void handlePageSort(QuerySearch request, Page<?> page, boolean camelToUnderscore) {
        handlePageSort(request, page, null, null, camelToUnderscore);
    }

    /**
     * 处理排序 for mybatis-plus
     *
     * @param request           QuerySearch
     * @param wrapper           wrapper
     * @param defaultSort       默认排序的字段
     * @param defaultOrder      默认排序规则
     * @param camelToUnderscore 是否开启驼峰转下划线
     */
    public static void handleWrapperSort(QuerySearch request, QueryWrapper<?> wrapper, String defaultSort, String defaultOrder, boolean camelToUnderscore) {
        String sortField = request.getProp();
        if (camelToUnderscore) {
            sortField = ViHackerUtil.camelToUnderscore(sortField);
            defaultSort = ViHackerUtil.camelToUnderscore(defaultSort);
        }
        if (StringUtils.isNotBlank(request.getProp())
                && StringUtils.isNotBlank(request.getOrder())
                && !StringUtils.equalsIgnoreCase(request.getProp(), "null")
                && !StringUtils.equalsIgnoreCase(request.getOrder(), "null")) {
            if (StringUtils.equals(request.getOrder(), ViHackerConstant.ORDER_DESC)) {
                wrapper.orderByDesc(sortField);
            } else {
                wrapper.orderByAsc(sortField);
            }
        } else {
            if (StringUtils.isNotBlank(defaultSort)) {
                if (StringUtils.equals(defaultOrder, ViHackerConstant.ORDER_DESC)) {
                    wrapper.orderByDesc(defaultSort);
                } else {
                    wrapper.orderByAsc(defaultSort);
                }
            }
        }
    }

    /**
     * 处理排序 for mybatis-plus
     *
     * @param request QuerySearch
     * @param wrapper wrapper
     */
    public static void handleWrapperSort(QuerySearch request, QueryWrapper<?> wrapper) {
        handleWrapperSort(request, wrapper, null, null, false);
    }

    /**
     * 处理排序 for mybatis-plus
     *
     * @param request           QuerySearch
     * @param wrapper           wrapper
     * @param camelToUnderscore 是否开启驼峰转下划线
     */
    public static void handleWrapperSort(QuerySearch request, QueryWrapper<?> wrapper, boolean camelToUnderscore) {
        handleWrapperSort(request, wrapper, null, null, camelToUnderscore);
    }
}
