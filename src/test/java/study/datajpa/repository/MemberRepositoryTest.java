package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.enttity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
//    @PersistenceContext
//    EntityManager em;

    @Test
    public void testJpaMember() {
        System.out.println("memberRepository => " + memberRepository.getClass());
        Member member = new Member("MemberA");
        Member savedMember = memberRepository.save(member);

//        Optional<Member> findMember  = memberRepository.findById(savedMember.getId());
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        // 저장 조회
        Member member1 = new Member("1");
        Member member2 = new Member("2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
//        Member findMember1 = memberRepository.findById(member1.getId()).orElseThrow(() -> {throw new NullPointerException();});
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(member1).isEqualTo(findMember1);
        assertThat(member2).isEqualTo(findMember2);


        // 리스트 검증 조회
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);

    }

    @Test
    public void paging() {
        memberRepository.save(new Member("1", 1));
        memberRepository.save(new Member("2",1));
        memberRepository.save(new Member("3",1));
        memberRepository.save(new Member("4",1));
        memberRepository.save(new Member("5",1));

        int age = 1;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "username");
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // DTO 로 반환
        Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(),m.getUsername(),"team"));

        List<Member> content = page.getContent();

        System.out.println("======================");
        long totalElements = page.getTotalElements();
        System.out.println("======================");

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        System.out.println("totalElements = " + totalElements);
        
        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5); // 전체 데이터 개수
        assertThat(page.getNumber()).isEqualTo(0); // 현재 페이지
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue(); // 다음 페이지 여부


    }

    @Test
    public void bulkUpdate(){
        memberRepository.save(new Member("1", 1));
        memberRepository.save(new Member("2",1));
        memberRepository.save(new Member("3",1));
        memberRepository.save(new Member("4",1));
        memberRepository.save(new Member("5",1));


        // 벌크 연산 이후 같은 트랜잭션안에서 이루어지면 flush 필요
        int resultCount = memberRepository.bulkAgePlus(1);

//        em.flush(); // 영속성 컨텍스트에 남아있는 변경되지 않은 내용들이 모두 디비에 반영됨.
//        em.clear();

        List<Member> byUsername = memberRepository.findByUsername("5");
        Member member5 = byUsername.get(0);
        System.out.println("member5 = " + member5);

    }

}
