package com.springboot.cric.responses;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
	private long totalCount;
	private List<T> items;
	private int page;
	private int limit;
}