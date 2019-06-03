package com.bracks.mylib.base.basevm;

/**
 * good programmer.
 *
 * @date : 2019-02-15 上午 11:19
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :对BaseRepo的定位是将其当做一个接口调度器，其持有 BaseRemoteDataSource的实例并中转ViewModel的接口调用请求，并可以在BaseRepo分担一部分数据处理逻辑
 * 这样，ViewModel不关心接口的实际调用实现，方便以后更换BaseRemoteDataSource的实现方式，且将一部分的数据处理逻辑放到了BaseRepo，有利于逻辑的复用
 */
public class BaseRepo<T extends BaseDataSource> {

    protected T remoteDataSource;

    public BaseRepo(T remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

}