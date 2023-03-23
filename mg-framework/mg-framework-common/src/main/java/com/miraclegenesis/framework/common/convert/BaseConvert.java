package com.miraclegenesis.framework.common.convert;

import java.util.List;

/**
 * @author robert
 */

public interface BaseConvert<S, T> {

    /**
     * po转换为dto
     *
     * @param s po
     * @return dto
     */
    T toT(S s);

    /**
     * dto转换为po
     *
     * @param t dto
     * @return po
     */
    S toS(T t);

    /**
     * po列表转换为dto列表
     *
     * @param sList po列表
     * @return dto列表
     */
    List<T> toTList(List<S> sList);

    /**
     * dto列表转换为po列表
     *
     * @param tList dto列表
     * @return po列表
     */
    List<S> toSList(List<T> tList);
}
