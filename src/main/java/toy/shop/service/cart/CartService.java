package toy.shop.service.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.etc.Cart;
import toy.shop.domain.item.Item;
import toy.shop.domain.member.Member;
import toy.shop.dto.cart.CartSaveRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.cart.CartRepository;
import toy.shop.repository.item.ItemRepository;
import toy.shop.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class CartService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    /**
     * 사용자의 장바구니에 아이템을 추가합니다.
     *
     * @param parameter 장바구니에 저장할 아이템 정보와 수량을 담은 DTO
     * @param userDetails 현재 인증된 사용자의 정보를 담은 객체
     * @return 저장된 장바구니 항목의 ID
     * @throws IllegalArgumentException 사용자 또는 아이템 정보가 유효하지 않을 경우
     */
    public Long saveCart(CartSaveRequestDTO parameter, UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getUserId());
        Item item = getItem(parameter.getItemId());

        Cart cart = Cart.builder()
                .member(member)
                .item(item)
                .quantity(parameter.getQuantity())
                .build();

        Cart savedCart = cartRepository.save(cart);

        return savedCart.getId();
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다."));
    }
}
