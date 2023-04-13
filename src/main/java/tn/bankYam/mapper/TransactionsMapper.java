package tn.bankYam.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tn.bankYam.dto.Transactions;

import java.util.List;

@Mapper
@Repository
public interface TransactionsMapper {
    void insertTrLog(Transactions transactions);
    List<Transactions> listTrLog();
}