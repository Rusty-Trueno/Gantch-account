package com.edu.bupt.new_account.service;

import com.edu.bupt.new_account.model.Relation;
import com.edu.bupt.new_account.model.Tenant;
import com.edu.bupt.new_account.model.User;

import java.util.List;

public interface UserService {

    Tenant findTenantByNameAndPasswd(String tenantName, String passwd);

    Integer saveUser(User user);

    User findUserByOpenid(String openid);

    User findUserByemail(String email);

    void updateUserInfo(User user);

    List<User> findAllUser();

    User findUserByphone(String phone);

    void saveRelation(Relation relation);

    List<Relation> getBindedRelations(int bindedId);

    Relation findRelationByBinderAndBinded(int binderId, int bindedId);

    void unbind(Integer id);


    User findUserById(Integer binded);

    List<Relation> findRelationsByBinderID(int binderId);

    void updateRelation(Relation re);

    void deleteUserById(Integer id);

    Boolean is_shared(String old_gatewayids, String new_gatewayids);

    void deleteDeviceByGateId(String gateId);

    void deleteShareByCustomerId(String customerId,String gateId);
}
