package pl.konradmaksymilian.turnbasedgames.core.dto;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

public class PageResponseDto<T> {

	private final List<T> content;
	
	private final Map<String, Sort.Direction> sort;
	
	private final int size;
	
	private final int page;
	
	private final int totalElements;

	public PageResponseDto(List<T> content, Map<String, Sort.Direction> sort, int size, int page,
			int totalElements) {
		this.content = content;
		this.sort = sort;
		this.size = size;
		this.page = page;
		this.totalElements = totalElements;
	}
	
	public List<T> getContent() {
		return content;
	}

	public Map<String, Sort.Direction> getSort() {
		return sort;
	}

	public int getSize() {
		return size;
	}

	public int getPage() {
		return page;
	}

	public int getTotalElements() {
		return totalElements;
	}
}
