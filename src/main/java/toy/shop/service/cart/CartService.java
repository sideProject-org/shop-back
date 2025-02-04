package toy.shop.service.cart;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toy.shop.cmmn.exception.AccessDeniedException;
import toy.shop.cmmn.exception.ConflictException;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.etc.Cart;
import toy.shop.domain.item.Item;
import toy.shop.domain.item.ItemImage;
import toy.shop.domain.member.Member;
import toy.shop.dto.cart.CartResponseDTO;
import toy.shop.dto.cart.CartSaveRequestDTO;
import toy.shop.dto.cart.CartUpdateRequestDTO;
import toy.shop.dto.item.ItemListResponseDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.cart.CartRepository;
import toy.shop.repository.item.ItemImageRepository;
import toy.shop.repository.item.ItemRepository;
import toy.shop.repository.member.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final CartRepository cartRepository;

    public List<CartResponseDTO> cartList(UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getUserId());
        List<Cart> cartList = cartRepository.findAllByMemberId(member.getId());
        List<CartResponseDTO> cartResponseDTOList = new ArrayList<>();

        if (cartList.isEmpty()) {
            throw new NotFoundException("장바구니에 상품이 존재하지 않습니다.");
        }

        for (Cart cart : cartList) {
            ItemImage itemImage = itemImageRepository.findFirstImageByItemId(cart.getItem().getId());
            ItemListResponseDTO itemListResponseDTO = ItemListResponseDTO.builder()
                    .id(cart.getItem().getId())
                    .name(cart.getItem().getName())
                    .price(cart.getItem().getPrice())
                    .sale(cart.getItem().getSale())
                    .itemImage(itemImage.getImagePath())
                    .build();

            CartResponseDTO cartResponseDTO = CartResponseDTO.builder()
                    .cartId(cart.getId())
                    .quantity(cart.getQuantity())
                    .item(itemListResponseDTO)
                    .build();

            cartResponseDTOList.add(cartResponseDTO);
        }

        return cartResponseDTOList;
    }

    /**
     * 사용자의 장바구니에 새로운 상품을 추가합니다.
     * 동일한 상품이 이미 장바구니에 존재할 경우 예외를 발생시킵니다.
     *
     * @param parameter 장바구니에 추가할 상품 정보와 수량을 담은 DTO
     * @param userDetails 현재 인증된 사용자의 정보를 담은 객체
     * @return 저장된 장바구니 항목의 ID
     * @throws IllegalArgumentException 동일한 상품이 이미 장바구니에 존재할 경우
     */
    public Long saveCart(CartSaveRequestDTO parameter, UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getUserId());
        Item item = getItem(parameter.getItemId());

        boolean exists = cartRepository.existsByMemberIdAndItemId(member.getId(), item.getId());
        if (exists) {
            throw new ConflictException("이미 장바구니에 있는 상품입니다.");
        }

        Cart cart = Cart.builder()
                .member(member)
                .item(item)
                .quantity(parameter.getQuantity())
                .build();

        Cart savedCart = cartRepository.save(cart);

        return savedCart.getId();
    }

    /**
     * 사용자의 장바구니 항목의 수량을 업데이트합니다.
     *
     * @param cartId 업데이트할 장바구니 항목의 ID
     * @param parameter 변경할 수량 정보를 담은 DTO
     * @param userDetails 현재 인증된 사용자의 정보를 담은 객체
     * @return 업데이트된 장바구니 항목의 ID
     * @throws AccessDeniedException 업데이트하려는 장바구니 항목이 로그인된 사용자와 연관되지 않은 경우
     * @throws IllegalArgumentException 사용자 또는 장바구니 항목 정보가 유효하지 않을 경우
     */
    @Transactional
    public Long updateCart(Long cartId, CartUpdateRequestDTO parameter, UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getUserId());
        Cart cart = getCart(cartId);

        if (!cart.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("로그인된 사용자의 장바구니 품목이 아닙니다.");
        }

        cart.updateQuantity(parameter.getQuantity());

        return cart.getId();
    }

    /**
     * 사용자의 장바구니에서 특정 항목을 삭제합니다.
     *
     * @param cartId 삭제할 장바구니 항목의 ID
     * @param userDetails 현재 인증된 사용자의 정보를 담은 객체
     * @throws AccessDeniedException 삭제하려는 장바구니 항목이 로그인된 사용자와 연관되지 않은 경우
     */
    public void deleteCart(Long cartId, UserDetailsImpl userDetails) {
        Member member = getMember(userDetails.getUserId());
        Cart cart = getCart(cartId);

        if (!cart.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("로그인된 사용자의 장바구니 품목이 아닙니다.");
        }

        cartRepository.deleteById(cartId);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findActiveItemById(itemId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다."));
    }

    private Cart getCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 장바구니 품목입니다."));
    }
}
