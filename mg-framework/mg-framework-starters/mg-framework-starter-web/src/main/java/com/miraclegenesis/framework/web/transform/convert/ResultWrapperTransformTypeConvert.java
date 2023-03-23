package com.miraclegenesis.framework.web.transform.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miraclegenesis.framework.common.result.ResultWrapper;
import com.miraclegenesis.framework.web.transform.TransformTypeConvert;

/**
 * @author robert
 */
public class ResultWrapperTransformTypeConvert implements TransformTypeConvert<ResultWrapper<?>> {

    @Override
    public Object convert(ResultWrapper<?> object) {
        Object data = object.getData();
        //如果是page则拿到data 再返回
        if (data instanceof Page) {
            return ((Page<?>) data).getRecords();
        }
        return data;
    }
}
