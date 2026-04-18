package com.flc.repository;

import com.flc.model.Member;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MemberRepository {

    private final Map<String, Member> byId = new LinkedHashMap<>();

    public void save(Member member) {
        byId.put(member.getMemberId(), member);
    }

    public Optional<Member> findById(String memberId) {
        return Optional.ofNullable(byId.get(memberId));
    }

    public List<Member> findAll() {
        return new ArrayList<>(byId.values());
    }
}
