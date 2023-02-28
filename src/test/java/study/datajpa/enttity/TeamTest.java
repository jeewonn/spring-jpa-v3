package study.datajpa.enttity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class TeamTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity() {
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");

        em.persist(team1);
        em.persist(team2);

        Member member1 = new Member("member1", 10, team1);
        Member member2 = new Member("member1", 20, team1);
        Member member3 = new Member("member1", 30, team2);
        Member member4 = new Member("member1", 40, team2);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 초기화 (영속성 컨텍스트 종료)
        em.flush();
        em.clear();

        List<Member> select_m_from_member_m = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : select_m_from_member_m) {
            System.out.println("member = " + member);
            System.out.println("member.team = " + member.getTeam());

        }
    }

}