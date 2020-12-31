package com.moxi.mougblog.base.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * mapper 父类，注意这个类不要让 mybatis-plus 扫描到！！
 * @author 陌溪
 * @date 2020年12月31日21:32:33
 */
public interface SuperMapper<T> extends BaseMapper<T> {

    // 这里可以放一些公共的方法
}
