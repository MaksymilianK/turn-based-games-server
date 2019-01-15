package pl.konradmaksymilian.turnbasedgames.core.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import pl.konradmaksymilian.turnbasedgames.core.dto.PageResponseDto;

public interface ModelToDtoConverter<T1, T2> {

	T2 convert(T1 model);
	
	default PageResponseDto<T2> convertPage(Page<T1> page) {
		var content = page.getContent().stream()
				.map(entity -> convert(entity))
				.collect(Collectors.toList());
		
		Map<String, Sort.Direction> sort;
		
		if (page.getSort().isSorted()) {
			sort = new HashMap<>();
			page.getSort().forEach(order -> sort.put(order.getProperty(), order.getDirection()));
		} else {
			sort = Collections.emptyMap();
		}
		
		return new PageResponseDto<T2>(content, sort, page.getSize(), page.getNumber(), 
				(int) page.getTotalElements());
	}
}