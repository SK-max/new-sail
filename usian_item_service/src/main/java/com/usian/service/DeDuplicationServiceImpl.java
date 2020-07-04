package com.usian.service;

import com.usian.mapper.DeDuplicationMapper;
import com.usian.pojo.DeDuplication;
import com.usian.pojo.DeDuplicationExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DeDuplicationServiceImpl implements DeDuplicationService {

    @Autowired
    private DeDuplicationMapper deDuplicationMapper;

    @Override
    public DeDuplication selectItemDuplicationByTxNo(String txNo) {
        DeDuplicationExample example = new DeDuplicationExample();
        DeDuplicationExample.Criteria criteria = example.createCriteria();
        criteria.andTxNoEqualTo(txNo);
        List<DeDuplication> list = deDuplicationMapper.selectByExample(example);
        return list.get(0);
    }

    @Override
    public void insertDeDuplication(DeDuplication deDuplication) {
        deDuplicationMapper.insertSelective(deDuplication);
    }
}
