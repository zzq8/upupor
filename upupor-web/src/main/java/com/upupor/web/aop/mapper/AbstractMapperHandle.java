/*
 * <!--
 *   ~ MIT License
 *   ~
 *   ~ Copyright (c) 2021-2022 yangrunkang
 *   ~
 *   ~ Author: yangrunkang
 *   ~ Email: yangrunkang53@gmail.com
 *   ~
 *   ~ Permission is hereby granted, free of charge, to any person obtaining a copy
 *   ~ of this software and associated documentation files (the "Software"), to deal
 *   ~ in the Software without restriction, including without limitation the rights
 *   ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   ~ copies of the Software, and to permit persons to whom the Software is
 *   ~ furnished to do so, subject to the following conditions:
 *   ~
 *   ~ The above copyright notice and this permission notice shall be included in all
 *   ~ copies or substantial portions of the Software.
 *   ~
 *   ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   ~ SOFTWARE.
 *   -->
 */

package com.upupor.web.aop.mapper;

import com.upupor.service.dto.cache.CacheSensitiveWord;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author Yang Runkang (cruise)
 * @createTime 2022-04-29 20:23
 * @email: yangrunkang53@gmail.com
 */
public abstract class AbstractMapperHandle<T> {

    public abstract Boolean isHandle(Class<?> clazz);

    /**
     * 将不同场景的返回值转为List集合(向下兼容)
     *
     * @return
     */


    protected abstract void handle(T t);

    private CacheSensitiveWord cacheSensitiveWord;
    private List<?> proceedList;


    protected String replaceSensitiveWord(String target) {


        if (Objects.isNull(cacheSensitiveWord) || CollectionUtils.isEmpty(cacheSensitiveWord.getWordList())) {
            return target;
        }

        for (String sensitiveWord : cacheSensitiveWord.getWordList()) {
            if (target.contains(sensitiveWord)) {
                return target.replace(sensitiveWord, "[*敏感词*]");
            }
        }
        return target;
    }


    public void initData(List<?> proceedList, CacheSensitiveWord cacheSensitiveWord) {
        this.proceedList = proceedList;
        this.cacheSensitiveWord = cacheSensitiveWord;
    }

    public void sensitive() {
        for (Object proceed : proceedList) {
            handle((T) proceed);
        }
    }

}
