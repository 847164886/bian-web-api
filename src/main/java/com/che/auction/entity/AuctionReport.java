/**
 * 文件名：AuctionReport.java<br/>
 * 创建时间：2016年6月29日 下午3:27:03<br/>
 * 创建者：Administrator<br/>
 * 修改者：暂无<br/>
 * 修改简述：暂无<br/>
 * 修改详述：
 * <p>
 * 暂无<br/>
 * </p>
 * 修改时间：暂无<br/>
 */
package com.che.auction.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * TODO 一句话描述类的主要作用<br/>
 * <p>
 * TODO 该类的详细描述<br/>
 * </p>
 * Time：2016年6月29日 下午3:27:03<br/>
 * 
 * @author Administrator
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class AuctionReport implements Serializable {

	private static final long serialVersionUID = 2334533177889852980L;
	private String content;
	private String material;
	private String mileage;
	private String repairDate;
	private String type;
}
