package toy.shop.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import toy.shop.domain.BaseEntity;
import toy.shop.domain.Role;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String imagePath;

    @Column
    private String phoneNumber;

    @Column(nullable = false)
    @ColumnDefault("'N'")
    private char banType = 'N';

    @Column(nullable = false)
    @ColumnDefault("'N'")
    private char deleteType = 'N';

    @Builder
    public Member(String email, String password, String nickName, String gender, Role role, String imagePath, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.gender = gender;
        this.role = role;
        this.imagePath = imagePath;
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
