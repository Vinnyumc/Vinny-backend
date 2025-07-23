package com.vinny.backend.Common.validator;

import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class ValidPageParamResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ValidPageParam.class)
                && parameter.getParameterType().equals(int.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        String pageParam = webRequest.getParameter("page");

        if (pageParam == null) {
            throw new GeneralException(ErrorStatus.MISSING_PAGE_PARAM);
        }

        try {
            int page = Integer.parseInt(pageParam);

            if (page < 1) {
                throw new GeneralException(ErrorStatus.INVALID_PAGE_PARAM);
            }

            return page - 1;

        } catch (NumberFormatException e) {
            throw new GeneralException(ErrorStatus.INVALID_PAGE_FORMAT);
        }
    }
}
