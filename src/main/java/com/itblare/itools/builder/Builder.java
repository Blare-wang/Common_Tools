package com.itblare.itools.builder;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.builder
 * ClassName:   Builder
 * Author:   Blare
 * Date:     Created in 2021/4/12 13:42
 * Description:    通用Builder 模式构建器
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/12 13:42    1.0.0             通用Builder 模式构建器
 */


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 通用Builder 模式构建器
 * Java8 引入 @FunctionalInterface 注解声明该接口是一个函数式接口。
 * Java8 之函数式编程Supplier接口和Consumer接口。
 * 1. Supplier接口，是一个供应商,提供者，可以作为生产工厂生产对象。
 * 提供一个抽象方法  T get(); ，可以通过get方法产生一个T类型实例。
 * Supplier也是是用来创建对象的，但是不同于传统的创建对象语法：new
 * 例如：Supplier<Object> sup= Object::new;
 * 2. Consumer接口，是一个一个消费者，可以作为消费者去消费对象。
 * 提供一个抽象方法 void accept(T t); 定义了要执行的具体操作。
 * 提供一个抽默认方法 default Consumer<T> andThen(Consumer after); ，接收Consumer类型参数，返回一个lambda表达式，
 * 此表达式定义了新的执行过程，先执行当前Consumer实例的accept方法，再执行入参传进来的Consumer实例的accept方法，这两个accept方法接收都是相同的入参t
 *
 * @author Blare
 * @create 2021/4/12 13:42
 * @since 1.0.0
 */
public class Builder<T> {

    /**
     * 实例化器
     */
    private final Supplier<T> instantiator;

    /**
     * 消费列表
     */
    private final List<Consumer<T>> modifiers = new ArrayList<>();

    public Builder(Supplier<T> instantiator) {
        this.instantiator = instantiator;
    }

    public static <T> Builder<T> of(Supplier<T> instantiator) {
        return new Builder<>(instantiator);
    }

    /**
     * 一个参数的构建
     *
     * @param consumer 构建执行方法对象
     * @param p1       参数1
     * @return {@link Builder<T>}
     * @method with
     * @date 2021/5/1 17:26
     */
    public <P1> Builder<T> with(Consumer1<T, P1> consumer, P1 p1) {
        /*final Consumer<T> c = new Consumer<T>() {
            @Override
            public void accept(T instance) {
                consumer.accept(instance, p1);
            }
        };*/
        Consumer<T> c = instance -> consumer.accept(instance, p1);
        modifiers.add(c);
        return this;
    }

    /**
     * 两个参数的构建对象
     *
     * @param consumer 构建执行方法
     * @param p1       参数1
     * @param p2       参数2
     * @return {@link Builder<T>}
     * @method with
     * @date 2021/5/1 17:25
     */
    public <P1, P2> Builder<T> with(Consumer2<T, P1, P2> consumer, P1 p1, P2 p2) {
        final Consumer<T> c = instance -> consumer.accept(instance, p1, p2);
        modifiers.add(c);
        return this;
    }

    /**
     * 三个参数的构建对象
     *
     * @param consumer 构建执行方法
     * @param p1       参数1
     * @param p2       参数2
     * @param p3       参数3
     * @return {@link Builder<T>}
     * @method with
     * @date 2021/5/1 17:21
     */
    public <P1, P2, P3> Builder<T> with(Consumer3<T, P1, P2, P3> consumer, P1 p1, P2 p2, P3 p3) {
        final Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3);
        modifiers.add(c);
        return this;
    }

    /**
     * 执行构建
     *
     * @return {@link T}
     * @method build
     * @date 2021/5/1 17:21
     */
    public T build() {
        final T value = instantiator.get();
        modifiers.forEach(modifier -> modifier.accept(value));
        modifiers.clear();
        return value;
    }

    /**
     * 1 个参数 Consumer accept执行
     */
    @FunctionalInterface
    public interface Consumer1<T, P1> {
        void accept(T t, P1 p1);
    }

    /**
     * 2 个参数 Consumer accept执行
     */
    @FunctionalInterface
    private interface Consumer2<T, P1, P2> {
        void accept(T t, P1 p1, P2 p2);
    }

    /**
     * 3 个参数 Consumer accept执行
     */
    @FunctionalInterface
    private interface Consumer3<T, P1, P2, P3> {
        void accept(T t, P1 p1, P2 p2, P3 p3);
    }

    public static void main(String[] args) {
        /*final StorageConfig build = Builder.of(StorageConfig::new)
            .with((v, n) -> {
                v.setAliyunConfig(n);
            }, new StorageConfig.AliyunConfig())
            .build();

        final StorageConfig build1 = Builder.of(StorageConfig::new)
            .with(StorageConfig::setAliyunConfig, new StorageConfig.AliyunConfig())
            .build();*/
    }
}