package com.miraclegenesis.framework.web.transform.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.miraclegenesis.framework.web.transform.TransformTypeConvert;

/**
 * @author robert
 */
public class PageTransformTypeConvert implements TransformTypeConvert<IPage<?>> {
        @Override
        // Object要求必须是集合或者是VO
        public Object convert(IPage<?> object) {
            return object.getRecords();
        }

}
