package com.kaikeba.marshalling.test;

import com.kaikeba.marshalling.Member;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageMemTest {
    public static void main(String[] args) throws IOException {
        List<Member> allMemberList = new ArrayList<Member>();
        for(int i = 0; i < 10; i++){
            Member member = new Member();
            member.setMid("MLDM-" + i);
            member.setAge(24+i);
            member.setName("Hello" + i);
            member.setSalary(112.1);
            allMemberList.add(member);
        }
    }
}
