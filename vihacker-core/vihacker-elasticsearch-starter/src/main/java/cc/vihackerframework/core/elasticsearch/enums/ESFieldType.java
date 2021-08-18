package cc.vihackerframework.core.elasticsearch.enums;

/**
 * @author Ranger
 * @since 2021/5/2
 * @email wilton.icp@gmail.com
 */
public enum ESFieldType {

	Text("text"),

	Byte("byte"),

	Short("short"),

	Integer("integer"),

	Long("long"),

	Date("date"),

	Float("float"),

	Double("double"),

	Boolean("boolean"),

	Object("object"),

	Keyword("keyword");


	ESFieldType(String typeName) {
		this.typeName = typeName;
	}

	public String typeName;
}
