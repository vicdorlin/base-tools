package com.jmeng.basetools.tools;

/**
 * 这个接口用来把F类型对象转换为T类型对象
 *
 * @author vicdor
 * @create 2017-11-14 19:06
 */
public interface IPorter<F, T> {
    /**
     * 实现一种由F到T的数据转换，具体落实到每个实现类
     *
     * @param f
     * @return
     */
    T transferF2T(F f);
}
