package com.thucjava.shopapp.utils;

import com.github.slugify.Slugify;

public class ConverterToSlug {
    public static String toSlug(String input) {
        Slugify slugify = new Slugify();
        return slugify.slugify(input);
    }
}
