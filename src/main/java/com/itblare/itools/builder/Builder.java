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
 * 一句话功能简述：通用Builder 模式构建器
 *
 * @author Blare
 * @create 2021/4/12 13:42
 * @since 1.0.0
 */
public class Builder<T> {

    /**
     * JAVA8之函数式编程Supplier接口和Consumer接口。
     * Supplier接口，是一个供应商,提供者，可以作为生产工厂生产对象。
     * Consumer接口，是一个一个消费者，可以作为消费者去消费对象。
     * Supplier也是是用来创建对象的，但是不同于传统的创建对象语法：new
     * eg：Supplier<Object> sup= Object::new;
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

    public <P1, P2> Builder<T> with(Consumer2<T, P1, P2> consumer, P1 p1, P2 p2) {
        final Consumer<T> c = instance -> consumer.accept(instance, p1, p2);
        modifiers.add(c);
        return this;
    }

    public <P1, P2, P3> Builder<T> with(Consumer3<T, P1, P2, P3> consumer, P1 p1, P2 p2, P3 p3) {
        final Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3);
        modifiers.add(c);
        return this;
    }

    public T build() {
        final T value = instantiator.get();
        modifiers.forEach(modifier -> modifier.accept(value));
        modifiers.clear();
        return value;
    }

    /**
     * 1 参数 Consumer
     */
    @FunctionalInterface
    public interface Consumer1<T, P1> {
        void accept(T t, P1 p1);
    }

    /**
     * 2 参数 Consumer
     */
    @FunctionalInterface
    private interface Consumer2<T, P1, P2> {
        void accept(T t, P1 p1, P2 p2);
    }

    /**
     * 3 参数 Consumer
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
