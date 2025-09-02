package com.nanhang.lease.web.admin.custom.converter;

import com.nanhang.lease.model.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

/*首先继承ConverterFactory接口，两个泛型，第一个写前端传入的数据类型，第二个写需要转化成的目标泛型的父类（因为这个是可以转换成不同泛型的方法，当所有泛型有一个共同的父类的时候，
我们只需要根据Controller依靠@RequestParam注解传入的类型当作目标枚举类型的Class对象）参考下面的getConverter方法参数 */
@Component
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {
 /*
    */
    @Override
    /*
    * ，<T extends BaseEnum>：表示T必须是BaseEnum的子类型
    *  Converter<String, T>：表示返回的转换器的类型，前端传入的字符串转换成T类型
    * Class<T> targetType:是目标枚举类型的实际Class对象（获取了controller的RequestParam注解标注的需要转换成目标类型的class）
    * 可以把(Class<T> targetType)看成(数据类型1 string1),这里的class<T>就好比是由controller的RequestParam注解标注了，我们需要将前端获取的数据类型转换成数据类型1（这里假设数据类型1是BaseEnum的子类）
    * */
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {

        //使用匿名内部类
        return new Converter<String, T>() {
            @Override
            public T convert(String source) {
                //获取目标枚举类型的所有枚举常量（枚举常量：已经在枚举类型中定义好的常量，比如ItemType.APARTMENT）
                T[] enumConstants = targetType.getEnumConstants();
                //遍历目标枚举类型的所有枚举常量
                for (T enumConstant : enumConstants) {
                    //将前端传入的字符串转化为Integer类型，再获取遍历到的枚举常量的code值，两者做对比，遇到相同的，return出去
                    if (Integer.valueOf(source)==enumConstant.getCode()){
                        return enumConstant;
                    }
                }
                //如果传入的code泛型中没有对应的实体类就抛出异常
                throw  new IllegalArgumentException("code"+source+"非法");
            }
        };
    }
}
