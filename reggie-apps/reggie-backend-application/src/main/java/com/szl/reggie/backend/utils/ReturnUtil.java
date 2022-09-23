package com.szl.reggie.backend.utils;

import com.szl.reggie.base.R;

public class ReturnUtil {
    /**
     * 返回处理
     *
     * @param
     * @param msg
     * @return
     */
    public static  <T,O> R<T> returnR(O o, String msg) {
        if (o == null) {
            return R.error("服务异常");
        }
        if (o instanceof Boolean) {
            Boolean result = (Boolean) o;
            if (!result) {
                return R.error("服务异常");
            }

            return R.success((T)msg);
        }

        return R.success((T)o);
    }
}
