package tn.bankYam.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tn.bankYam.dto.*;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface FriendsMapper {

    List<Accounty> searchFriend(HashMap<String, Object> hashMap);

    List<Friend> selectFrList(Membery membery);

    Friendreq checkFrReq(HashMap<String, Object> hashMap);

    void insertFrReq(HashMap<String, Object> hashMap);

    List<Friendreq> selectReqList(Membery membery);

    List<Friendreq> selectRecList(Membery membery);

    List<Blocklist> selectBlList(Membery membery);
}