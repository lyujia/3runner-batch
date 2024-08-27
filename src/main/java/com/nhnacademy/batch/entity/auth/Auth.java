package com.nhnacademy.batch.entity.auth;

import com.nhnacademy.batch.entity.memberauth.MemberAuth;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Setter
    @Size(min = 1, max = 50)
    private String name;

    @OneToMany(mappedBy = "auth",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberAuth> memberAuthSet = new ArrayList<>();

}
