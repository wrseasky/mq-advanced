package com.littlersmall.rabbitmqaccess.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *   Lombok 注解在线帮助文档：http://projectlombok.org/features/index.
 下面介绍几个我常用的 lombok 注解：
 @Data   ：注解在类上；提供类所有属性的 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法
 @Setter：注解在属性上；为属性提供 setting 方法
 @Getter：注解在属性上；为属性提供 getting 方法
 @Log4j ：注解在类上；为类提供一个 属性名为log 的 log4j 日志对象
 @NoArgsConstructor：注解在类上；为类提供一个无参的构造方法
 @AllArgsConstructor：注解在类上；为类提供一个全参的构造方法
 */
//统一返回值,可描述失败细节
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailRes {
	
    boolean isSuccess;
    String errMsg;
    
    
	public DetailRes(boolean isSuccess, String errMsg) {
		super();
		this.isSuccess = isSuccess;
		this.errMsg = errMsg;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
    
}
