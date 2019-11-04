/**
 * 
 */
package com.che.common.httpclient.juhe;

import lombok.Data;

/**
 * @author karlhell
 *
 */
@Data
public class JuheData {
	private String resultcode;
	private String reason;
	private String result;
	private String error_code;
}
