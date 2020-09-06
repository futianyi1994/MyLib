package com.bracks.mylib.base.basemvp;

/**
 * good programmer.
 *
 * @date : 2019-01-23 下午 03:39
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : Presenter工厂实现类
 */
public class PresenterFactoryImpl<P extends BasePresenter> implements PresenterFactory<P> {

    /**
     * 需要创建的Presenter的类型
     */
    private final Class<P> mPresenterClass;


    private PresenterFactoryImpl(Class<P> presenterClass) {
        this.mPresenterClass = presenterClass;
    }

    /**
     * 根据注解创建Presenter的工厂实现类
     *
     * @param viewClazz 需要创建Presenter的V层实现类
     * @param <P>       当前要创建的Presenter类型
     * @return 工厂类
     */
    public static <P extends BasePresenter> PresenterFactoryImpl<P> createFactory(Class<?> viewClazz) {
        CreatePresenter annotation = viewClazz.getAnnotation(CreatePresenter.class);
        Class<P> aClass = null;
        if (annotation != null) {
            aClass = (Class<P>) annotation.value();
        }
        if (aClass == null) {
            throw new RuntimeException("Presenter创建失败!，检查是否声明了@CreatePresenter(xx.class)注解");
        } else {
            return new PresenterFactoryImpl<>(aClass);
        }
    }

    @Override
    public P createPresenter() {
        try {
            return mPresenterClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Presenter创建失败!，检查是否声明了@CreatePresenter(xx.class)注解");
        }
    }
}