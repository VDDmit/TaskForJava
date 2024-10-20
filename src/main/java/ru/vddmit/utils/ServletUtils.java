package ru.vddmit.utils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

public class ServletUtils {

    @SuppressWarnings("unchecked")
    public static <T> T getServiceFromContext(ServletContext context, String attributeName, Class<T> serviceClass) throws ServletException {
        Object service = context.getAttribute(attributeName);
        if (service == null) {
            throw new ServletException(serviceClass.getSimpleName() + " not found in ServletContext");
        }
        return (T) service;
    }
}