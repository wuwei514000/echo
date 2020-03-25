package com.kaikeba.messagepack.test;

import com.kaikeba.messagepack.Member;
import org.msgpack.MessagePack;
import org.msgpack.template.Template;
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
        //序列化
        MessagePack msgPack = new MessagePack();
        byte[] data = msgPack.write(allMemberList);
        System.out.println(data.length);

        {//反序列化
            List<Member> memberList = msgPack.read(data, Templates.tList(msgPack.lookup(Member.class)));
            System.out.println(memberList);

        }
    }
}
