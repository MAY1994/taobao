package com.taobao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taobao.common.pojo.EasyUIDataGridResult;
import com.taobao.common.pojo.TaobaoResult;
import com.taobao.common.utils.JsonUtils;
import com.taobao.content.jedis.JedisClient;
import com.taobao.content.service.TbContentService;
import com.taobao.mapper.TbContentMapper;
import com.taobao.pojo.TbContent;
import com.taobao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TbContentServiceImpl implements TbContentService {

    @Autowired
    TbContentMapper tbContentMapper;

    @Autowired
    JedisClient jedisClient;

    @Value("${TB_CONTENT_KEY}")
    private String TB_CONTENT_KEY;

    @Override
    public EasyUIDataGridResult queryTbContentList(Long categoryId, Integer page, Integer rows) {
        page = (page == null ? 1 : page);
        rows = (rows == null ? 20 : rows);
        TbContentExample example = new TbContentExample();
        example.createCriteria().andCategoryIdEqualTo(categoryId);
        List<TbContent> tbContent = tbContentMapper.selectByExampleWithBLOBs(example);

        PageHelper.startPage(page, rows);
        PageInfo<TbContent> pageInfo = new PageInfo<>(tbContent);
        EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
        easyUIDataGridResult.setTotal((int) pageInfo.getTotal());
        easyUIDataGridResult.setRows(pageInfo.getList());

        return easyUIDataGridResult;
    }

    @Override
    public TaobaoResult insertTbContent(TbContent tbContent) {
        tbContent.setCreated(new Date());
        tbContent.setUpdated(tbContent.getCreated());
        tbContentMapper.insertSelective(tbContent);

        /*清空redis缓存*/
        try {
            System.out.println("添加了类别为" + tbContent.getCategoryId() + "的数据，已清空该类别redis缓存");
            jedisClient.hdel(TB_CONTENT_KEY, tbContent.getCategoryId() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaobaoResult.ok();
    }

    @Override
    public TaobaoResult updateTbContent(TbContent tbContent) {
        tbContent.setUpdated(new Date());
        tbContentMapper.updateByPrimaryKeyWithBLOBs(tbContent);

        /*清空redis缓存
        *
        *
        * */
        try {
            System.out.println("修改了类别为" + tbContent.getCategoryId() + "的数据，已清空该类别redis缓存");
            jedisClient.hdel(TB_CONTENT_KEY, tbContent.getCategoryId() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaobaoResult.ok();
    }

    @Override
    public TaobaoResult deleteTbContentById(Long... ids) {
        for (Long id : ids) {
            TbContent tbContent = tbContentMapper.selectByPrimaryKey(id);
            tbContentMapper.deleteByPrimaryKey(id);
            /*清空redis缓存*/
            try {
                jedisClient.hdel(TB_CONTENT_KEY, tbContent.getCategoryId() + "");
                System.out.println("删除了类别为" + id + "的数据，已清空该类别redis缓存");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TaobaoResult.ok();
    }

    @Override
    public List<TbContent> queryTbContentListByCatId(Long categoryId) {
        /*现在redis中查询该key有没有对应的缓存*/
        //若有则取出，返回
        try {
            String res = jedisClient.hget(TB_CONTENT_KEY, categoryId + "");
            if (StringUtils.isNotBlank(res)) {
                System.out.println("从redis缓存中取出");
                return JsonUtils.jsonToList(res, TbContent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("从mysql数据库中取出");
        //若没有则从mysql数据库中取出
        TbContentExample example = new TbContentExample();
        example.createCriteria().andCategoryIdEqualTo(categoryId);
        List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
        //往redis中存一份
        try {
            jedisClient.hset(TB_CONTENT_KEY, categoryId + "", JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


}
