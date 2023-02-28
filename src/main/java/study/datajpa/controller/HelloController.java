package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final MemberRepository memberRepository;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    // 페이징
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5)Pageable pageable) {
        return memberRepository.findAll(pageable).map(member -> new MemberDto(member.getId(), member.getUsername(), "team"));
    }

}
