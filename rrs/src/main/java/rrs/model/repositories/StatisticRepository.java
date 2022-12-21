package rrs.model.repositories;

public interface StatisticRepository {

	public Object execute(CUSTOM query, Object...params) throws Exception;
	public Object execute(S_ORDER proc, Object...params) throws Exception;
	
	public enum CUSTOM {
		LESS_ACCOUNT("EXEC PROC_LESS_ACCOUNT_BY_ROLE ?");

		private String value;
		private CUSTOM(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return this.value;
		}
	}
	
	public enum S_ORDER {
		MIN_MAX("SELECT * FROM VIEW_SO_RANGE"),
		/**
		 * PROC_SO_TIME: CONTENT UPLOAD BY TIME
		 *	<h3 style="color: red; text-align: center;">PHẢI TRUYỀN ĐỦ <u>5</u> THAM SỐ BẤT KỂ LÀ NULL</h3>
		 *	@Top số lượng rows | mặc định 1000
		 *	@start thời gian bắt đầu | mặc định min regTime
		 *	@end thời gian kết thúc | mặc định max regTime
		 *	@at Chọn theo 1(YEAR) | 2(MONTH) | 3(DAY)
		 *	@desc sắp xếp theo số lượng
		 */
		Q_M("{CALL PROC_SO_TIME(?, ?, ?, ?, ?)}"),// ?[3](@at) = 1 || 2 || 3
		

		/**
		 * 	PROC_SS_TIME: STATISTIC ORDER SOLD
		 *	<h3 style="color: red; text-align: center;">PHẢI TRUYỀN ĐỦ <u>6</u> THAM SỐ BẤT KỂ LÀ NULL</h3>
		 *	
		 *	@Top: số lượng rows | mặc định 1000
		 *	@at: GROUP THEO (1 - YEAR | 2 - MONTH | 3 - DAY)
		 *	@status: trạng thái đơn hàng (1 | 2 | 3 | 4)
		 *	@start: thời gian bắt đầu | mặc định min regTime
		 *	@end: thời gian kết thúc | mặc định max regTime
		 *	@desc: sắp xếp theo số lượng
		 */
		QT_M("{CALL PROC_SS_TIME(?, ?, ?, ?, ?, ?)}");
		// ?[1](@at) = 1 || 2 || 3
		// ?[2](@status) = 1 || 2 || 3 || 4
		

		private String value;
		S_ORDER(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
}
