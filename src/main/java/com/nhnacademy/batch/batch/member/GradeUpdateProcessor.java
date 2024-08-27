package com.nhnacademy.batch.batch.member;

import com.nhnacademy.batch.entity.enums.PurchaseStatus;
import com.nhnacademy.batch.entity.member.Member;
import com.nhnacademy.batch.entity.member.enums.Grade;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.batch.item.ItemProcessor;

public class GradeUpdateProcessor implements ItemProcessor<Member, Member> {
	private final EntityManager entityManager;

	public GradeUpdateProcessor(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Member process(Member member) {
		// 총 사용 금액을 조회하는 쿼리 작성
		String jpql = "SELECT SUM(p.totalPrice - p.deliveryPrice) FROM Purchase p WHERE p.member.id = :memberId AND p.status = :status";
		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter("memberId", member.getId());
		query.setParameter("status", PurchaseStatus.CONFIRMATION);
		Long totalAmount = query.getSingleResult();
		if (totalAmount != null) {
			if (totalAmount >= 300000) {
				member.setGrade(Grade.Platinum);
			} else if (totalAmount>=200000) {
				member.setGrade(Grade.Gold);
			}else if(totalAmount >= 100000){
				member.setGrade(Grade.Royal);
			}
		}
		// 총 사용 금액에 따른 회원 등급 변경 로직
		return member;
	}
}
