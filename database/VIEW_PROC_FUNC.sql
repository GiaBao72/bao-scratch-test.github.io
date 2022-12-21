
USE [DSMT]
GO


-- +++++++++++++++++++++++++++++++++++ SIZE - MỐC THỜI GIAN ĐĂNG TẢI
IF EXISTS (SELECT name FROM sys.views WHERE name = 'VIEW_SO_RANGE')
	DROP VIEW VIEW_SO_RANGE
GO
CREATE VIEW VIEW_SO_RANGE AS
	SELECT 
		COUNT(id) as 'length',
		CASE 
			WHEN MIN(regTime) IS NULL THEN GETDATE() 
			ELSE MIN(regTime)
		END as 'st',
		CASE 
			WHEN MAX(regTime) IS NULL THEN GETDATE() 
			ELSE MAX(regTime) 
			END as 'et'
	FROM ORDERS
GO
SELECT * FROM VIEW_SO_RANGE

GO
-- ++++++++++++++++++++++++++++++++++++++++++++++ SELECT QUANTITY AND TOTAL OF PRODUCT BY STATUS
/*
	PROC_SS_TIME: STATISTIC ORDER SOLD
	
	@Top: số lượng rows | mặc định 1000
	@at: GROUP THEO (1 - YEAR | 2 - MONTH | 3 - DAY)
	@status: trạng thái đơn hàng (1 | 2 | 3 | 4)
	@start: thời gian bắt đầu | mặc định min regTime
	@end: thời gian kết thúc | mặc định max regTime
	@desc: sắp xếp theo số lượng
*/
IF EXISTS (SELECT name FROM sys.procedures WHERE name = 'PROC_SS_TIME')
	DROP PROCEDURE PROC_SS_TIME
GO
CREATE PROC PROC_SS_TIME
	@Top int, @at int, @status int, @start datetime, @end datetime, @desc bit
AS BEGIN 
	IF @start IS NULL SET @start = (SELECT st FROM VIEW_SO_RANGE)
	IF @end IS NULL SET @end = (SELECT et FROM VIEW_SO_RANGE)
	IF @status IS NULL SET @status = 3
	IF @at IS NULL SET @at = 2
	SET @at = @at*3

	-- SELECT INTO TEMPORARY TABLE
	SELECT 
		SUBSTRING(CONVERT(varchar(8), regTime, 2), 0 , @at) as 'month',
		SUM(d.quantity) as 'quantity',
		SUM(d.oldPrice) as 'total' INTO #TEMP
	FROM ORDER_DETAILS d
		INNER JOIN ORDER_STATUS s ON s.order_id=d.order_id
		INNER JOIN ORDERS o ON o.id = d.order_id
		WHERE regTime BETWEEN @start and @end AND s.status >= @status
	GROUP BY SUBSTRING(CONVERT(varchar(8), regTime, 2), 0 , @at)


	-- SELECT DATA TO RETURN
	IF @desc IS NULL SELECT TOP(ISNULL(@top, 1000)) * FROM #TEMP
	ELSE IF @desc=0 SELECT  TOP(ISNULL(@top, 1000)) * FROM #TEMP o ORDER BY o.total
	ELSE SELECT TOP(ISNULL(@top, 1000)) * FROM #TEMP o ORDER BY o.total DESC
END
GO
	-- @at IS NULL THEN SET @at default = 2
	-- @status IS NULL THEN SET @status default = 3
	EXEC PROC_SS_TIME NULL, 2, 2, '2021-6-1', NULL, NULL
GO


-- ++++++++++++++++++++++++++++++++++++++++++++++ SELECT ORDER BY TIME
/*
	PROC_SO_TIME: CONTENT UPLOAD BY TIME
	
	@Top: số lượng rows | mặc định 1000
	@start: thời gian bắt đầu | mặc định min regTime
	@end: thời gian kết thúc | mặc định max regTime
	@at: Chọn theo 1(YEAR) | 2(MONTH) | 3(DAY)
	@desc: sắp xếp theo số lượng
*/
IF EXISTS (SELECT name FROM sys.procedures WHERE name = 'PROC_SO_TIME')
	DROP PROCEDURE PROC_SO_TIME
GO
CREATE PROC PROC_SO_TIME
	@Top int, @start datetime, @end datetime, @at int, @desc bit 
AS BEGIN 
	IF @start IS NULL SET @start = (SELECT st FROM VIEW_SO_RANGE)
	IF @end IS NULL SET @end = (SELECT et FROM VIEW_SO_RANGE)
	DECLARE @CUT_AT TINYINT = 3*@at

	-- SELECT INTO TEMPORARY TABLE
	SELECT 
		SUBSTRING(CONVERT(varchar(8), regTime, 2), 0 , @CUT_AT) as 'month',
		SUM(d.quantity) as quantity INTO #TEMP
	FROM ORDERS o
		INNER JOIN ORDER_DETAILS d ON o.id=d.order_id
	WHERE regTime BETWEEN @start AND @end
	GROUP BY SUBSTRING(CONVERT(varchar(8), regTime, 2), 0 , @CUT_AT)


	-- SELECT DATA TO RETURN
	IF @desc IS NULL SELECT TOP(ISNULL(@top, 1000)) * FROM #TEMP
	ELSE IF @desc=0 SELECT  TOP(ISNULL(@top, 1000)) * FROM #TEMP o ORDER BY o.quantity
	ELSE SELECT TOP(ISNULL(@top, 1000)) * FROM #TEMP o ORDER BY o.quantity DESC
END
GO
EXEC PROC_SO_TIME 12, null, '2022', 2, null
GO

GO
-- ++++++++++++++++++++++++++++++++++++++++++++++ SELECT ACCOUNT BY ROLE ID
IF EXISTS (SELECT name FROM sys.procedures WHERE name = 'PROC_ACCOUNT_BY_ROLE')
	DROP PROC PROC_ACCOUNT_BY_ROLE
GO
CREATE PROCEDURE PROC_ACCOUNT_BY_ROLE
	@role_id varchar(10)
AS BEGIN 
	SELECT * FROM ACCOUNTS 
	WHERE username IN (
		SELECT account_id FROM AUTHORITIES
		WHERE role_id = @role_id
	)
END
GO
EXEC PROC_ACCOUNT_BY_ROLE 'SHIPPER'

-- ++++++++++++++++++++++++++++++++++++++++++++++ SELECT USERNAME, PASSWORD ACCOUNT BY ROLE ID
IF EXISTS (SELECT name FROM sys.procedures WHERE name = 'PROC_LESS_ACCOUNT_BY_ROLE')
	DROP PROC PROC_LESS_ACCOUNT_BY_ROLE
GO
CREATE PROCEDURE PROC_LESS_ACCOUNT_BY_ROLE
	@role_id varchar(10)
AS BEGIN 
	SELECT username, name 
		FROM ACCOUNTS a 
		INNER JOIN AUTHORITIES r
		ON a.username = r.account_id
	WHERE r.role_id = @role_id
END
GO
EXEC PROC_LESS_ACCOUNT_BY_ROLE 'BUYER'



-- ++++++++++++++++++++++++++++++++++++++++++++++ SELECT ORDER BY STATUS
IF EXISTS (SELECT name FROM sys.procedures WHERE name = 'PROC_ORDER_ORDERS')
	DROP PROC PROC_ORDER_ORDERS
GO
CREATE PROCEDURE PROC_ORDER_ORDERS
	@status int
AS BEGIN 
	IF @status IS NULL
		SELECT * FROM ORDERS 
		WHERE id NOT IN(SELECT order_id FROM ORDER_STATUS)
	ELSE SELECT * FROM ORDERS o 
		INNER JOIN ORDER_STATUS s
		ON s.order_id=o.id WHERE s.status = @status
END
GO
EXEC PROC_ORDER_ORDERS null


-- ++++++++++++++++++++++++++++++++++++++++++++++ SELECT ORDER BY ACCOUNT_ID
IF EXISTS (SELECT name FROM sys.procedures WHERE name = 'PROC_ORDER_STATUS_A')
	DROP PROC PROC_ORDER_STATUS_A
GO
CREATE PROCEDURE PROC_ORDER_STATUS_A
	@account_id varchar(20)
AS BEGIN 
	SELECT s.* FROM dbo.ORDER_STATUS s
	INNER JOIN ORDERS o ON o.id=s.order_id
	GROUP BY s.account_id, s.descript, s.order_id, s.status, o.account_id
	HAVING o.account_id = @account_id
END
GO
EXEC PROC_ORDER_STATUS_A 'buyer1'


GO
-- ++++++++++++++++++++++++++++++++++++++++++++++ SELECT ORDER BY PRODUCT_ID
IF EXISTS (SELECT name FROM sys.procedures WHERE name = 'PROC_ORDER_STATUS_P')
	DROP PROC PROC_ORDER_STATUS_P
GO
CREATE PROCEDURE PROC_ORDER_STATUS_P
	@product_id int
AS BEGIN 
	SELECT s.* FROM ORDERS o
	INNER JOIN ORDER_STATUS s ON s.order_id=o.id
	WHERE s.order_id IN (
		SELECT d.order_id FROM ORDER_DETAILS d
		WHERE d.product_id=@product_id
	) GROUP BY s.account_id, s.descript, s.order_id, s.status
END
GO
EXEC PROC_ORDER_STATUS_P 1

-- ++++++++++++++++++++++++++++++++++++++++++++++ SELECT DISCOUNT PRODUCT
IF EXISTS (SELECT name FROM sys.procedures WHERE name = 'PROC_DIS_PRODUCT')
	DROP PROC PROC_DIS_PRODUCT
GO
CREATE PROCEDURE PROC_DIS_PRODUCT
	@top int
AS BEGIN 
	SELECT TOP (ISNULL(@top, 1000)) p.*, SUM(o.quantity) as 'quantity'
	FROM PRODUCTS p INNER JOIN ORDER_DETAILS o
		ON p.id = o.product_id 
	GROUP BY id, cosPrice, proPrice, name, descript, category_id, account_id
	ORDER BY SUM(o.quantity) DESC
END
GO
EXEC PROC_DIS_PRODUCT 10

-- ++++++++++++++++++++++++++++++++++++++++++++++ SELECT TOP PRODUCT
IF EXISTS (SELECT name FROM sys.procedures WHERE name = 'PROC_TOP_PRODUCT')
	DROP PROC PROC_TOP_PRODUCT
GO
CREATE PROCEDURE PROC_TOP_PRODUCT
	@top int
AS BEGIN 
	SELECT TOP(ISNULL(@top, 1000)) p.*, COUNT(*) as 'quantity' FROM ORDER_DETAILS d
		INNER JOIN ORDERS o ON o.id = d.order_id
		INNER JOIN PRODUCTS p ON p.id = d.product_id
	GROUP BY p.id, cosPrice, proPrice, name, descript, category_id, p.account_id
	ORDER BY COUNT(*) DESC
END
GO
EXEC PROC_TOP_PRODUCT 10















