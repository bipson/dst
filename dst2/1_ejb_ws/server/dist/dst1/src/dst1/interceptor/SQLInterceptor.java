package dst1.interceptor;

import java.util.regex.Pattern;

import org.hibernate.EmptyInterceptor;

public class SQLInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 3894614218727237142L;
	
	private static int count = 0;

	public String onPrepareStatement(String sql) {
		
		if (Pattern.compile(Pattern.quote("select"), Pattern.CASE_INSENSITIVE).matcher(sql).find())
			count++;

		return sql;
	}
	
	public static int getSelectCount() {
		return SQLInterceptor.count;
	}
	
	public static void resetSelectCount() {
		SQLInterceptor.count = 0;
	}

}
