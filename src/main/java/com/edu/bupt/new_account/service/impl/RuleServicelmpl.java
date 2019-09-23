package com.edu.bupt.new_account.service.impl;

import com.edu.bupt.new_account.model.*;
import com.edu.bupt.new_account.dao.*;
import com.edu.bupt.new_account.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import okhttp3.*;
@Service
public class RuleServicelmpl implements RuleService {

    @Autowired
    private FilterMapper filterMapper;

    @Autowired
    private Rule2FilterMapper rule2FilterMapper;

    @Autowired
    private Rule2TransFormMapper rule2TransFormMapper;

    @Autowired
    private RuleMapper ruleMapper;

    @Autowired
    private TransformMapper transformMapper;

    @Override
    public List<Rule> getBindedRules(String gateId){
        return ruleMapper.getBindedRules(gateId);
    }

    @Override
    public List<Rule2FilterKey> getBindedR2F(Integer ruleId){
        return rule2FilterMapper.getBindedR2F(ruleId);
    }

    @Override
    public Filter getBindedFilter(Integer filterId){
        return filterMapper.selectByPrimaryKey(filterId);
    }

    @Override
    public List<Rule2TransFormKey> getBindedR2T(Integer ruleId){
        return rule2TransFormMapper.getBindedR2T(ruleId);
    }

    @Override
    public Transform getBindedTransform(Integer transformId){
        return transformMapper.selectByPrimaryKey(transformId);
    }

    @Override
    public void unbindR2F(Rule2FilterKey R2F){
        rule2FilterMapper.deleteByPrimaryKey(R2F);
    }

    @Override
    public void unbindFilter(Integer filterId){
        filterMapper.deleteByPrimaryKey(filterId);
    }

    @Override
    public void unbindR2T(Rule2TransFormKey R2T){
        rule2TransFormMapper.deleteByPrimaryKey(R2T);
    }

    @Override
    public void unbindTransform(Integer transformId){
        transformMapper.deleteByPrimaryKey(transformId);
    }

    @Override
    public void unbindRule(Integer ruleId){
        ruleMapper.deleteByPrimaryKey(ruleId);
    }

    @Override
    public void deleteBindedRule(Integer ruleId){
        String DELETE_RULE_URL = String.format("https://smart.gantch.cn/api/v1/smartruler/remove/%d",ruleId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(DELETE_RULE_URL)
                .delete()
                .build();
        try{
            okhttp3.Response res = client.newCall(request).execute();
            if(res.isSuccessful()){
                System.out.println("规则删除成功!");
            }
            else if(res.code()==401){
                System.out.println("没有权限!");
            }
            else if(res.code()==403){
                System.out.println("请求被禁止!");
            }
            else if(res.code()==204){
                System.out.println("没有可以被删除的内容!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
