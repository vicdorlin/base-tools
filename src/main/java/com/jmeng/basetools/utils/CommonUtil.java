package com.jmeng.basetools.utils;


import com.alibaba.fastjson.JSON;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jmeng.enums.EDateFormat.DF_DEFAULT;

/**
 * @author vicdor
 * @create 2018-09-19 18:12
 */
public class CommonUtil {
    private static final int TWO = 2;

    /**
     * 判断对象是否不为空
     *
     * @param o
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean exist(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof String) {
            return ((String) o).trim().length() > 0;
        }
        if (o instanceof Collection) {
            return ((Collection) o).size() > 0;
        }
        if (o instanceof Map) {
            return ((Map) o).size() > 0;
        }
        if (o instanceof Object[]) {
            return ((Object[]) o).length > 0;
        }
        return true;
    }

    /**
     * 判断对象是否为空
     *
     * @param o
     * @return
     */
    public static boolean notExist(Object o) {
        return !exist(o);
    }

    /**
     * 判断所有参数对象是否都不为空
     *
     * @param objs
     * @return
     */
    public static boolean existAll(Object... objs) {
        if (objs == null || objs.length < 1) {
            return false;
        }
        for (Object obj : objs) {
            if (notExist(obj)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否是纯数字
     *
     * @param str
     * @return
     */
    public static boolean isStringNumber(String str) {
        if (notExist(str)) {
            return false;
        }
        return str.matches("^[0-9]*$");
    }

    /**
     * 判断字符串为空
     *
     * @param str
     * @return
     */
    public static boolean isStringEmpty(String str) {
        return (str == null) || (str.trim().length() == 0);
    }

    /**
     * 判断一个集合是否不存在非空子集
     *
     * @param collection
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isSetEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }

    /**
     * 判断一个集合是否存在非空子集
     *
     * @param collection
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isSetNotEmpty(Collection collection) {
        return !isSetEmpty(collection);
    }

    /**
     * 可以理解为万用toString
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String transferToString(T t) {
        if (t == null) {
            return null;
        }
        if (t instanceof String) {
            return (String) t;
        }
        if (t instanceof Date) {
            return new SimpleDateFormat(DF_DEFAULT.getValue()).format(t);
        }
        if (t instanceof Number || t instanceof Character || t instanceof Boolean) {
            return String.valueOf(t);
        }
        return JSON.toJSONString(t);
    }

    /**
     * 得到一个Bean类型的所有字段名
     *
     * @param clazz
     * @return
     */
    public static List<String> extractFieldNames(Class<?> clazz) {
        List<String> fieldNames = new ArrayList<String>();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields.length > 0) {
                for (Field field : fields) {
                    fieldNames.add(field.getName());
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fieldNames;
    }

    /**
     * 简要构建map
     *
     * @param objs
     * @return
     */
    public static Map<Object, Object> buildMap(Object... objs) {
        Map<Object, Object> map = new HashMap<>(objs.length / 2);
        for (int i = 0; i < objs.length; i += TWO) {
            map.put(objs[i], objs[i + 1]);
        }
        return map;
    }

    /**
     * Build search map by keywords. 要求参数数量必须为偶数数量 奇数位字符串为key,偶数位为其前位key对应之value
     *
     * @param keywords the keywords
     * @return the map
     */
    public static Map<String, Object> buildSoMap(String... keywords) {
        Map<String, Object> map = new HashMap<>(keywords.length / 2);
        for (int i = 0; i < keywords.length; i += TWO) {
            map.put(keywords[i], keywords[i + 1]);
        }
        return map;
    }

    /**
     * just like {@link #buildSoMap}
     *
     * @param keywords the keywords
     * @return the map
     */
    public static Map<String, String> buildSsMap(String... keywords) {
        Map<String, String> map = new HashMap<>(keywords.length / 2);
        for (int i = 0; i < keywords.length; i += TWO) {
            map.put(keywords[i], keywords[i + 1]);
        }
        return map;
    }

    /**
     * Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
     *
     * @param obj
     * @return
     */
    public static Map<String, String> transBeanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>(1);
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!"class".equals(key)) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (value != null) {
                        map.put(key, value.toString());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("transBeanToMap Error " + e);
        }
        return map;
    }

    /**
     * 将符合bean规范的对象按<字段名,属性值>转为Map
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> transBean2SoMap(Object bean) {
        if (bean == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>(1);
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                // 得到property对应的getter方法
                Method getter = property.getReadMethod();
                Object value = getter.invoke(bean);
                if (value == null) {
                    continue;
                }
                map.put(property.getName(), value);
            }
            map.remove("class");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 将符合bean规范的对象按<字段名,属性值>转为Map<String,String> value若为非常用引用类型，则转为json
     *
     * @param obj
     * @return
     */
    public static Map<String, String> transBean2SsMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>(1);
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                // 得到property对应的getter方法
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj);
                if (value == null) {
                    continue;
                }
                map.put(property.getName(), transferToString(value));
            }
            map.remove("class");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * Build search map by keywords. 要求参数数量必须为偶数数量 奇数位字符串为key,偶数位为其前位key对应之value
     *
     * @param keywords the keywords
     * @return the map
     */
    public static Map<String, Object> buildSearchMap(String... keywords) {
        Map<String, Object> map = new HashMap<>(1);
        for (int i = 0; i < keywords.length; i += TWO) {
            map.put(keywords[i], keywords[i + 1]);
        }
        return map;
    }

    /**
     * just like {@link #buildSearchMap}
     *
     * @param keywords the keywords
     * @return the map
     */
    public static Map<String, String> buildTestSearchMap(String... keywords) {
        Map<String, String> map = new HashMap<>(1);
        for (int i = 0; i < keywords.length; i += TWO) {
            map.put(keywords[i], keywords[i + 1]);
        }
        return map;
    }

    /**
     * 判断入参是否为数字
     *
     * @param obj
     * @return
     */
    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        return isStringNumber(obj.toString());
    }

    /**
     * 手机格式验证
     *
     * @param tel
     * @return
     */
    public static boolean isTel(String tel) {
        return tel.matches("^1[0-9][0-9]\\d{8}$");
    }

    /**
     * 返回非空字符串
     *
     * @param word
     * @return
     */
    public static String nullToEmptyString(String word) {
        return word != null ? word : "";
    }

    /**
     * List转String
     *
     * @param list
     * @param separator 分隔符
     * @return
     */
    public static String listToString(List<?> list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 为字符串打上一连串的码
     *
     * @param words  待操作字符串
     * @param mark   替换标识{如：*}
     * @param start  打码起始位置
     * @param length 打码长度
     * @return
     */
    public static String markCenter(String words, String mark, int start, int length) {
        String ex = "(?<=[\\S]{" + start + "})\\S(?=[\\S]{" + (words.length() - start - length) + "})";
        return words.replaceAll(ex, mark);
    }

    /**
     * 为字符串两边打码，仅留中间部分
     *
     * @param words         待操作字符串
     * @param mark          替换标识{如：*}
     * @param markNumBefore 前标识位数
     * @param markNumAfter  后标识位数
     * @return
     */
    public static String markSides(String words, String mark, int markNumBefore, int markNumAfter) {
        return markCenter(markCenter(words, mark, 0, markNumBefore), mark, words.length() - markNumAfter, markNumAfter);
    }

    /**
     * 返回当前页后是否有数据
     *
     * @param total 总记录数 一共多少条
     * @param page  页码 从1开始
     * @param size  每页记录条数 一页多少条
     * @Author Vicdor
     */
    public static Boolean haveNextPage(long total, int page, int size) {
        return page < (total % size > 0 ? total / size + 1 : total / size);
    }

    /**
     * 将json串转为指定类型的对象
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String jsonStr, Class<T> clazz) {
        if (notExist(jsonStr)) {
            return null;
        }
        return JSON.toJavaObject(JSON.parseObject(jsonStr), clazz);
    }

    /**
     * 拆散list
     *
     * @param list 原始list
     * @param max  拆散后每个list限制的最大size
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> unravelList(List<T> list, int max) {
        if (list == null || list.size() == 0 || max < 1) {
            return null;
        }
        List<List<T>> lists = new ArrayList<>();
        if (list.size() <= max) {
            lists.add(list);
            return lists;
        }
        int listSize = list.size() % max != 0 ? list.size() / max + 1 : list.size() / max;
        for (int i = 0; i < listSize; i++) {
            int toIndex = (i == listSize - 1) ? list.size() : (i + 1) * max;
            lists.add(list.subList(i * max, toIndex));
        }
        return lists;
    }

}
