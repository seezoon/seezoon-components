package com.seezoon.core.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * 带事务的service application/domain/都可以用
 *
 * 事务粒度过大，但是方便，对于高性能要求的项目不要在父类加
 *
 * @author hdf
 */
@Transactional(rollbackFor = Exception.class)
public abstract class AbstractTransactionService extends AbstractBaseService {

}
