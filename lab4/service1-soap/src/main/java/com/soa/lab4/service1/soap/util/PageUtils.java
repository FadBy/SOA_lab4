package com.soa.lab4.service1.soap.util;

import java.util.List;

public class PageUtils {
    public static <T> List<T> paginate(List<T> list, int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be >= 0");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Size must be >= 1");
        }

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, list.size());

        if (fromIndex >= list.size()) {
            return List.of();
        }

        return list.subList(fromIndex, toIndex);
    }
}
