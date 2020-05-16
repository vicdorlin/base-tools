package com.jmeng.basetools.tools;


import com.jmeng.basetools.utils.CommonUtil;
import com.jmeng.basetools.utils.EnumUtil;
import lombok.Getter;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * @author linss
 * @program toolkit-server
 * @create 2019-07-11 16:13
 */
public class DataMocker {
    private static DataMocker mocker;


    public DataMocker() {
    }

    /**
     * 返回一个porter
     *
     * @return
     */
    public static DataMocker newMocker() {
        if (mocker != null) {
            return mocker;
        }
        return new DataMocker();
    }

    /**
     * 为Bean填充值
     *
     * @param b
     * @param <B>
     * @return
     */
    public <B> B fillInBean(B b) {
        return fillInBean(b, true);
    }

    /**
     * 为Bean填充值
     *
     * @param b
     * @param recursionSupport
     * @param <B>
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <B> B fillInBean(B b, boolean recursionSupport) {
        if (b == null) {
            return b;
        }
        try {
            Class clazz = b.getClass();
            List<String> fieldNames = CommonUtil.extractFieldNames(clazz);
            if (CommonUtil.isSetEmpty(fieldNames)) {
                return b;
            }
            for (String fieldName : fieldNames) {
                PropertyDescriptor pd;
                try {
                    pd = new PropertyDescriptor(fieldName, clazz);
                } catch (IntrospectionException e) {
                    continue;
                }
                Method fieldReader = pd.getReadMethod();
                Object o = fieldReader.invoke(b);

                Method fieldSetter = pd.getWriteMethod();
                String type = pd.getPropertyType().getName();
                if(o != null) {
                    continue;
                }
                generateFiledValue(recursionSupport, b, fieldName, pd, fieldSetter, type);
            }
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成虚拟对象，默认支持递归生成
     * 有内存溢出风险，可在{@link this#createBean(Class, boolean)}中添加前置条件加以限制
     *
     * @param clazz
     * @param <B>
     * @return
     */
    public <B> B createBean(Class<B> clazz) {
        return createBean(clazz, true);
    }

    /**
     * 生成虚拟对象 使用者请自行判断是否不宜递归
     *
     * @param clazz
     * @param recursionSupport 是否打开递归，如传入true则针对非基本数据类型以及非常用类型字段进行递归虚拟
     * @param <B>
     * @return
     */
    public <B> B createBean(Class<B> clazz, boolean recursionSupport) {
        try {
            B b = clazz.newInstance();
            List<String> fieldNames = CommonUtil.extractFieldNames(clazz);
            if (CommonUtil.isSetEmpty(fieldNames)) {
                return b;
            }
            for (String fieldName : fieldNames) {
                PropertyDescriptor pd;
                try {
                    pd = new PropertyDescriptor(fieldName, clazz);
                } catch (IntrospectionException e) {
                    continue;
                }
                Method fieldSetter = pd.getWriteMethod();
                String type = pd.getPropertyType().getName();
                generateFiledValue(recursionSupport, b, fieldName, pd, fieldSetter, type);
            }
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private <B> void generateFiledValue(boolean recursionSupport, B b, String fieldName, PropertyDescriptor pd, Method fieldSetter, String type) throws IllegalAccessException, InvocationTargetException {
        Object value;
        if ("java.lang.String".equals(type)) {
            value = RandomValue.getInstance().defaultStringValue(fieldName);
        } else {
            FiledTypeEnum typeEnum = EnumUtil.valueOf(FiledTypeEnum.class, "type", type);
            if (typeEnum != null) {
                value = typeEnum.getValue();
            } else if (recursionSupport) {
                value = createBean(pd.getPropertyType(), recursionSupport);
            } else {
                value = null;
            }
        }
        fieldSetter.invoke(b, value);
    }

    @Getter
    private enum FiledTypeEnum {
//        FT_LONG_BASE("long", Long.valueOf(RandomValue.getInstance().getNum(1000000, 1000000000))),
        FT_LONG("java.lang.Long", Long.valueOf(RandomValue.getInstance().getNum(1000000, 1000000000))),
//        FT_INT("int", RandomValue.getInstance().getNum(1000, 100000)),
        FT_INTEGER("java.lang.Integer", RandomValue.getInstance().getNum(1000, 100000)),
        FT_DATE("java.util.Date", new Date()),
//        FT_BOOLEAN_BASE("boolean", Boolean.valueOf(RandomValue.getInstance().getNum(0, 1) > 0)),
        FT_BOOLEAN("java.lang.Boolean", Boolean.valueOf(RandomValue.getInstance().getNum(0, 1) > 0)),
//        FT_CHAR("char", Character.valueOf((char) (Math.random() * (Character.MAX_VALUE - Character.MIN_VALUE) + Character.MIN_VALUE))),
        FT_CHARACTER("java.lang.Character", Character.valueOf((char) (Math.random() * (Character.MAX_VALUE - Character.MIN_VALUE) + Character.MIN_VALUE))),
//        FT_BYTE_BASE("byte", (byte) RandomValue.getInstance().getNum(0, Byte.MAX_VALUE)),
        FT_BYTE("java.lang.Byte", (byte) RandomValue.getInstance().getNum(0, Byte.MAX_VALUE)),
//        FT_SHORT_BASE("short", Short.valueOf((short) RandomValue.getInstance().getNum(0, Short.MAX_VALUE))),
        FT_SHORT("java.lang.Short", Short.valueOf((short) RandomValue.getInstance().getNum(0, Short.MAX_VALUE))),
//        FT_DOUBLE_BASE("double", Double.valueOf(new DecimalFormat("#.00").format(Double.valueOf(Math.random())))),
        FT_DOUBLE("java.lang.Double", Double.valueOf(new DecimalFormat("#.00").format(Double.valueOf(Math.random())))),
//        FT_FLOAT_BASE("float", Float.valueOf(new DecimalFormat("#.00").format(new Float(Math.random())))),
        FT_FLOAT("java.lang.Float", Float.valueOf(new DecimalFormat("#.00").format(new Float(Math.random())))),
        FT_LIST("java.util.List", null);
        private String type;
        private Object value;

        private FiledTypeEnum(String type, Object value) {
            this.type = type;
            this.value = value;
        }
    }
}
