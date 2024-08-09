package com.simple.basic.memo;

import com.simple.basic.command.MemoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper // 이게 붙은 인터패이스를 마이바티스가 인식함, 제거하면 동작X
public interface MemoMapper {
    public String hello();
    public void insert(MemoVO vo);
    public ArrayList<MemoVO> getList();
}
