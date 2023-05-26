package com.springboot.cric.requests.countries;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import com.springboot.cric.exceptions.BadRequestException;

@Data
@NoArgsConstructor
public class CreateRequest {
	private String name;

	public void validate() {
		if (StringUtils.isEmpty(name)) {
			throw new BadRequestException("Invalid name");
		}
	}
}