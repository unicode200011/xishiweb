package com.stylefeng.guns.admin.modular.system.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.admin.common.exception.BizExceptionEnum;
import com.stylefeng.guns.persistence.dao.DictMapper;
import com.stylefeng.guns.persistence.model.Dict;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.admin.modular.system.dao.DictDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.stylefeng.guns.admin.common.constant.factory.MutiStrFactory.MUTI_STR_KEY;
import static com.stylefeng.guns.admin.common.constant.factory.MutiStrFactory.MUTI_STR_VALUE;
import static com.stylefeng.guns.admin.common.constant.factory.MutiStrFactory.parseKeyValue;

/**
 * 字典服务
 *
 * @author fengshuonan
 * @date 2017-04-27 17:00
 */
@Service
public class DictService {

    @Resource
    DictDao dictDao;

    @Resource
    DictMapper dictMapper;

    @Transactional
    public void addDict(String dictName, String dictValues) {
        //判断有没有该字典
        List<Dict> dicts = dictMapper.selectList(new EntityWrapper<Dict>().eq("name", dictName).and().eq("pid", 0));
        if(dicts != null && dicts.size() > 0){
            throw new GunsException(BizExceptionEnum.DICT_EXISTED);
        }

        //解析dictValues
        List<Map<String, String>> items = parseKeyValue(dictValues);

        //添加字典
        Dict dict = new Dict();
        dict.setName(dictName);
        dict.setNum(0);
        dict.setPid(0);
        this.dictMapper.insert(dict);

        //添加字典条目
        for (Map<String, String> item : items) {
            String num = item.get(MUTI_STR_KEY);
            String name = item.get(MUTI_STR_VALUE);
            Dict itemDict = new Dict();
            itemDict.setPid(dict.getId());
            itemDict.setName(name);
            try {
                itemDict.setNum(Integer.valueOf(num));
            }catch (NumberFormatException e){
                throw new GunsException(BizExceptionEnum.DICT_MUST_BE_NUMBER);
            }
            this.dictMapper.insert(itemDict);
        }
    }

    @Transactional
    public void editDict(Integer dictId, String dictName, String dicts) {
        //删除之前的字典
        this.delteDict(dictId);

        //重新添加新的字典
        this.addDict(dictName,dicts);
    }

    @Transactional
    public void delteDict(Integer dictId) {
        //删除这个字典的子词典
        Wrapper<Dict> dictEntityWrapper = new EntityWrapper<>();
        dictEntityWrapper = dictEntityWrapper.eq("pid", dictId);
        dictMapper.delete(dictEntityWrapper);

        //删除这个词典
        dictMapper.deleteById(dictId);
    }

}
