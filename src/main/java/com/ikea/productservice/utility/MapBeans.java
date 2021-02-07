package com.ikea.productservice.utility;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ikea.productservice.exception.BeanMapperException;

public class MapBeans {
	private static final Logger LOG = LoggerFactory.getLogger(MapBeans.class);

	
	public static <T, S> T mapData(S source, Class<T> targetClass) {
		T dataMapped = null;
		long startTime = System.currentTimeMillis();
		try {
			dataMapped = targetClass.newInstance();
			if (source != null) {
				Field[] fieldsToMap = targetClass.getDeclaredFields();
				for (int fieldCount = 0; fieldCount < fieldsToMap.length; fieldCount++) {
					fieldsToMap[fieldCount].setAccessible(true);
					Field dataField = null;
					try {
						dataField = source.getClass().getDeclaredField(fieldsToMap[fieldCount].getName());
						dataField.setAccessible(true);

						if (dataField.get(source) != null) {
							LOG.debug("{}:{}", fieldsToMap[fieldCount], dataField.get(source));
							if (Modifier.isFinal(dataField.getModifiers())
									&& Modifier.isStatic(dataField.getModifiers())) {
								continue;
							}
							fieldsToMap[fieldCount].set(dataMapped, dataField.get(source));
						}
					} catch (NoSuchFieldException e) {
						LOG.debug("Field not present");
					}
				}
			} else {
				return null;
			}
		}  catch (SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException   ex) {
			throw new BeanMapperException(ex);
		}
		LOG.info("Time taken:{}", (System.currentTimeMillis() - startTime));
		return dataMapped;

	}

}