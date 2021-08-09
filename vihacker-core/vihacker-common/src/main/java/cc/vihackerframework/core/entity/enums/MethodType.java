package cc.vihackerframework.core.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 方法类型
 *
 * @author Ranger
 * @since 2021/7/29
 * @email wilton.icp@gmail.com
 */
@Getter
@AllArgsConstructor
public enum MethodType {

	/**
	 * 方法类型
	 * GET
	 * PUT
	 * POST
	 * DELETE
	 * OPTIONS
	 */
	GET(false),
	PUT(true),
	POST(true),
	DELETE(false),
	HEAD(false),
	OPTIONS(false);

	private final boolean hasContent;

}
