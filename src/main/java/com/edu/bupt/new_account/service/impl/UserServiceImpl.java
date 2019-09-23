package com.edu.bupt.new_account.service.impl;

import com.edu.bupt.new_account.dao.*;
import com.edu.bupt.new_account.model.*;
import com.edu.bupt.new_account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import okhttp3.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RelationMapper relationMapper;

    @Autowired
    private DeviceTokenRelationMapper deviceTokenRelationMapper;

    @Override
    public Tenant findTenantByNameAndPasswd(String tenantName, String passwd) {
        Tenant tenant = new Tenant();
        tenant.setTenantName(tenantName);
        tenant.setPassword(passwd);
        return tenantMapper.selectByNameAndPassword(tenant);
    }

    @Override
    public User findUserByemail(String email) {return userMapper.selectByemail(email);}

    @Override
    public Integer saveUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public User findUserByOpenid(String openid) {
        return userMapper.selectByOpenid(openid);
    }

    @Override
    public void updateUserInfo(User user) {
        userMapper.updateByUser(user);
    }

    @Override
    public List<User> findAllUser() {
        return userMapper.searchAllUser();
    }

    @Override
    public User findUserByphone(String phone) {
        return userMapper.selectByPhone(phone);
    }

    @Override
    public void saveRelation(Relation relation) {
        relationMapper.insert(relation);
    }

    @Override
    public List<Relation> getBindedRelations(int bindedId) {
        return relationMapper.getBindedRelations(bindedId);
    }

    @Override
    public Relation findRelationByBinderAndBinded(int binderId, int bindedId) {
        Relation re = new Relation();
        re.setBinder(binderId);
        re.setBinded(bindedId);
        return relationMapper.getRelationBy2Bind(re);
    }

    @Override
    public void unbind(Integer id) {
        relationMapper.deleteByPrimaryKey(id);
    }



    @Override
    public User findUserById(Integer binded) {
        return userMapper.selectByPrimaryKey(binded);
    }

    @Override
    public List<Relation> findRelationsByBinderID(int binderId) {
        return relationMapper.getRelationsByBinderId(binderId);
    }

    @Override
    public void updateRelation(Relation re) {
        relationMapper.updateByPrimaryKey(re);
    }

    public void deleteUserById(Integer id){
        userMapper.deleteById(id);
    }

    public Boolean is_shared(String old_gatewayids, String new_gatewayids){
        String [] gatewayids = new_gatewayids.split(",");
        for (String gatewayid: gatewayids){
            if (old_gatewayids.contains(gatewayid)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteDeviceByGateId(String gateId){
        String GET_DEVICE_URL = String.format("https://smart.gantch.cn/api/v1/deviceaccess/parentdevices/%s?limit=1000",gateId);
        OkHttpClient client = new OkHttpClient();
        List<String> devices = new ArrayList<String>();
        Request request = new Request.Builder()
                .url(GET_DEVICE_URL)
                .get()
                .build();
        try{

            Response res = client.newCall(request).execute();
            if(res.isSuccessful()){
                //解析返回结果
                String result = res.body().string();
                JSONArray jarr = JSONArray.parseArray(result);
                for(Iterator iterator = jarr.iterator(); iterator.hasNext();){
                    JSONObject job = (JSONObject) iterator.next();
                    String deviceId = job.getString("id");
                    devices.add(deviceId);//获取网关所绑定的设备列表
                    deviceTokenRelationMapper.deleteByUuid(deviceId);
                    String DELETE_DEVICE_URL = String.format("https://smart.gantch.cn/api/v1/device/deleteDevice/%s",deviceId);
                    Request request2 = new Request.Builder()
                            .url(DELETE_DEVICE_URL)
                            .delete()
                            .build();
                    Response res2 = client.newCall(request2).execute();
                    if(res2.isSuccessful()){
                        System.out.format("设备：%s成功删除",deviceId);
                    }
                    else if(res2.code()==401){
                        System.out.println("没有权限");
                    }
                    else if(res2.code()==403){
                        System.out.println("请求被禁止");
                    }
                    else if(res2.code()==204){
                        System.out.println("没有内容");
                    }
                }
            }
            else if(res.code()==401){
                System.out.println("没有权限");
            }
            else if(res.code()==403){
                System.out.println("请求被禁止");
            }
            else if(res.code()==404){
                System.out.println("没有搜索到请求的资源");
            }

            //deviceTokenRelationMapper.deleteByUuid(gateId);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteShareByCustomerId(String customerId,String gateId){
        JSONObject data = new JSONObject();
        data.put("customerid",customerId);
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String GET_SHARE_URL = "https://smart.gantch.cn/api/v1/account/getGates";
        OkHttpClient client = new okhttp3.OkHttpClient();
        RequestBody body = RequestBody.create(JSON,data.toJSONString());
        Request request = new Request.Builder()
                .url(GET_SHARE_URL)
                .post(body)
                .build();
        try{
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                JSONObject json = JSONObject.parseObject(result);
                JSONArray jarr = json.getJSONArray("data");
                for(Iterator iterator = jarr.iterator(); iterator.hasNext();){
                    JSONObject dataItem = (JSONObject) iterator.next();
                    String sharedGateId = dataItem.getString("gates");
                    if(sharedGateId==gateId){//如果分享给对方的网关是当前正在解绑的网关，就将该分享删除
                        String DELETE_SHARE_URL = "https://smart.gantch.cn/api/v1/account/unBindedALLGate";
                        JSONObject data2 = new JSONObject();
                        data2.put("customerid",customerId);
                        data2.put("gateid",gateId);
                        RequestBody body2 = RequestBody.create(JSON,data2.toJSONString());
                        Request request2 = new Request.Builder()
                                .url(DELETE_SHARE_URL)
                                .post(body2)
                                .build();
                        Response response2 = client.newCall(request2).execute();
                        if(response2.isSuccessful()){
                            System.out.format("分享删除成功，分享对象为：%s",customerId);
                        }
                        else if(response2.code()==401){
                            System.out.println("没有权限");
                        }
                        else if(response2.code()==403){
                            System.out.println("请求被禁止");
                        }
                        else if(response2.code()==204){
                            System.out.println("没有内容");
                        }
                    }
                }
            }
            else if(response.code()==401){
                System.out.println("没有权限");
            }
            else if(response.code()==403){
                System.out.println("请求被禁止");
            }
            else if(response.code()==204){
                System.out.println("没有内容");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
