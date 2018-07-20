package pl.konradmaksymilian.turnbasedgames.core.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import pl.konradmaksymilian.turnbasedgames.core.dto.PageResponseDto;

public abstract class PageToDtoConverter<T1, T2> implements Converter<Page<T1>, PageResponseDto<T2>> {

	private Converter<T1, T2> entityToDtoConverter;
	
	public PageToDtoConverter(Converter<T1, T2> entityToDtoConverter) {
		this.entityToDtoConverter = entityToDtoConverter;
	}

	@Override
	public PageResponseDto<T2> convert(Page<T1> source) {
		var content = source.getContent().stream()
				.map(entity -> entityToDtoConverter.convert(entity))
				.collect(Collectors.toList());
		
		Map<String, Sort.Direction> sort;
		
		if (source.getSort().isSorted()) {
			sort = new HashMap<>();
			source.getSort().forEach(order -> sort.put(order.getProperty(), order.getDirection()));
		} else {
			sort = Collections.emptyMap();
		}
		
		return new PageResponseDto<T2>(content, sort, source.getSize(), source.getNumber(), 
				(int) source.getTotalElements());
	}
}
