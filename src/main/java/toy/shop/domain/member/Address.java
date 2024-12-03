package toy.shop.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String addr;

    @Column(nullable = false)
    private String addrDetail;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String zipCode;

    @Column
    private String request;

    @Column(nullable = false)
    @ColumnDefault("'N'")
    private char defaultType = 'N';

    @Builder
    public Address(Member member, String name, String addr, String addrDetail, String phone, String zipCode, String request) {
        this.member = member;
        this.name = name;
        this.addr = addr;
        this.addrDetail = addrDetail;
        this.phone = phone;
        this.zipCode = zipCode;
        this.request = request;
    }
}
