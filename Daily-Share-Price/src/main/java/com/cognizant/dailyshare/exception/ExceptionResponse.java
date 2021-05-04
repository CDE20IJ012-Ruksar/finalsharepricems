package com.cognizant.dailyshare.exception;
/**
 * @author Ruksar, Revathi, Rameswara, Prachi
 */
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExceptionResponse {
	private Date timeStamp;
	private String details;
	private String message;
}
